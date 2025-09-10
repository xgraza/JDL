package world.jdl.structure.user;

import com.google.gson.annotations.SerializedName;
import world.jdl.structure.Snowflake;

public class User
{
    private Snowflake id;
    private String username;
    private String discriminator;
    @SerializedName("global_name")
    private String globalName;
    @SerializedName("avatar")
    private String avatarHash;
    private boolean bot;
    private boolean system;
    @SerializedName("mfa_enabled")
    private boolean mfaEnabled;
    @SerializedName("banner_hash")
    private String bannerHash;
    @SerializedName("accent_color")
    private int accentColor;
    private String locale;
    private int flags;
    @SerializedName("premium_type")
    private int premiumType;
    @SerializedName("public_flags")
    private int publicFlags;

    public Snowflake getId()
    {
        return id;
    }

    public void setId(Snowflake id)
    {
        this.id = id;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getDiscriminator()
    {
        return discriminator;
    }

    public void setDiscriminator(String discriminator)
    {
        this.discriminator = discriminator;
    }

    public String getGlobalName()
    {
        return globalName;
    }

    public void setGlobalName(String globalName)
    {
        this.globalName = globalName;
    }

    public String getAvatarHash()
    {
        return avatarHash;
    }

    public void setAvatarHash(String avatarHash)
    {
        this.avatarHash = avatarHash;
    }

    public boolean isBot()
    {
        return bot;
    }

    public void setBot(boolean bot)
    {
        this.bot = bot;
    }

    public boolean isSystem()
    {
        return system;
    }

    public void setSystem(boolean system)
    {
        this.system = system;
    }

    public boolean isMfaEnabled()
    {
        return mfaEnabled;
    }

    public void setMfaEnabled(boolean mfaEnabled)
    {
        this.mfaEnabled = mfaEnabled;
    }

    public String getBannerHash()
    {
        return bannerHash;
    }

    public void setBannerHash(String bannerHash)
    {
        this.bannerHash = bannerHash;
    }

    public int getAccentColor()
    {
        return accentColor;
    }

    public void setAccentColor(int accentColor)
    {
        this.accentColor = accentColor;
    }

    public String getLocale()
    {
        return locale;
    }

    public void setLocale(String locale)
    {
        this.locale = locale;
    }

    public int getFlags()
    {
        return flags;
    }

    public void setFlags(int flags)
    {
        this.flags = flags;
    }

    public int getPremiumType()
    {
        return premiumType;
    }

    public void setPremiumType(int premiumType)
    {
        this.premiumType = premiumType;
    }

    public int getPublicFlags()
    {
        return publicFlags;
    }

    public void setPublicFlags(int publicFlags)
    {
        this.publicFlags = publicFlags;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", discriminator='" + discriminator + '\'' +
                ", globalName='" + globalName + '\'' +
                ", avatarHash='" + avatarHash + '\'' +
                ", bot=" + bot +
                ", system=" + system +
                ", mfaEnabled=" + mfaEnabled +
                ", bannerHash='" + bannerHash + '\'' +
                ", accentColor=" + accentColor +
                ", locale='" + locale + '\'' +
                ", flags=" + flags +
                ", premiumType=" + premiumType +
                ", publicFlags=" + publicFlags +
                '}';
    }
}
