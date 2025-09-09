package world.jdl.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.Strictness;
import world.jdl.structure.Snowflake;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author xgraza
 * @since 9/8/25
 */
public final class RESTClient
{
    private static final String DISCORD_API_URL = "https://discord.com/api/v10";
    private static final String USER_AGENT = "DiscordBot (https://github.com/xgraza/JDL, 1.0.0)";

    static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Snowflake.class, new Snowflake.SnowflakeDeserializer())
            .registerTypeAdapter(Snowflake.class, new Snowflake.SnowflakeSerializer())
            .setStrictness(Strictness.LENIENT)
            .create();

    private final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .connectTimeout(Duration.ofSeconds(5L))
            .build();
    private final Timer rateLimitScheduler = new Timer();
    private final String authorization;

    private final Map<String, RateLimit> rateLimitedRouteMap = new ConcurrentHashMap<>();
    private final Set<String> currentRateLimitedRouteSet = new LinkedHashSet<>();

    public RESTClient(final String authorization)
    {
        this.authorization = authorization;

        rateLimitScheduler.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                for (final String rateLimitedRoute : currentRateLimitedRouteSet)
                {
                    final RateLimit rateLimit = rateLimitedRouteMap.get(rateLimitedRoute);
                    if (rateLimit == null || rateLimit.getResetAtMS() == -1L)
                    {
                        continue;
                    }
                    final long rateLimitOverIn = Math.max(0L, rateLimit.getResetAtMS() - System.currentTimeMillis());
                    if (rateLimitOverIn > 0L)
                    {
                        continue;
                    }

                    rateLimit.setRemaining(rateLimit.getLimit());
                    final Queue<RESTAction<?>> queuedRestActions = rateLimit.getActionQueue();
                    for (int i = 0; i < rateLimit.getLimit() - 1; ++i)
                    {
                        if (rateLimit.getRemaining() <= 0)
                        {
                            break;
                        }
                        final RESTAction<?> restAction = queuedRestActions.poll();
                        if (restAction == null)
                        {
                            break;
                        }
                        rateLimit.setRemaining(rateLimit.getRemaining() - 1);
                        restAction.setThrottled(false);
                        restAction.complete();
                        if (restAction.isThrottled())
                        {
                            break;
                        }
                    }
                    if (queuedRestActions.isEmpty())
                    {
                        rateLimit.setResetAtMS(-1L);
                        currentRateLimitedRouteSet.remove(rateLimitedRoute);
                    }
                }
            }
        }, 0L, 1500L);
    }

    /**
     * Creates an HTTP/GET request
     * @param endpoint the {@link world.jdl.rest.Endpoints.Endpoint<T>}
     * @return a {@link RESTAction<T>} to act upon
     * @param <T> the expected type
     */
    public <T> RESTAction<T> get(final Endpoints.Endpoint<T> endpoint)
    {
        final HttpRequest request = createRequest(endpoint).GET().build();
        return new RESTAction<>(this, request, endpoint);
    }

    public <T> T getAsync(final Endpoints.Endpoint<T> endpoint)
            throws IOException, InterruptedException
    {
        final RESTAction<T> restAction = get(endpoint);
        return restAction.execute();
    }

    /**
     * Creates a HTTP/POST request
     * @param endpoint the {@link world.jdl.rest.Endpoints.Endpoint<T>}
     * @param body an {@link Object} for what the body should be
     * @return a {@link RESTAction<T>} to act upon
     * @param <T> the expected type
     */
    public <T> RESTAction<T> post(final Endpoints.Endpoint<T> endpoint,
                                  final Object body)
    {
        final HttpRequest.Builder requestBuilder = createRequest(endpoint);
        if (body instanceof JsonElement element)
        {
            requestBuilder.setHeader("Content-Type", "application/json");
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(element)));
        }
        return new RESTAction<>(this, requestBuilder.build(), endpoint);
    }

    /**
     * Checks a {@link HttpResponse<?>}'s status code & x-ratelimit headers for Rate limits
     * @param restAction the {@link RESTAction<?>}
     * @param response the {@link HttpResponse<?>}
     * @return if this request was rate limited
     */
    boolean checkForRateLimit(final RESTAction<?> restAction, final HttpResponse<?> response)
    {
        final String endpointRoute = restAction.getEndpoint().route();
        final HttpHeaders headers = response.headers();

        final String remainingHeader = getHeader(headers.firstValue("x-ratelimit-remaining"));
        final String retryAfterHeader = getHeader(headers.firstValue("x-ratelimit-reset-after"));
        final String limitHeader = getHeader(headers.firstValue("x-ratelimit-limit"));

        if (remainingHeader == null || retryAfterHeader == null || limitHeader == null)
        {
            return false;
        }

        final int remaining = Integer.parseInt(remainingHeader);
        final int limit = Integer.parseInt(limitHeader);
        final long resetAtMS = System.currentTimeMillis()
                + (long)(Double.parseDouble(retryAfterHeader) * 1000.0);

        final RateLimit rateLimit = rateLimitedRouteMap.computeIfAbsent(
                endpointRoute, RateLimit::new);
        rateLimit.setLimit(limit);
        rateLimit.setRemaining(remaining);

        if (rateLimit.getRemaining() <= 0)
        {
            currentRateLimitedRouteSet.add(endpointRoute);

            if (!restAction.isThrottled())
            {
                rateLimit.getActionQueue().add(restAction);
            }
            restAction.setThrottled(true);
            rateLimit.setResetAtMS(resetAtMS);
            return true;
        }

        return false;
    }

    String getHeader(final Optional<String> optional)
    {
        return optional.orElse(null);
    }

    /**
     * Creates a base request
     * @param endpoint the {@link world.jdl.rest.Endpoints.Endpoint<T>} object
     * @return a {@link HttpRequest.Builder}
     * @param <T> the expected response type
     */
    private <T> HttpRequest.Builder createRequest(final Endpoints.Endpoint<T> endpoint)
    {
        return HttpRequest.newBuilder()
                .uri(URI.create(DISCORD_API_URL + endpoint.route()))
                .setHeader("User-Agent", USER_AGENT)
                .setHeader("Authorization", String.format("Bot %s", authorization));
    }

    HttpClient getHttpClient()
    {
        return httpClient;
    }

    private static final class RateLimit
    {
        private int remaining, limit;
        private long resetAtMS;

        private final Queue<RESTAction<?>> actionQueue = new ConcurrentLinkedQueue<>();

        public RateLimit(final String route)
        {

        }

        public Queue<RESTAction<?>> getActionQueue()
        {
            return actionQueue;
        }

        public int getRemaining()
        {
            return remaining;
        }

        public void setRemaining(int remaining)
        {
            this.remaining = remaining;
        }

        public int getLimit()
        {
            return limit;
        }

        public void setLimit(int limit)
        {
            this.limit = limit;
        }

        public long getResetAtMS()
        {
            return resetAtMS;
        }

        public void setResetAtMS(long resetAtMS)
        {
            this.resetAtMS = resetAtMS;
        }
    }
}
