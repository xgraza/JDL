package world.jdl.structure.user.activity;

import java.util.LinkedList;
import java.util.List;

/**
 * @author xgraza
 * @since 9/8/25
 */
public final class Presence
{
    private int since;
    private List<Activity> activities = new LinkedList<>();
    private String status;
    private boolean afk;

    public Presence(String status, boolean afk)
    {
        this.since = 0;
        this.status = status;
        this.afk = afk;
    }

    public int getSince()
    {
        return since;
    }

    public void addActivity(final Activity activity)
    {
        activities.add(activity);
    }

    public String getStatus()
    {
        return status;
    }

    public boolean isAfk()
    {
        return afk;
    }

    public void setAfk(boolean afk)
    {
        this.afk = afk;
    }

    public static Presence empty()
    {
        return new Presence("online", false);
    }

    public static Presence dnd()
    {
        return new Presence("dnd", false);
    }

    public static Presence idle()
    {
        return new Presence("idle", false);
    }

    public static Presence invisible()
    {
        return new Presence("invisible", false);
    }

    public static Presence offline()
    {
        return new Presence("offline", false);
    }
}
