package ru.edhunter.cache_implementation.cacheUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Static hard-coded HashMap with "primitive name - primitive type" values.
 * Used by reflection-based method in CacheManager class
 *
 * @see CacheManager#createIdentity(Class[], Object[])
 */
public class PrimitiveMap {
    private static final Map<String, Class> PRIMITIVES = new HashMap<>();

    static {
        PRIMITIVES.put("Integer", Integer.TYPE);
        PRIMITIVES.put("Long", Long.TYPE);
        PRIMITIVES.put("Double", Double.TYPE);
        PRIMITIVES.put("Float", Float.TYPE);
        PRIMITIVES.put("Boolean", Boolean.TYPE);
        PRIMITIVES.put("Char", Character.TYPE);
        PRIMITIVES.put("Byte", Byte.TYPE);
        PRIMITIVES.put("Short", Short.TYPE);
    }

    static Class getPrimitive(String className) {
        return PRIMITIVES.get(className);
    }
}
