package world.jdl.rest;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author xgraza
 * @since 9/8/25
 */
public final class RESTAction<T>
{
    private final RESTClient restClient;
    private final HttpRequest httpRequest;
    private final Endpoints.Endpoint<T> endpoint;

    private Callback<T> successCallback;
    private Callback<Throwable> errorCallback = Throwable::printStackTrace;

    private boolean throttled;

    public RESTAction(final RESTClient restClient,
                      final HttpRequest httpRequest,
                      final Endpoints.Endpoint<T> endpoint)
    {
        this.restClient = restClient;
        this.httpRequest = httpRequest;
        this.endpoint = endpoint;
    }

    public void complete(final Callback<T> successCallback, final Callback<Throwable> errorCallback)
    {
        this.successCallback = successCallback;
        this.errorCallback = errorCallback;
        complete();
    }

    public void complete(final Callback<T> successCallback)
    {
        this.successCallback = successCallback;
        complete();
    }

    public void complete()
    {
        try
        {
            if (isThrottled())
            {
                return;
            }
            final HttpResponse<String> response = restClient.getHttpClient().send(httpRequest,
                    HttpResponse.BodyHandlers.ofString());
            if (restClient.checkForRateLimit(this, response))
            {
                return;
            }
            if (successCallback != null)
            {
                successCallback.callback(RESTClient.GSON.fromJson(
                        response.body(), endpoint.responseType()));
            }
        } catch (IOException | InterruptedException e)
        {
            if (errorCallback != null)
            {
                errorCallback.callback(e);
            }
        }
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

    @FunctionalInterface
    public interface Callback<T>
    {
        void callback(T completed);
    }
}
