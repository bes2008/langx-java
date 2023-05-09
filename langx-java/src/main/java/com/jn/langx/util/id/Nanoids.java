package com.jn.langx.util.id;

import com.jn.langx.annotation.IntLimit;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.concurrent.threadlocal.GlobalThreadLocalMap;
import com.jn.langx.util.function.Function3;
import com.jn.langx.util.random.BytesRandom;

/**
 * @since 4.4.7
 */
public class Nanoids {
    private Nanoids() {
    }

    /**
     * 基于步长 的ID生成器
     */
    public static final Function3<String, Integer, BytesRandom, String> STEP_ID_FUN = new Function3<String, Integer, BytesRandom, String>() {
        @Override
        public String apply(String alphabet, Integer expectedIdLength, BytesRandom randomBytesSupplier) {
            if (randomBytesSupplier == null) {
                randomBytesSupplier = GlobalThreadLocalMap.pooledBytesRandom();
            }
            // First, a bitmask is necessary to generate the ID. The bitmask makes bytes
            // values closer to the alphabet size. The bitmask calculates the closest
            // `2^31 - 1` number, which exceeds the alphabet size.
            // For example, the bitmask for the alphabet size 30 is 31 (00011111).
            int mask = (2 << (31 - Maths.clz32((alphabet.length() - 1) | 1))) - 1;

            // Though, the bitmask solution is not perfect since the bytes exceeding
            // the alphabet size are refused. Therefore, to reliably generate the ID,
            // the random bytes redundancy has to be satisfied.

            // Note: every hardware random generator call is performance expensive,
            // because the system call for entropy collection takes a lot of time.
            // So, to avoid additional system calls, extra bytes are requested in advance.

            // Next, a step determines how many random bytes to generate.
            // The number of random bytes gets decided upon the ID size, mask,
            // alphabet size, and magic number 1.6 (using 1.6 peaks at performance
            // according to benchmarks).
            int step = Double.valueOf(Math.ceil((1.6 * mask * expectedIdLength) / alphabet.length())).intValue();

            StringBuilder id = new StringBuilder();

            outter:
            while (true) {
                byte[] bytes = randomBytesSupplier.get(step);
                int i = step;

                while (i-- > 0) {
                    int index = bytes[i] & mask;
                    id.append(index >= alphabet.length() ? "" : alphabet.charAt(index));
                    if (id.length() == expectedIdLength) {
                        break outter;
                    }
                }
            }
            return id.toString();
        }
    };

    public static final Function3<String, Integer, BytesRandom, String> SIMPLE_ID_FUN = new Function3<String, Integer, BytesRandom, String>() {
        @Override
        public String apply(String alphabet, Integer expectedIdLength, BytesRandom randomBytesSupplier) {
            if (randomBytesSupplier == null) {
                randomBytesSupplier = GlobalThreadLocalMap.pooledBytesRandom();
            }
            byte[] randomBytes = randomBytesSupplier.get(expectedIdLength);
            StringBuilder b = new StringBuilder();
            // We are reading directly from the random pool to avoid creating new array
            int mask = alphabet.length() - 1;
            for (int i = 0; i < expectedIdLength; i++) {
                // It is incorrect to use bytes exceeding the alphabet size.
                // The following mask reduces the random byte in the 0-255 value
                // range to the 0-63 value range. Therefore, adding hacks, such
                // as empty string fallback or magic numbers, is unneccessary because
                // the bitmask trims bytes down to the alphabet size.
                int index = randomBytes[i] & mask;
                b.append(index >= alphabet.length() ? "" : alphabet.charAt(index));
            }
            return b.toString();
        }
    };


    /**
     * This alphabet uses `A-Za-z0-9_-` symbols.
     * The order of characters is optimized for better gzip and brotli compression.
     * Same as in non-secure/index.js
     */
    public static final String URL_ALPHABET = "useandom-26T198340PX75pxJACKVERYMINDBUSHWOLF_GQZbfghjklqvwyzrict";

    public static String urlNanoid() {
        return urlNanoid(21);
    }

    public static String urlNanoid(int expectedIdLength) {
        return nanoid(URL_ALPHABET, expectedIdLength);
    }


    public static String nanoid(@NotEmpty String alphabet,
                                @IntLimit(value = 21, min = 1) int expectedIdLength) {
        return nanoid(alphabet, expectedIdLength, GlobalThreadLocalMap.pooledBytesRandom(), SIMPLE_ID_FUN);
    }

    public static String nanoid(@NotEmpty String alphabet,
                                @IntLimit(value = 21, min = 1) int expectedIdLength,
                                @NonNull Function3<String, Integer, BytesRandom, String> idGenFun) {
        return nanoid(alphabet, expectedIdLength, GlobalThreadLocalMap.pooledBytesRandom(), idGenFun);
    }


    public static String nanoid(@NotEmpty String alphabet,
                                @IntLimit(value = 21, min = 1) int expectedIdLength,
                                @NonNull BytesRandom randomBytesSupplier,
                                @NonNull Function3<String, Integer, BytesRandom, String> idGenFun) {
        if (expectedIdLength < 1) {
            expectedIdLength = 21;
        }
        Preconditions.checkNotEmpty(alphabet, "alphabet is empty or null");
        Preconditions.checkNotNullArgument(randomBytesSupplier, "randomBytesSupplier");
        Preconditions.checkNotNullArgument(idGenFun, "idGenFun");
        return idGenFun.apply(alphabet, expectedIdLength, randomBytesSupplier);
    }


}
