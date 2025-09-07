package world.jdl.util;

import java.lang.reflect.InvocationTargetException;

/**
 * @author xgraza
 * @since 9/7/25
 */
public final class ReflectionUtil
{
    @SuppressWarnings("unchecked")
    public static <T> T createInstance(final Class<T> clazz)
    {
        try
        {
            return (T) clazz.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }
}
