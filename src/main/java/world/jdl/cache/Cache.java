package world.jdl.cache;

import world.jdl.rest.RESTClient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A generic cache instance
 * @param <K> the identifier for each object in the cache
 * @param <V> the cached object
 */
public abstract class Cache<K, V>
{
    private final Map<K, V> cacheMap = new ConcurrentHashMap<>();
    private final List<V> cacheList = new CopyOnWriteArrayList<>();

    protected final RESTClient restClient;

    public Cache(final RESTClient restClient)
    {
        this.restClient = restClient;
    }

    /**
     * Fetches an object for caching
     * @param key the key for the uncased object
     * @return the
     */
    public abstract V fetch(final K key);

    /**
     * Places an object into this {@link Cache}
     * @param key the identifier
     * @param value the object to cache
     * @return {@link V} the cached object
     */
    public V cache(final K key, final V value)
    {
        final V cachedValue = cacheMap.put(key, value);
        if (!Objects.equals(cachedValue, value))
        {
            cacheList.remove(cachedValue);
            cacheList.add(value);
        }
        return value;
    }

    /**
     * Forgets and destroys a cached object
     * @param key the identifier
     * @return {@link V} or null
     */
    public V forget(final K key)
    {
        final V value = cacheMap.remove(key);
        if (value != null)
        {
            cacheList.remove(value);
        }
        return value;
    }

    /**
     * Gets a cached object reference from a identifier
     * @param key the identifier
     * @return {@link V} or null
     */
    public V get(final K key)
    {
        return cacheMap.get(key);
    }

    /**
     * Retrieves a cached object from the cache or fetches the object if unavailable
     * @param key the identifier
     * @return {@link V} or null
     */
    public V getOrFetch(final K key)
    {
        return cacheMap.getOrDefault(key, fetch(key));
    }

    /**
     * Gets the list of cached objects
     * @return {@link java.util.LinkedList} of {@link V}
     */
    public List<V> list()
    {
        return cacheList;
    }

    /**
     * Retries the size of this cache
     * @return the size of this cache
     */
    public int size()
    {
        return cacheMap.size();
    }
}
