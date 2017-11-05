package ru.edhunter.cache_implementation.cacheUtils;

import java.io.*;

/**
 * Class that writes cached data on disk
 */

public class FileCacheWriter {

    /**
     * Writes services method returnValue to file on disk.
     *
     * @param returnValue value that we want to keep.
     * @param file        file, where we want to store value
     * @return returns offset, where value stored in file
     */
    public static long write(Object returnValue, File file) {
        long offset;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {

            objectOutputStream.writeObject(returnValue);
            objectOutputStream.flush();

            byte[] output = byteArrayOutputStream.toByteArray();
            offset = fileOutputStream.getChannel().position();

            if (CacheManager.isEncryptCache()) output = BasicCipher.encode(output);

            if (CacheManager.isZip()) {
                Zipper.compress(fileOutputStream, output);
            } else {
                fileOutputStream.write(output);
            }

        } catch (NotSerializableException e) {
            throw new RuntimeException("results of cached method must be serializable ("+e.getMessage()+"), " +
                    "use cache 'IN_MEMORY'", e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return offset;
    }
}
