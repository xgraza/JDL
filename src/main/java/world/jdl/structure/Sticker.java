package world.jdl.structure;

import com.google.gson.annotations.SerializedName;
import world.jdl.structure.user.User;

import java.util.LinkedList;
import java.util.List;

public final class Sticker
{
    private Snowflake id;
    @SerializedName("pack_id")
    private Snowflake packId;
    private String name;
    private String description;
    //private final List<String> tags = new LinkedList<>();
    private int type;
    @SerializedName("format_type")
    private int formatType;
    private boolean available;
    @SerializedName("guild_id")
    private Snowflake guildId;
    private User user;
    @SerializedName("sort_value")
    private int sortValue;

    public Snowflake getId()
    {
        return id;
    }

    public Snowflake getPackId()
    {
        return packId;
    }

    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

//    public List<String> getTags()
//    {
//        return tags;
//    }

    public int getType()
    {
        return type;
    }

    public int getFormatType()
    {
        return formatType;
    }

    public boolean isAvailable()
    {
        return available;
    }

    public Snowflake getGuildId()
    {
        return guildId;
    }

    public User getUser()
    {
        return user;
    }

    public int getSortValue()
    {
        return sortValue;
    }
}
