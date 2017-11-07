package ru.edhunter.cache_implementation.caches;

import ru.edhunter.cache_implementation.interfaces.CacheInterface;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * RAM implementation of ICache interface. Uses HashMap for storing uuid and cached value.
 */
public class MemoryCache implements CacheInterface {
    private ConcurrentMap<List, Object> cache = new ConcurrentHashMap<>();

    /**
     * @see CacheInterface#putData(List, Object)
     */
    @Override
    public void putData(List identity, final Object returnValue) {
        cache.put(identity, returnValue);
    }

    /**
     * @see CacheInterface#getData(List)
     */
    @Override
    public Object getData(List identity) {
        return cache.get(identity);
    }

    /**
     * @see CacheInterface#contains(List)
     */
    @Override
    public boolean contains(List identity) {
        return cache.containsKey(identity);
    }

    /**
     * @see CacheInterface#getSize()
     */
    @Override
    public int getSize() {
        return cache.size();
    }
}