package world.jdl.structure.guild;

import com.google.gson.annotations.SerializedName;
import world.jdl.structure.IdentifiableObject;
import world.jdl.structure.user.User;

public final class GuildEmoji extends IdentifiableObject
{
    private String name;
    // private List<Role> roles;
    private User user;
    @SerializedName("require_colons")
    private boolean requireColons;
    private boolean managed;
    private boolean animated;
    private boolean available;

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
