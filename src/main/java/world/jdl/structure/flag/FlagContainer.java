package world.jdl.structure.flag;

import com.google.gson.*;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

/**
 * @author xgraza
 * @since 9/10/25
 * @param <T> the {@link IFlag} type
 */
public final class FlagContainer<T extends IBitFlag>
{
    private final Class<T> type;

    private Set<T> flags;
    private int value;

    public FlagContainer(final Class<T> type)
    {
        this.type = type;
    }

    void setValue(final int value)
    {
        this.value = value;
        flags = IBitFlag.flagsFrom(type, value);
    }

    public int getValue()
    {
        return value;
    }

    public Set<T> getFlags()
    {
        return flags;
    }

    public static final class FlagContainerSerializer implements JsonSerializer<FlagContainer<?>>
    {
        @Override
        public JsonElement serialize(FlagContainer<?> flagContainer,
                                     Type type,
                                     JsonSerializationContext jsonSerializationContext)
        {
            return new JsonPrimitive(flagContainer.getValue());
        }
    }

    public static final class FlagContainerDeserializer implements JsonDeserializer<FlagContainer<?>>
    {
        @SuppressWarnings("unchecked")
        @Override
        public FlagContainer<?> deserialize(JsonElement jsonElement,
                                            Type type,
                                            JsonDeserializationContext jsonDeserializationContext)
                throws JsonParseException
        {
            final FlagContainer<IBitFlag> container = new FlagContainer<>(
                    (Class<IBitFlag>) ((ParameterizedType)type).getActualTypeArguments()[0]);
            container.setValue(jsonElement.getAsJsonPrimitive().getAsInt());
            return container;
        }
    }

}
