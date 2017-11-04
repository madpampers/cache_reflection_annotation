package ru.edhunter.cache_implementation.interfaces;

import ru.edhunter.cache_implementation.interfaces.annotation.Cache;

import java.util.Date;
import java.util.List;

import static ru.edhunter.cache_implementation.interfaces.annotation.Cache.CacheType.FILE;
import static ru.edhunter.cache_implementation.interfaces.annotation.Cache.CacheType.IN_MEMORY;

/**
 * Test interface with methods with cache
 */
public interface IService {
    @Cache(cacheType = FILE, fileNamePrefix = "data", zip, identityBy = {String.class}, listList = 5)
    List run(final String item, double value, Date date);

    @Cache(cacheType = IN_MEMORY, listList = 5, zip = true, identityBy = {String.class}, fileNamePrefix = "asdaa", encrypt = true)
    List work(String item, double value, Date date);
}