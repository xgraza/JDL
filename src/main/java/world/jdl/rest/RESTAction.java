package world.jdl.rest;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Consumer;

/**
 * @author xgraza
 * @since 9/8/25
 */
public final class RESTAction<T>
{
    private final RESTClient restClient;
    private final HttpRequest httpRequest;
    private final Endpoints.Endpoint<T> endpoint;

    private Consumer<T> successCallback;
    private Consumer<Throwable> errorCallback = Throwable::printStackTrace;

    private boolean throttled;

    public RESTAction(final RESTClient restClient,
                      final HttpRequest httpRequest,
                      final Endpoints.Endpoint<T> endpoint)
    {
        this.restClient = restClient;
        this.httpRequest = httpRequest;
        this.endpoint = endpoint;
    }

    /**
     * Completes ths {@link RESTAction} with a success and an error callback
     * @param successCallback callback to accept on success
     * @param errorCallback callback to accept on failure
     */
    public void complete(final Consumer<T> successCallback, final Consumer<Throwable> errorCallback)
    {
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
        complete();
    }

    /**
     * Completes this {@link RESTAction} with a success callback
     * @param successCallback callback to accept on success
     */
    public void complete(final Consumer<T> successCallback)
    {
        this.successCallback = successCallback;
        complete();
    }

    /**
     * Completes this {@link RESTAction}
     */
    public void complete()
    {
        try
        {
            if (isThrottled())
            {
                return;
            }
            if (successCallback != null)
            {
                successCallback.accept(execute());
            }
        } catch (IOException | InterruptedException e)
        {
            if (errorCallback != null)
            {
                errorCallback.accept(e);
            }
        }
    }

    /**
     * Executes this {@link RESTAction}
     * @return {@link T} or null
     * @throws IOException
     * @throws InterruptedException
     */
    public T execute() throws IOException, InterruptedException
    {
        final HttpResponse<String> response = restClient.getHttpClient().send(
                httpRequest, HttpResponse.BodyHandlers.ofString());
        if (restClient.handleRateLimit(this, response))
        {
            return null;
        }
        return RESTClient.GSON.fromJson(response.body(), endpoint.responseType());
    }

    public Endpoints.Endpoint<T> getEndpoint()
    {
        return endpoint;
    }

    void setThrottled(boolean throttled)
    {
        this.throttled = throttled;
    }

    public boolean isThrottled()
    {
        return throttled;
    }
}
