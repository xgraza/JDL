package world.jdl.structure.guild;

import com.google.gson.annotations.SerializedName;
import world.jdl.structure.Snowflake;
import world.jdl.structure.user.User;

public final class GuildEmoji
{
    private Snowflake id;
    private String name;
    // private List<Role> roles;
    private User user;
    @SerializedName("require_colons")
    private boolean requireColons;
    private boolean managed;
    private boolean animated;
    private boolean available;

    public Snowflake getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public User getUser()
    {
        return user;
    }

    public boolean isRequireColons()
    {
        return requireColons;
    }

    public boolean isManaged()
    {
        return managed;
    }

    public boolean isAnimated()
    {
        return animated;
    }

    public boolean isAvailable()
    {
        return available;
    }
}
