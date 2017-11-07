package ru.edhunter.cache_implementation.interfaces.annotation;

import com.sun.org.glassfish.gmbal.InheritedAttributes;
import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;
import javafx.beans.DefaultProperty;
import ru.edhunter.cache_implementation.interfaces.CachedService;

import javax.annotation.processing.SupportedOptions;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Runtime annotation that provides settings for cache
 */

@Retention(RUNTIME)
@Target(METHOD)
@Inherited

public @interface Cache {

    /**
     * Property which sets type of Cache: IN_MEMORY - cache storage in heap,
     * FILE - cache off-heap cache storage
     */
    CacheType cacheType() default CacheType.IN_MEMORY;

    /**
     * Sets filename/key for cache
     */
    String fileNamePrefix() default "";

    /**
     * Describes types of method parameters, which determine identity of objects, that are stored in cache
     */
    Class<?>[] identityBy() default {};

    /**
     * Setting for encrypt or not cache
     */
    boolean encrypt() default false;

    /**
     * Setting for compress cache or not
     */
    boolean zip() default false;

    /**
     * Setting for setting maximum of stored items, if type of item is List
     */
    int listList() default -1;

    /**
     * Enum of supported cache types
     */
    enum CacheType {
        FILE,
        IN_MEMORY
    }
}
