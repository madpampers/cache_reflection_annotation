package ru.edhunter.cache_implementation.interfaces;

import java.util.List;

/**
 * Basic cache interface, with methods for manipulating with it. Current implementations:
 *
 * @see ru.edhunter.cache_implementation.caches.FileCache
 * @see ru.edhunter.cache_implementation.caches.MemoryCache
 */
public interface CacheInterface {

    /**
     * Puts data on storage.
     *
     * @param identity    unique identity of caching value.
     * @param returnValue value, that we cache.
     */
    void putData(final List identity, final Object returnValue);

    /**
     * Gets data from cache by its identity.
     *
     * @param identity unique identity of stored cache value
     * @return returns object from cache.
     */
    Object getData(final List identity);

    /**
     * Checks if value with provided identity present in cache.
     *
     * @param identity unique identity of stored cache value
     * @return true if cache contains identity
     */
    boolean contains(final List identity);

    /**
     * @return returns count of elements in cache
     */
    int getSize();
}
