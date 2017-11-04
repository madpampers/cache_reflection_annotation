package ru.edhunter.cache_implementation.cacheUtils;

/**
 * Class BasicCipher provides XOR encryption/decryption of cache.
 */
class BasicCipher {
    private static final String KEY;

    static {
        KEY = CacheManager.getKey();
    }

    /**
     * Encodes byte array with cached data.
     *
     * @param toEncode byte array with data.
     */
    static byte[] encode(byte[] toEncode) {
        byte[] key = KEY.getBytes();
        byte[] result = new byte[toEncode.length];

        for (int i = 0; i < toEncode.length; i++) {
            result[i] = (byte) (toEncode[i] ^ key[i % key.length]);
        }

        return result;
    }

    /**
     * Decodes byte array with cached data.
     *
     * @param toDecode byte array with data.
     */
    static byte[] decode(byte[] toDecode) {
        byte[] result = new byte[toDecode.length];
        byte[] key = KEY.getBytes();

        for (int i = 0; i < toDecode.length; i++) {
            result[i] = (byte) (toDecode[i] ^ key[i % key.length]);
        }

        return result;
    }
}
