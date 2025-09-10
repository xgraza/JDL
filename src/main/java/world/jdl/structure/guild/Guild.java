package world.jdl.structure.guild;

import com.google.gson.annotations.SerializedName;
import world.jdl.structure.IdentifiableObject;
import world.jdl.structure.Snowflake;
import world.jdl.structure.sticker.Sticker;

import java.util.LinkedList;
import java.util.List;

public class Guild extends IdentifiableObject
{
    private boolean unavailable;
    private String name;
    @SerializedName("icon")
    private String iconHash;
    private String splash;
    @SerializedName("discovery_splash")
    private String discoverySplash;
    private final List<GuildEmoji> emojis = new LinkedList<>();
    private final List<String> features = new LinkedList<>();
    @SerializedName("approximate_member_count")
    private int approxMemberCount;
    @SerializedName("approximate_presence_count")
    private int approxPresenceCount;
    private String description;
    private final List<Sticker> stickers = new LinkedList<>();
    @SerializedName("owner_id")
    private Snowflake ownerId;
    @SerializedName("afk_channel_id")
    private Snowflake afkChannelId;
    @SerializedName("afk_timeout")
    private int afkTimeout;
    @SerializedName("widget_enabled")
    private boolean widgetEnabled;
    @SerializedName("widget_channel_id")
    private Snowflake widgetChannelId;
    @SerializedName("verification_level")
    private int verificationLevel;
    @SerializedName("default_message_notifications")
    private int defaultMessageNotifications;
    @SerializedName("explicit_content_filter")
    private int explicitContentFilter;
    // private final List<Role> roles = new LinkedList<>();
    @SerializedName("mfa_level")
    private int mfaLevel;
    @SerializedName("application_id")
    private Snowflake applicationId;
    @SerializedName("max_presences")
    private int maxPresences;
    @SerializedName("max_members")
    private int maxMembers;
    @SerializedName("vanity_url_code")
    private String vanityUrlCode;
    @SerializedName("banner")
    private String bannerHash;
    @SerializedName("premium_tier")
    private int nitroLevel;
    @SerializedName("premium_subscription_count")
    private int boosters;
    @SerializedName("preferred_locale")
    private String preferredLocale;
    @SerializedName("public_updates_channel_id")
    private Snowflake updatesChannelId;
    @SerializedName("max_video_channel_users")
    private int maxVideoUsers;
    @SerializedName("max_stage_video_channel_users")
    private int maxStageVideoUsers;
    // @SerializedName("welcome_screen")
    // private WelcomeScreen welcomeScreen;
    @SerializedName("nsfw_level")
    private int nsfwLevel;
    @SerializedName("premium_progress_bar_enabled")
    private boolean nitroProgressBarEnabled;
    @SerializedName("safety_alerts_channel_id")
    private Snowflake safetyAlertsChannelId;
    // @SerializedName("incidents_data")
    // private final List<IncidentData> incidents = new LinkedList<>();

    public boolean isUnavailable()
    {
        return unavailable;
    }

    public String getName()
    {
        return name;
    }

    public String getIconHash()
    {
        return iconHash;
    }

    public String getSplash()
    {
        return splash;
    }

    public String getDiscoverySplash()
    {
        return discoverySplash;
    }

    public List<GuildEmoji> getEmojis()
    {
        return emojis;
    }

    public List<String> getFeatures()
    {
        return features;
    }

    public int getApproxMemberCount()
    {
        return approxMemberCount;
    }

    public int getApproxPresenceCount()
    {
        return approxPresenceCount;
    }

    public String getDescription()
    {
        return description;
    }

    public List<Sticker> getStickers()
    {
        return stickers;
    }

    public Snowflake getOwnerId()
    {
        return ownerId;
    }

    public Snowflake getAfkChannelId()
    {
        return afkChannelId;
    }

    public int getAfkTimeout()
    {
        return afkTimeout;
    }

    public boolean isWidgetEnabled()
    {
        return widgetEnabled;
    }

    public Snowflake getWidgetChannelId()
    {
        return widgetChannelId;
    }

    public int getVerificationLevel()
    {
        return verificationLevel;
    }

    public int getDefaultMessageNotifications()
    {
        return defaultMessageNotifications;
    }

    public int getExplicitContentFilter()
    {
        return explicitContentFilter;
    }

    public int getMfaLevel()
    {
        return mfaLevel;
    }

    public Snowflake getApplicationId()
    {
        return applicationId;
    }

    public int getMaxPresences()
    {
        return maxPresences;
    }

    public int getMaxMembers()
    {
        return maxMembers;
    }

    public String getVanityUrlCode()
    {
        return vanityUrlCode;
    }

    public String getBannerHash()
    {
        return bannerHash;
    }

    public int getNitroLevel()
    {
        return nitroLevel;
    }

    public int getBoosters()
    {
        return boosters;
    }

    public String getPreferredLocale()
    {
        return preferredLocale;
    }

    public Snowflake getUpdatesChannelId()
    {
        return updatesChannelId;
    }

    public int getMaxVideoUsers()
    {
        return maxVideoUsers;
    }

    public int getMaxStageVideoUsers()
    {
        return maxStageVideoUsers;
    }

    public int getNsfwLevel()
    {
        return nsfwLevel;
    }

    public boolean isNitroProgressBarEnabled()
    {
        return nitroProgressBarEnabled;
    }

    public Snowflake getSafetyAlertsChannelId()
    {
        return safetyAlertsChannelId;
    }

    @Override
    public String toString()
    {
        return "Guild{" +
                "id=" + getSnowflake() +
                ", unavailable=" + unavailable +
                ", name='" + name + '\'' +
                ", iconHash='" + iconHash + '\'' +
                ", splash='" + splash + '\'' +
                ", discoverySplash='" + discoverySplash + '\'' +
                ", emojis=" + emojis +
                ", features=" + features +
                ", approxMemberCount=" + approxMemberCount +
                ", approxPresenceCount=" + approxPresenceCount +
                ", description='" + description + '\'' +
                ", stickers=" + stickers +
                ", ownerId=" + ownerId +
                ", afkChannelId=" + afkChannelId +
                ", afkTimeout=" + afkTimeout +
                ", widgetEnabled=" + widgetEnabled +
                ", widgetChannelId=" + widgetChannelId +
                ", verificationLevel=" + verificationLevel +
                ", defaultMessageNotifications=" + defaultMessageNotifications +
                ", explicitContentFilter=" + explicitContentFilter +
                ", mfaLevel=" + mfaLevel +
                ", applicationId=" + applicationId +
                ", maxPresences=" + maxPresences +
                ", maxMembers=" + maxMembers +
                ", vanityUrlCode='" + vanityUrlCode + '\'' +
                ", bannerHash='" + bannerHash + '\'' +
                ", nitroLevel=" + nitroLevel +
                ", boosters=" + boosters +
                ", preferredLocale='" + preferredLocale + '\'' +
                ", updatesChannelId=" + updatesChannelId +
                ", maxVideoUsers=" + maxVideoUsers +
                ", maxStageVideoUsers=" + maxStageVideoUsers +
                ", nsfwLevel=" + nsfwLevel +
                ", nitroProgressBarEnabled=" + nitroProgressBarEnabled +
                ", safetyAlertsChannelId=" + safetyAlertsChannelId +
                '}';
    }
}
