package world.jdl.structure;

import com.google.gson.annotations.SerializedName;

/**
 * @author xgraza
 * @since 9/10/25
 */
public class IdentifiableObject
{
    @SerializedName("id")
    private Snowflake snowflake;

    public Snowflake getSnowflake()
    {
        return snowflake;
    }

    public long getID()
    {
        return snowflake.getId();
    }

    public String getIDString()
    {
        return String.valueOf(getID());
    }

    public long getTimestampCreated()
    {
        return snowflake.getTimestamp();
    }
}
