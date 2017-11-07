package ru.edhunter.cache_implementation;

import ru.edhunter.cache_implementation.caches.FileCache;
import ru.edhunter.cache_implementation.caches.MemoryCache;
import ru.edhunter.cache_implementation.interfaces.CacheInterface;
import ru.edhunter.cache_implementation.interfaces.CachedService;
import ru.edhunter.cache_implementation.interfaces.annotation.Cache;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static ru.edhunter.cache_implementation.interfaces.annotation.Cache.CacheType.FILE;

/**
 * Class-handler, that provides caching functionality.
 */
class CacheHandler implements InvocationHandler {
    /**
     * Map, where caches kept
     */
    final private ConcurrentMap<String, CacheInterface> cacheMap = new ConcurrentHashMap<>();

    /**
     * field storing key for encoding purposes
     */
    final private String key;
    /**
     * path to cache work directory
     */
    final private String dir;

    /***
     *Field with instance of service, methods of which we cache
     */
    final private CachedService service;

    /**
     * @param service instance of service that needs caching for its methods.
     * @param key     key for decoding/encoding
     * @param dir     directory for storing cache
     */
    CacheHandler(CachedService service, String key, String dir) {
        this.service = service;
        this.key = key;
        this.dir = dir;
    }

    /**
     * Method detects @Cache annotations and invokes method without caching if didn't find it, or adds caching
     * function if finds.
     * * Invokes method with cache functionality, uses options set in @Cache annotation.
     *
     * @param method method of service instance.
     * @param args   method parameters.
     * @param proxy  classloader of proxy object
     * @return returns base method result.
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Cache cacheAnnotation = method.getAnnotation(Cache.class);

        if (cacheAnnotation == null) {
            return method.invoke(service, args);

        } else {

            final List<Object> identity = createIdentity(cacheAnnotation.identityBy(), method.getReturnType(), args);
            final CacheInterface cache = acquireCache(cacheAnnotation, method);
            final Object returnValue;

            if (cache.contains(identity)) {
                returnValue = cache.getData(identity);
                System.out.println("Reading from cache");
            } else {
                returnValue = method.invoke(service, args);
                if (cacheAnnotation.listList() > 0 && returnValue instanceof List) {
                    System.out.println("Added to cache");
                    cache.putData(identity, listValue(returnValue, cacheAnnotation.listList()));
                } else {
                    System.out.println("Added to cache");
                    cache.putData(identity, returnValue);
                }
            }
            return returnValue;
        }
    }

    /**
     * Method returns list, based by size limit, limit stored in 'listList' value in @Cache annotation
     *
     * @param returnValue value that checked
     * @param maxSize     sizeLimit
     */
    private List listValue(Object returnValue, int maxSize) {
        if (((List) returnValue).size() > maxSize) {
            return cutTheList(returnValue, maxSize);
        } else {
            return (List) returnValue;
        }
    }

    /**
     * If method returns list, which size equals is limited by 'listList' vakue in annotation 'Cache'
     */

    private List<Object> cutTheList(Object returnValue, int size) {
        if (returnValue instanceof ArrayList) {
            return new ArrayList<Object>(((ArrayList) returnValue).subList(0, size));
        } else {
            return new LinkedList<Object>(((LinkedList) returnValue).subList(0, size));
        }
    }

    /**
     * Method checks stored in cacheMap caches, with its name. Name of cache based on annotation option "fileNamePrefix",
     * default value of cache is name of method, that uses that cache. If there id no such cache stored, method creates
     * new cache with that name.
     *
     * @param method          method that used cache.
     * @param cacheAnnotation annotation with options for cache
     */
    private CacheInterface acquireCache(Cache cacheAnnotation, Method method) {
        String cacheName = cacheAnnotation.fileNamePrefix().equals("") ? method.getName() : cacheAnnotation.fileNamePrefix();

        if (!cacheMap.containsKey(cacheName)) {
            cacheMap.put(cacheName, createNewCache(cacheName,
                    cacheAnnotation.cacheType(),
                    cacheAnnotation.zip(),
                    cacheAnnotation.encrypt()));
        }

        return cacheMap.get(cacheName);
    }

    /**
     * Method creates unique identity of cached value, based on @Cache annotation option "identityBy".
     *
     * @param classes    array of parameter types, which used for identification of unique method cached result
     * @param returnType Object type of returnValue of cached method;
     * @param args       array of method parameters.  @return returns object if uuid, that is unique id of cached value
     */
    private List<Object> createIdentity(Class<?>[] classes, Class<?> returnType, Object[] args) {
        List<Object> identity = new ArrayList<>();

        identity.add(returnType);

        if (classes.length != 0) {
            for (Class<?> clazz : classes) {
                for (Object arg : args) {
                    Class<?> argClass = arg.getClass();
                    if (clazz.isPrimitive()) {
                        argClass = PrimitiveMap.getPrimitive(argClass.getSimpleName());
                    }
                    if (argClass == clazz) {
                        identity.add(arg);
                        break;
                    }
                }
            }
        } else {
            identity.addAll(Arrays.asList(args));
        }

        return identity;
    }

    /**
     * Method, that creates new cache, based on @Cache annotation values
     *
     * @param cacheName name of newly created cache
     * @param cacheType type of cache(File-based or Memory)
     * @param zip       true if cache needs to be compressed
     * @param encrypt   true if cache needs to bve encrypted
     * @return return ne Cache item
     */

    private CacheInterface createNewCache(String cacheName,
                                          Cache.CacheType cacheType,
                                          boolean zip,
                                          boolean encrypt) {

        return cacheType == FILE ? new FileCache(cacheName, zip, encrypt, dir, key) : new MemoryCache();
    }

    /**
     * Static hard-coded HashMap with "primitive name - primitive type" values.
     * Used by reflection-based method in CacheManager class
     *
     * @see CacheHandler#createIdentity(Class[], Class, Object[])
     */
    private static class PrimitiveMap {
        private static final Map<String, Class> PRIMITIVES = new HashMap<>();

        static {
            PRIMITIVES.put("Integer", Integer.TYPE);
            PRIMITIVES.put("Long", Long.TYPE);
            PRIMITIVES.put("Double", Double.TYPE);
            PRIMITIVES.put("Float", Float.TYPE);
            PRIMITIVES.put("Char", Character.TYPE);
            PRIMITIVES.put("Boolean", Boolean.TYPE);
            PRIMITIVES.put("Byte", Byte.TYPE);
            PRIMITIVES.put("Short", Short.TYPE);
        }

        private static Class getPrimitive(String className) {
            return PRIMITIVES.get(className);
        }
    }
}
