package world.jdl.rest;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FormattingStyle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class RESTClient
{
    public static final String DISCORD_API_URL = "https://discord.com/api/v10/";
    public static final String REQUEST_USER_AGENT = "DiscordBot (https://github.com/xgraza/JDL, 1.0.0)";

    public static final Gson GSON = new GsonBuilder()
            .setFieldNamingStrategy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setFormattingStyle(FormattingStyle.PRETTY)
            .serializeNulls()
            .create();

    private final HttpClient client = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.ALWAYS)
            .build();
    private final String botToken;

    public RESTClient(final String botToken)
    {
        this.botToken = botToken;
    }

    public <T> RESTAction<T> get(final DiscordEndpoints.Endpoint<?> endpoint)
    {
        try
        {
            final HttpRequest.Builder request = createBaseRequest(endpoint);
            request.method(endpoint.method(), HttpRequest.BodyPublishers.noBody());
            return new RESTAction<>(client, request.build());
        } catch (URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }

    private HttpRequest.Builder createBaseRequest(final DiscordEndpoints.Endpoint<?> endpoint)
            throws URISyntaxException
    {
        return HttpRequest.newBuilder()
                .uri(new URI(DISCORD_API_URL + endpoint.endpoint()))
                .header("User-Agent", REQUEST_USER_AGENT)
                .header("Authorization", "Bot " + botToken);
    }
}
