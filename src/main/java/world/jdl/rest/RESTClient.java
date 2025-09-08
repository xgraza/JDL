package world.jdl.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FormattingStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import world.jdl.structure.Snowflake;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class RESTClient
{
    public static final String DISCORD_API_URL = "https://discord.com/api/v10/";
    public static final String REQUEST_USER_AGENT = "DiscordBot (https://github.com/xgraza/JDL, 1.0.0)";

    private static final Timer RATE_LIMIT_SCHEDULER = new Timer();
    static final Gson GSON = new GsonBuilder()
            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setFormattingStyle(FormattingStyle.PRETTY)
            .serializeNulls()
            .registerTypeAdapter(Snowflake.class, new Snowflake.SnowflakeSerializer())
            .registerTypeAdapter(Snowflake.class, new Snowflake.SnowflakeDeserializer())
            .create();

    private final Map<String, RateLimit> ratelimitMap = new ConcurrentHashMap<>();
    private final Set<String> rateLimitedRoutesSet = new LinkedHashSet<>();

    final HttpClient httpClient = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();
    private final String botToken;

    public RESTClient(final String botToken)
    {
        this.botToken = botToken;

        // TODO: duplicates packets...
        RATE_LIMIT_SCHEDULER.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                if (rateLimitedRoutesSet.isEmpty())
                {
                    return;
                }

                for (final String route : rateLimitedRoutesSet)
                {
                    final RateLimit rateLimit = ratelimitMap.get(route);
                    if (rateLimit == null || !rateLimit.isRateLimitOver())
                    {
                        continue;
                    }
                    // Reset how many requests we have until we reach max
                    rateLimit.setRemaining(rateLimit.getMaxRequests());

                    final Queue<RESTAction<?>> restActionQueue = rateLimit.getActionQueue();
                    while (!restActionQueue.isEmpty())
                    {
                        if (rateLimit.getRemaining() <= 0)
                        {
                            break;
                        }
                        final RESTAction<?> restAction = restActionQueue.poll();
                        if (restAction == null)
                        {
                            break;
                        }
                        restAction.setRatelimited(false);
                        restAction.queue();
                        rateLimit.setRemaining(rateLimit.getRemaining() - 1);
                    }

                    if (restActionQueue.isEmpty())
                    {
                        rateLimitedRoutesSet.remove(route);
                        break;
                    }
                }
            }
        }, 0L, 500L);
    }

    public <T> RESTAction<T> get(final DiscordEndpoints.Endpoint<?> endpoint)
    {
        try
        {
            final HttpRequest.Builder request = createBaseRequest(endpoint);
            request.method(endpoint.method(), HttpRequest.BodyPublishers.noBody());
            final RESTAction<T> restAction = new RESTAction<>(this, request, endpoint);
            handleRateLimiting(endpoint, restAction);
            return restAction;
        } catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    boolean parseRateLimitHeaders(final HttpResponse<?> response,
                                  final RESTAction<?> restAction,
                                  final DiscordEndpoints.Endpoint<?> endpoint)
    {
        final HttpHeaders headers = response.headers();

        final Optional<String> rateLimitLimitHeader = headers.firstValue("x-ratelimit-limit");
        final Optional<String> rateLimitResetAfterHeader = headers.firstValue("x-ratelimit-reset-after");
        final Optional<String> rateLimitRemainingHeader = headers.firstValue("x-ratelimit-remaining");
        if (rateLimitLimitHeader.isPresent()
                && rateLimitResetAfterHeader.isPresent()
                && rateLimitRemainingHeader.isPresent())
        {
            final int limit = Integer.parseInt(rateLimitLimitHeader.get());
            final double resetAfter = Double.parseDouble(rateLimitResetAfterHeader.get());
            final long resetAt = (long) (System.currentTimeMillis() + (resetAfter * 1000.0));
            final int remaining = Integer.parseInt(rateLimitRemainingHeader.get());
            if (remaining > 0)
            {
                return false;
            }

            final RateLimit rateLimit = ratelimitMap.computeIfAbsent(
                    endpoint.route(), RateLimit::new);

            rateLimitedRoutesSet.add(rateLimit.getRoute());
            if (!restAction.isRatelimited())
            {
                rateLimit.queue(restAction);
            }
            restAction.setRatelimited(true);
            rateLimit.setRemaining(remaining);
            rateLimit.setMaxRequests(limit);
            rateLimit.setResetAt(resetAt);
        }

        return true;
    }

    private void handleRateLimiting(final DiscordEndpoints.Endpoint<?> endpoint,
                                    final RESTAction<?> restAction)
    {
        final RateLimit rateLimit = ratelimitMap.get(endpoint.route());
        if (rateLimit != null && rateLimit.isRateLimited())
        {
            rateLimitedRoutesSet.add(endpoint.route());
            if (!restAction.isRatelimited())
            {
                rateLimit.queue(restAction);
            }
            restAction.setRatelimited(true);
        }
    }

    private HttpRequest.Builder createBaseRequest(final DiscordEndpoints.Endpoint<?> endpoint)
            throws URISyntaxException
    {
        return HttpRequest.newBuilder()
                .uri(new URI(DISCORD_API_URL + endpoint.route()))
                .header("User-Agent", REQUEST_USER_AGENT)
                .header("Authorization", "Bot " + botToken);
    }

    static final class RateLimit
    {
        private final String route;
        private final Queue<RESTAction<?>> actionQueue = new ConcurrentLinkedQueue<>();
        private long resetAt = -1L;
        private int maxRequests, remaining;

        RateLimit(final String route)
        {
            this.route = route;
        }

        void queue(final RESTAction<?> restAction)
        {
            actionQueue.add(restAction);
        }

        Queue<RESTAction<?>> getActionQueue()
        {
            return actionQueue;
        }

        String getRoute()
        {
            return route;
        }

        void setResetAt(final long resetAt)
        {
            this.resetAt = resetAt;
        }

        void setMaxRequests(int maxRequests)
        {
            this.maxRequests = maxRequests;
        }

        int getMaxRequests()
        {
            return maxRequests;
        }

        void setRemaining(int remaining)
        {
            this.remaining = remaining;
        }

        int getRemaining()
        {
            return remaining;
        }

        boolean isRateLimited()
        {
            return resetAt != -1;
        }

        boolean isRateLimitOver()
        {
            return isRateLimited() && System.currentTimeMillis() - resetAt >= 50L;
        }
    }
}
