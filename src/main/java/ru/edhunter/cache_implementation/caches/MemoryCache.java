package ru.edhunter.cache_implementation.caches;

import ru.edhunter.cache_implementation.interfaces.ICache;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * RAM implementation of ICache interface. Uses HashMap for storing uuid and cached value.
 */
public class MemoryCache implements ICache {
    private Map<UUID, Object> cache = new HashMap<>();

    /**@see ICache#putData(UUID, Object) */
    @Override
    public void putData(final UUID identity, final Object returnValue) {
        cache.put(identity, returnValue);
    }
    /**@see ICache#getData(UUID) */
    @Override
    public Object getData(final UUID identity) {
        return cache.get(identity);
    }

    /**@see ICache#contains(UUID) */
    @Override
    public boolean contains(final UUID identity) {
        return cache.containsKey(identity);
    }

    /**@see ICache#getSize() */
    @Override
    public int getSize() {
        return cache.size();
    }
}