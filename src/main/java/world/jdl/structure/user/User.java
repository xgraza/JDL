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
}
