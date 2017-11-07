package ru.edhunter.cache_implementation;

import ru.edhunter.cache_implementation.interfaces.CachedService;

import java.lang.reflect.Proxy;

/**
 * CacheProxy class provides caching for IServices methods, by putting annotation "@Cache" before method, that needs caching.
 */
public class CacheProxy {
    final private String dir;
    final private String key;

    /**
     * Constructors can assign default directory, where cache will be, and key for cache encoding,
     * default constructor will use default values "target\generated-sources" as directory and "key" as key for
     * encoding.
     *
     * @param dir path to dir for storage
     * @param key key for encoding/decoding purposes
     */
    public CacheProxy(final String dir, final String key) {
        this.dir = dir;
        this.key = key;
    }


    /**
     * Coustructor with default key for encode cache, and custom dir path
     *
     * @param dir path to dir for storage
     */
    public CacheProxy(final String dir) {
        this.dir = dir;
        this.key = "key";
    }

    /**
     * Coustructor with default key for encode cache, and default dir path
     */
    public CacheProxy() {
        this.dir = "target\\generated-sources";
        this.key = "key";
    }

    /**
     * Method creates proxy-object.
     *
     * @param service instance of service with catchable methods.
     */
    public CachedService cache(final CachedService service) {
        CacheHandler cacheHandler = new CacheHandler(service, key, dir);

        return (CachedService) Proxy.newProxyInstance(CachedService.class.getClassLoader(),
                service.getClass().getInterfaces(),
                cacheHandler);
    }
}
