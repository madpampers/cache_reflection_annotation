package ru.edhunter.cache_implementation.cacheUtils;

import java.io.*;

/**
 * Class that reads cached data from disk
 */

public class FileCacheReader {

    /**
     * Writes services method returnValue to file on disk.
     *
     * @param offset offset, from where value stored in cache file.
     * @param file   file, from which we get stored value.
     * @return returns value stored in cache file.
     */
    public static Object read(long offset, File file) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {

            fileInputStream.getChannel().position(offset);
            byte[] input;

            if (CacheManager.isZip()) {
                input = Zipper.decompress(fileInputStream);
            } else {
                input = new byte[fileInputStream.available()];
                fileInputStream.read(input);
            }

            if (CacheManager.isEncryptCache()) input = BasicCipher.decode(input);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            return objectInputStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}