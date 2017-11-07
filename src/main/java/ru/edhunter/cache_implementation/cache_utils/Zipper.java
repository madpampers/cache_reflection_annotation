package ru.edhunter.cache_implementation.cache_utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Class that provides compression and decompression of cache
 */
class Zipper {

    /**
     * Method compresses output.
     *
     * @param fileOutputStream output stream to file with cache
     * @param output           byte-array with cache data
     */
    static void compress(FileOutputStream fileOutputStream, byte[] output) {
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream)) {

            gzipOutputStream.write(output);
            gzipOutputStream.finish();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method decompresses input file
     *
     * @param fileInputStream inputstream to file with cache
     * @return returns byte-array with decompressed cache data
     */
    static byte[] decompress(FileInputStream fileInputStream) {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(fileInputStream);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] tmp = new byte[256];
            int n;

            while ((n = gzipInputStream.read(tmp)) >= 0)
                byteArrayOutputStream.write(tmp, 0, n);
            gzipInputStream.close();

            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
