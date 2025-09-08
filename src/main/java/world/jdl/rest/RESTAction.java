package world.jdl.rest;

import world.jdl.rest.callback.RESTCallback;
import world.jdl.rest.callback.RESTErrorCallback;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author xgraza
 * @since 9/7/25
 * @param <T> the resulting type when this {@link RESTAction} is completed
 */
public class RESTAction<T>
{
    protected final RESTClient client;
    public final HttpRequest.Builder request;
    protected final DiscordEndpoints.Endpoint<?> endpoint;

    protected RESTCallback<T> callback;
    protected RESTErrorCallback<Throwable> errorCallback;

    private boolean ratelimited;

    public RESTAction(final RESTClient client,
                      final HttpRequest.Builder request,
                      final DiscordEndpoints.Endpoint<?> endpoint)
    {
        this.client = client;
        this.request = request;
        this.endpoint = endpoint;
    }

    public void queue()
    {
        queue(null, null);
    }

    public void queue(final RESTCallback<T> callback)
    {
        queue(callback, null);
    }

    public void queue(final RESTCallback<T> callback, final RESTErrorCallback<Throwable> errorCallback)
    {
        if (this.callback == null)
        {
            this.callback = callback;
        }
        if (this.errorCallback == null)
        {
            this.errorCallback = errorCallback;
        }
        sendRequest();
    }

    void setRatelimited(boolean ratelimited)
    {
        this.ratelimited = ratelimited;
    }

    boolean isRatelimited()
    {
        return ratelimited;
    }

    private void makeRequest() throws IOException, InterruptedException
    {
        if (ratelimited)
        {
            return;
        }
        final HttpResponse<String> response = client.httpClient.send(request.build(),
                HttpResponse.BodyHandlers.ofString());
        if (client.parseRateLimitHeaders(response, this, endpoint))
        {
            return;
        }
        if (callback != null)
        {
            Object type = RESTClient.GSON.fromJson(response.body(), endpoint.responseType());
            callback.callback((T) type);
        }
    }

    private void sendRequest()
    {
        try
        {
            makeRequest();
        } catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
