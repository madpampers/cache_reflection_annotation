package ru.edhunter.cache_implementation.interfaces;

import ru.edhunter.cache_implementation.interfaces.annotation.Cache;

import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;

import static ru.edhunter.cache_implementation.interfaces.annotation.Cache.CacheType.FILE;
import static ru.edhunter.cache_implementation.interfaces.annotation.Cache.CacheType.IN_MEMORY;

/**
 * Test interface with methods cache-enabled
 */

public interface CachedService {
    @Cache(cacheType = FILE, identityBy = {String.class}, fileNamePrefix = "dasaaa", listList = 10000, encrypt = true, zip = true)
    List<String> run(final String item, double value, Date date);

    @Cache(cacheType = FILE, identityBy = {String.class, Integer.class})
    String work(String item, int value);
}