package ru.edhunter.cache_implementation.cache_utils;

/**
 * Class BasicCipher provides XOR encryption/decryption of cache.
 */
class BasicCipher {

    /**
     * Encodes byte array with cached data.
     *
     * @param toEncode byte array with data.
     * @param key      key for encoding.
     */
    static byte[] encode(byte[] toEncode, String key) {
        byte[] keyArray = key.getBytes();
        byte[] result = new byte[toEncode.length];

        for (int i = 0; i < toEncode.length; i++) {
            result[i] = (byte) (toEncode[i] ^ keyArray[i % keyArray.length]);
        }

        return result;
    }

    /**
     * Decodes byte array with cached data.
     *
     * @param toDecode byte array with data.
     * @param key      key for decoding.
     */
    static byte[] decode(byte[] toDecode, String key) {
        byte[] result = new byte[toDecode.length];
        byte[] keyArray = key.getBytes();

        for (int i = 0; i < toDecode.length; i++) {
            result[i] = (byte) (toDecode[i] ^ keyArray[i % keyArray.length]);
        }

        return result;
    }
}
