package world.jdl.gateway.packet;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class Payload
{
    private int op;
    @SerializedName("t")
    private String eventName;
    @SerializedName("s")
    private Integer sequence;
    @SerializedName("d")
    private JsonElement data;

    public int getOp()
    {
        return op;
    }

    public String getEventName()
    {
        return eventName;
    }

    public Integer getSequence()
    {
        return sequence;
    }

    public JsonElement getData()
    {
        return data;
    }
}
