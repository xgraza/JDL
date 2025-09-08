package world.jdl.structure;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class Snowflake
{
    private static final long DISCORD_EPOCH = 1420070400000L;

    private final long id;

    public Snowflake(final long id)
    {
        this.id = id;
    }

    public long getId()
    {
        return id;
    }

    public long getTimestamp()
    {
        return (id >> 22) + DISCORD_EPOCH;
    }

    @Override
    public String toString()
    {
        return "Snowflake{" +
                "id=" + id +
                '}';
    }

    public static Snowflake fromTimestamp(final long timestampMS)
    {
        return new Snowflake((timestampMS - DISCORD_EPOCH) << 22);
    }

    public static final class SnowflakeDeserializer implements JsonDeserializer<Snowflake>
    {
        @Override
        public Snowflake deserialize(JsonElement element,
                                Type type,
                                JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException
        {
            return new Snowflake(element.getAsJsonPrimitive().getAsLong());
        }
    }

    public static final class SnowflakeSerializer implements JsonSerializer<Snowflake>
    {
        @Override
        public JsonElement serialize(Snowflake snowflake,
                                     Type type,
                                     JsonSerializationContext jsonSerializationContext)
        {
            return new JsonPrimitive(snowflake.getId());
        }
    }
}
