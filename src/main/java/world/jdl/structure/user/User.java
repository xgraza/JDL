package world.jdl.structure.user;

import com.google.gson.annotations.SerializedName;
import world.jdl.structure.IdentifiableObject;
import world.jdl.structure.flag.FlagContainer;
import world.jdl.structure.user.flag.NitroType;
import world.jdl.structure.user.flag.UserFlag;

/**
 * @author xgraza
 * @since 9/9/25
 */
public class User extends IdentifiableObject
{
    private String username;
    private String discriminator;
    @SerializedName("global_name")
    private String globalName;
    @SerializedName("avatar")
    private String avatarHash;
    private boolean bot;
    private boolean system;
    @SerializedName("banner_hash")
    private String bannerHash;
    @SerializedName("accent_color")
    private int accentColor;
    private String locale;
    private FlagContainer<UserFlag> flags;
    @SerializedName("premium_type")
    private NitroType nitroType = NitroType.NONE;
    @SerializedName("public_flags")
    private FlagContainer<UserFlag> publicFlags;

    public String getUsername()
    {
        return username;
    }

    public String getDiscriminator()
    {
        return discriminator;
    }

    public String getGlobalName()
    {
        return globalName;
    }

    public String getAvatarHash()
    {
        return avatarHash;
    }

    public boolean isBot()
    {
        return bot;
    }

    public boolean isSystem()
    {
        return system;
    }

    public String getBannerHash()
    {
        return bannerHash;
    }

    public int getAccentColor()
    {
        return accentColor;
    }

    public String getLocale()
    {
        return locale;
    }

    public FlagContainer<UserFlag> getFlags()
    {
        return flags;
    }

    public NitroType getNitroType()
    {
        return nitroType;
    }

    public FlagContainer<UserFlag> getPublicFlags()
    {
        return publicFlags;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + getSnowflake() +
                ", username='" + username + '\'' +
                ", discriminator='" + discriminator + '\'' +
                ", globalName='" + globalName + '\'' +
                ", avatarHash='" + avatarHash + '\'' +
                ", bot=" + bot +
                ", system=" + system +
                ", bannerHash='" + bannerHash + '\'' +
                ", accentColor=" + accentColor +
                ", locale='" + locale + '\'' +
                ", flags=" + flags +
                ", premiumType=" + nitroType +
                ", publicFlags=" + publicFlags +
                '}';
    }

    /**
     * Checks if a new username is acceptable
     * @param username the new username
     * @see <a href="https://discord.com/developers/docs/resources/user#usernames-and-nicknames">Discord Username & Nickname Documentation</a>
     * @return if the username is acceptable
     */
    public static boolean isValidUsername(final String username)
    {
        final int length = username.length();
        if (length < 2 || length > 32)
        {
            return false;
        }
        if (username.equalsIgnoreCase("everyone")
                || username.equalsIgnoreCase("here"))
        {
            return false;
        }
        return !username.contains("@")
                && !username.contains("#")
                && !username.contains(":")
                && !username.contains("```")
                && !username.contains("discord");
    }
}
