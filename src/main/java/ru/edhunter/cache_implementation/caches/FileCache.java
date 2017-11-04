package ru.edhunter.cache_implementation.caches;


import ru.edhunter.cache_implementation.cacheUtils.CacheManager;
import ru.edhunter.cache_implementation.cacheUtils.FileCacheReader;
import ru.edhunter.cache_implementation.cacheUtils.FileCacheWriter;
import ru.edhunter.cache_implementation.interfaces.ICache;

import java.io.*;
import java.util.HashMap;
import java.util.UUID;

/**
 * Off-heap implementation of ICache interface. Uses HashMap for storing uuid and offset of stored cached items in file.
 */
public class FileCache implements ICache {
    private HashMap<UUID, Long> cached = new HashMap<>();
    private File file;

    /**
     * Contructor
     * All cache files deletes on JVM terminates
     *
     * @param fileName name, with which cache stored on disk, sets file extension as .zip, if file compressed
     */

    public FileCache(String fileName) {
        StringBuilder filePath = new StringBuilder(CacheManager.getDir() + "\\" + fileName);
        if (CacheManager.isZip()) {
            filePath.append(".zip");
        }
        file = new File(filePath.toString());
        file.deleteOnExit();
    }

    /**
     * @see ICache#putData(UUID, Object)
     */
    @Override
    public void putData(final UUID identity, Object returnValue) {
        long offset;
        offset = FileCacheWriter.write(returnValue, file);
        cached.put(identity, offset);
    }

    /**
     * @see ICache#getData(UUID)
     */
    @Override
    public Object getData(final UUID identity) {
        return FileCacheReader.read(cached.get(identity), file);
    }

    /**
     * @see ICache#contains(UUID)
     */
    @Override
    public boolean contains(final UUID identity) {
        return cached.containsKey(identity);
    }

    /**
     * @see ICache#getSize()
     */
    @Override
    public int getSize() {
        return cached.size();
    }
}
