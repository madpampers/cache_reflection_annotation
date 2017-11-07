package ru.edhunter.cache_implementation.cache_utils;

import java.io.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Class that reads cached data from disk
 */

public class FileCacheReaderWriter {

    /**
     * Writes services method returnValue to file on disk.
     *
     * @param offset  offset, from where value stored in cache file.
     * @param file    file, from which we get stored value.
     * @param encrypt this parameter equals true if encrypting if cache enables.
     * @param key     key for encrypting cache
     * @param zip     @return returns value stored in cache file.
     */
    public Object read(long offset, File file, boolean encrypt, String key, boolean zip) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.getChannel().position(offset);
            byte[] input;

            if (zip) {
                input = Zipper.decompress(fileInputStream);
            } else {
                input = new byte[fileInputStream.available()];
                fileInputStream.read(input);
            }

            if (encrypt) {
                input = BasicCipher.decode(input, key);
            }

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes services method returnValue to file on disk.
     *
     * @param returnValue value that we want to keep.
     * @param file        file, where we want to store value
     * @param encrypt     this parameter equals true if encrypting if cache enables.
     * @param key         key for encrypting cache
     * @param zip         @return returns value stored in cache file.
     * @return returns offset of cached item
     */
    public long write(Object returnValue, File file, boolean encrypt, String key, boolean zip) {
        final long offset;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
             FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {

            objectOutputStream.writeObject(returnValue);
            objectOutputStream.flush();

            byte[] output = byteArrayOutputStream.toByteArray();
            offset = fileOutputStream.getChannel().position();

            if (encrypt) {
                output = BasicCipher.encode(output, key);
            }

            if (zip) {
                Zipper.compress(fileOutputStream, output);
            } else {
                fileOutputStream.write(output);
            }

        } catch (NotSerializableException e) {
            throw new RuntimeException("results of cached method must be serializable (" + e.getMessage() + "), " +
                    "use cache 'IN_MEMORY'", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return offset;
    }
}