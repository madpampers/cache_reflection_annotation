package ru.edhunter.cache_implementation.cacheUtils;


import ru.edhunter.cache_implementation.caches.FileCache;
import ru.edhunter.cache_implementation.caches.MemoryCache;
import ru.edhunter.cache_implementation.interfaces.ICache;
import ru.edhunter.cache_implementation.interfaces.IService;
import ru.edhunter.cache_implementation.interfaces.annotation.Cache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static ru.edhunter.cache_implementation.interfaces.annotation.Cache.CacheType.FILE;

/**
 * Main class, that manages caches.
 * Fie
 */

public class CacheManager {

    /**
     * Map, where caches kept
     */
    private static Map<String, ICache> cacheMap = new HashMap<>();

    /**
     * Fields for storing cache options with default values.
     */
    private static String cacheDir = "target\\generated-sources";
    private static String key = "key";
    private static boolean encryptCache;
    private static boolean zip;

    /**
     * Setter for encryption key.
     *
     * @param cipherKey String, used as key for coding and decoding cache.
     */
    public static void setCipherKey(String cipherKey) {
        CacheManager.key = cipherKey;
    }

    /**
     * Setter for the directory, where cache will be stored.
     *
     * @param dir path to dir.
     */
    public static void setCacheDir(final String dir) {
        cacheDir = dir;
    }

    /**
     * Getter for path to directory, where cache stored.
     *
     * @return String with path to directory
     */
    public static String getDir() {
        return cacheDir;
    }

    /**
     * Getter for compressed of not type of cache.
     *
     * @return boolean, which value true if cache compressed.
     */
    public static boolean isZip() {
        return zip;
    }

    /**
     * Invokes method with cache functionality, uses options set in @Cache annotation.
     *
     * @param service         instance of service for which methods adds caching.
     * @param method          method of service instance.
     * @param cacheAnnotation annotation with options for caching method.
     * @param args            method parameters.
     */
    public static Object invoke(final IService service,
                                final Object[] args,
                                final Method method,
                                final Cache cacheAnnotation) throws InvocationTargetException, IllegalAccessException {

        encryptCache = cacheAnnotation.encrypt();
        zip = cacheAnnotation.zip();

        final UUID identity = createIdentity(cacheAnnotation.identityBy(), args);
        final ICache cache = acquireCache(cacheAnnotation, method);
        final Object returnValue;

        if (cache.contains(identity)) {
            returnValue = cache.getData(identity);
        } else {
            returnValue = method.invoke(service, args);
            if (method.getReturnType() == List.class) {
                if (cache.getSize() < cacheAnnotation.listList() || cacheAnnotation.listList() <= 0) {
                    cache.putData(identity, returnValue);
                }
            } else {
                cache.putData(identity, returnValue);
            }
        }
        return returnValue;
    }

    /**
     * Method creates new cache, based on annotation @Cache.
     *
     * @param cacheType annotation for method.
     * @param cacheName name of file/key with which cache will be stored
     */
    private static ICache createNewCache(final Cache.CacheType cacheType, final String cacheName) {
        final ICache newCache;

        if (cacheType == FILE) {
            newCache = new FileCache(cacheName);
        } else {
            newCache = new MemoryCache();
        }

        cacheMap.put(cacheName, newCache);

        return newCache;

    }

    /**
     * Method creates unique identity of cached value, based on @Cache annotation option "identityBy".
     *
     * @param args    array of method parameters.
     * @param classes array of parameter types, which used for identification of unique method cached result
     * @return returns object if uuid, that is unique id of cached value
     */
    private static UUID createIdentity(Class<?>[] classes, Object[] args) {
        StringBuilder stringBuilder = new StringBuilder();

        if (classes.length != 0) {
            for (Class<?> clazz : classes) {
                for (Object arg : args) {
                    Class<?> argClass = arg.getClass();
                    if (clazz.isPrimitive()) {
                        argClass = PrimitiveMap.getPrimitive(argClass.getSimpleName());
                    }
                    if (argClass == clazz) {
                        stringBuilder.append(arg.toString()).append(arg.hashCode());
                        break;
                    }
                }
            }
        } else {
            for (Object arg : args) {
                stringBuilder.append(arg.toString()).append(arg.hashCode());
            }
        }

        return UUID.nameUUIDFromBytes(stringBuilder.toString().getBytes());
    }

    /**
     * Method checks stored in cacheMap caches, with its name. Name of cache based on annotation option "fileNamePrefix",
     * default value of cache is name of method, that uses that cache. If there id no such cache stored, method creates
     * new cache with that name.
     *
     * @param method          method that used cache.
     * @param cacheAnnotation annotation with options for cache
     */
    private static ICache acquireCache(Cache cacheAnnotation, Method method) {
        String cacheName = cacheAnnotation.fileNamePrefix().equals("") ? method.getName() : cacheAnnotation.fileNamePrefix();

        if (!cacheMap.containsKey(cacheName)) {
            cacheMap.put(cacheName, createNewCache(cacheAnnotation.cacheType(), cacheName));
        }

        return cacheMap.get(cacheName);
    }

    /**
     * returns cryptographic key
     *
     * @return String, used as key
     */
    static String getKey() {
        return key;
    }

    /**
     * @return returns true, if cache encrypted
     */
    static boolean isEncryptCache() {
        return encryptCache;
    }
}