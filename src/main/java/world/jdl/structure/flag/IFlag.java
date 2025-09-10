package world.jdl.structure.flag;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * @author xgraza
 * @since 9/10/25
 */
public interface IFlag
{
    /**
     * Gets the bit / value of this flag
     * @return the value
     */
    int getValue();

    final class FlagDeserializer implements JsonDeserializer<IFlag>
    {
        @Override
        public IFlag deserialize(JsonElement jsonElement,
                                 Type type,
                                 JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException
        {
            if (!jsonElement.isJsonPrimitive())
            {
                throw new JsonParseException("must be JsonPrimitive");
            }
            System.out.println(type);
            for (final IFlag flag : ((Class<IFlag>) type).getEnumConstants())
            {
                if (flag.getValue() == jsonElement.getAsJsonPrimitive().getAsInt())
                {
                    return flag;
                }
            }
            return null;
        }
    }

    final class FlagSerializer implements JsonSerializer<IFlag>
    {
        @Override
        public JsonElement serialize(IFlag iFlag,
                                     Type type,
                                     JsonSerializationContext jsonSerializationContext)
        {
            return new JsonPrimitive(iFlag.getValue());
        }
    }
}
