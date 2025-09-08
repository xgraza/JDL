package world.jdl.structure.user.activity;

import com.google.gson.annotations.SerializedName;

/**
 * @author xgraza
 * @since 9/8/25
 */
public final class Activity
{
    private String name;
    private int type;
    @SerializedName("created_at")
    private long createdAt;

    private String details, state, url;

    public Activity(String name, int type)
    {
        this.name = name;
        this.type = type;
        this.createdAt = System.currentTimeMillis();
    }

    public static Activity playing(final String name)
    {
        return new Activity(name, 0);
    }

    public static Activity streaming(final String details, final String url)
    {
        if (!isValidStreamingURL(url))
        {
            throw new RuntimeException("Discord does not support that streaming URL");
        }
        final Activity activity = new Activity(null, 1);
        activity.details = details;
        activity.url = url;
        return activity;
    }

    public static Activity listening(final String name)
    {
        return new Activity(name, 2);
    }

    public static Activity watching(final String name)
    {
        return new Activity(name, 3);
    }

    public static Activity custom(final Object emoji, final String state)
    {
        final Activity activity = new Activity(null, 4);
        activity.state = state;
        return activity;
    }

    public static Activity competing(final String name)
    {
        return new Activity(name, 5);
    }

    private static boolean isValidStreamingURL(final String url)
    {
        return true;
    }
}
