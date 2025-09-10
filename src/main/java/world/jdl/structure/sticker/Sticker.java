package world.jdl.structure.sticker;

import com.google.gson.annotations.SerializedName;
import world.jdl.structure.IdentifiableObject;
import world.jdl.structure.Snowflake;
import world.jdl.structure.user.User;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @author xgraza
 * @since 9/9/25
 */
public final class Sticker extends IdentifiableObject
{
    @SerializedName("pack_id")
    private Snowflake packId;
    private String name;
    private String description;
    @SerializedName("tags")
    private String tags;
    private final List<String> tagList = new LinkedList<>();
    private StickerType type;
    @SerializedName("format_type")
    private StickerFormatType formatType;
    private boolean available;
    @SerializedName("guild_id")
    private Snowflake guildId;
    private User user;
    @SerializedName("sort_value")
    private int sortValue;

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

    public List<String> getTags()
    {
        if (tags != null && !tags.isEmpty() && tagList.isEmpty())
        {
            Collections.addAll(tagList, tags.split(","));
        }
        return tagList;
    }

    public StickerType getType()
    {
        return type;
    }

    public StickerFormatType getFormatType()
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
