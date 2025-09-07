package world.jdl.rest;

import world.jdl.rest.callback.RESTCallback;
import world.jdl.rest.callback.RESTErrorCallback;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author xgraza
 * @since 9/7/25
 * @param <T> the resulting type when this {@link RESTAction} is completed
 */
public class RESTAction<T>
{
    private final HttpClient client;
    private final HttpRequest request;

    private RESTCallback<T> callback;
    private RESTErrorCallback<Throwable> errorCallback;

    public RESTAction(final HttpClient client, final HttpRequest request)
    {
        this.client = client;
        this.request = request;
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
        this.callback = callback;
        this.errorCallback = errorCallback;
    }

    private void sendRequest()
    {
        try
        {
            final HttpResponse<?> response = client.send(request, null);

        } catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
