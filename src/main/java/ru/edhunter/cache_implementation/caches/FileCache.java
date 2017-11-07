package ru.edhunter.cache_implementation.caches;


import ru.edhunter.cache_implementation.cache_utils.FileCacheReaderWriter;
import ru.edhunter.cache_implementation.interfaces.CacheInterface;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/**
 * Off-heap implementation of ICache interface. Uses HashMap for storing uuid and offset of stored cached items in file.
 * After VM shutdown, all cache-files deleted
 */
public class FileCache implements CacheInterface {
    final private HashMap<List, Long> cached = new HashMap<>();
    final private File file;
    final private boolean encrypt;
    final private String key;
    final private boolean zip;

    /**
     * Constructor
     * All cache files deletes on JVM terminates
     *
     * @param fileName name, with which cache stored on disk, sets file extension as .zip, if file compressed
     * @param zip      true if compressing enabled
     * @param encrypt  true if encrypting of cache enabled
     * @param cacheDir directory, where cache saved
     * @param key      encoding/decoding key
     */

    public FileCache(String fileName, boolean zip, boolean encrypt, String cacheDir, String key) {
        StringBuilder filePath = new StringBuilder(cacheDir + "\\" + fileName);
        if (zip) {
            filePath.append(".zip");
        }
        file = new File(filePath.toString());
        file.deleteOnExit();
        this.encrypt = encrypt;
        this.key = key;
        this.zip = zip;
    }

    /**
     * @see CacheInterface#putData(List, Object)
     */
    @Override
    public void putData(List identity, Object returnValue) {
        synchronized (FileCacheReaderWriter.class) {
            FileCacheReaderWriter fileCacheReaderWriter = new FileCacheReaderWriter();
            final long offset = fileCacheReaderWriter.write(returnValue, file, encrypt, key, zip);
            cached.put(identity, offset);
        }
    }

    /**
     * @see CacheInterface#getData(List)
     */
    @Override
    public Object getData(List identity) {
        synchronized (FileCacheReaderWriter.class) {
            FileCacheReaderWriter fileCacheReaderWriter = new FileCacheReaderWriter();
            return fileCacheReaderWriter.read(cached.get(identity), file, encrypt, key, zip);
        }
    }

    /**
     * @see CacheInterface#contains(List)
     */
    @Override
    public boolean contains(List identity) {
        return cached.containsKey(identity);
    }

    /**
     * @see CacheInterface#getSize()
     */
    @Override
    public int getSize() {
        return cached.size();
    }
}
