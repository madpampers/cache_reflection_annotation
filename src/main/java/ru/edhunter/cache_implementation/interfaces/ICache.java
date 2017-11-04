package ru.edhunter.cache_implementation.interfaces;

import java.util.UUID;

/**
 * Basic cache interface, with methods for manipulating with it. Current implementations:
 *
 * @see ru.edhunter.cache_implementation.caches.FileCache
 * @see ru.edhunter.cache_implementation.caches.MemoryCache
 */
public interface ICache {

    /**
     * Puts data on storage.
     *
     * @param identity    unique identification of caching value.
     * @param returnValue value, that we cache.
     */
    void putData(final UUID identity, final Object returnValue);

    /**
     * Gets data from cache by its identity.
     *
     * @return returns object from cache.
     */
    Object getData(final UUID identity);

    /**
     * Checks if value with provided identity present in cache.
     *
     * @return true if cache contains identity
     */
    boolean contains(final UUID identity);

    /**
     * @return returns count of elements in cache
     */
    int getSize();
}
