package ru.edhunter.cache_implementation;

import ru.edhunter.cache_implementation.cacheUtils.CacheManager;
import ru.edhunter.cache_implementation.interfaces.IService;
import ru.edhunter.cache_implementation.interfaces.annotation.Cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * CacheProxy class provides caching for IServices methods, by putting annotation "@Cache" before method, that needs caching.
 */
public class CacheProxy {

    /**
     * Constructors can assign default directory, where cache will be, and key for cache encoding,
     * default constructor will use default values "target\generated-sources" as directory and "key" as key for
     * encoding.
     *
     * @param dir path to dir for storage
     * @param key key for encoding/decoding purposes
     */
    public CacheProxy(final String dir, final String key) {
        this(dir);
        CacheManager.setCipherKey(key);
    }

    /**
     * Coustructor with default key for encode cache, and custom dir path
     *
     * @param dir path to dir for storage
     */

    public CacheProxy(final String dir) {
        CacheManager.setCacheDir(dir);
    }

    /**
     * Coustructor with default key for encode cache, and default dir path
     */

    public CacheProxy() {
    }

    /**
     * Method creates proxy-object.
     *
     * @param service instance of service with catchable methods.
     */
    public IService cache(final IService service) {
        return (IService) Proxy.newProxyInstance(IService.class.getClassLoader(),
                service.getClass().getInterfaces(),
                new CacheHandler(service));
    }

    /**
     * Class-handler, that provides caching functionality.
     */
    private static class CacheHandler implements InvocationHandler {

        /***
         *Field with instance of service, methods of which we cache
         */
        private final IService service;

        /**
         * @param service instance of service that needs caching for its methods.
         */
        CacheHandler(final IService service) {
            this.service = service;
        }

        /**
         * Method detects @Cache annotations and invokes method without caching if didn't find it, or adds caching
         * function if finds.
         *
         * @return returns base method result.
         */
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            Cache cacheAnnotation = method.getAnnotation(Cache.class);

            if (cacheAnnotation == null) {
                return method.invoke(service, args);

            } else {
                return CacheManager.invoke(service, args, method, cacheAnnotation);
            }
        }
    }
}
