package com.jn.langx.util.id.nanoid;

import com.jn.langx.util.Maths;

import java.nio.ByteBuffer;

public class Nanoid {
    // This alphabet uses `A-Za-z0-9_-` symbols.
// The order of characters is optimized for better gzip and brotli compression.
// Same as in non-secure/index.js
    public static final String urlAlphabet = "useandom-26T198340PX75pxJACKVERYMINDBUSHWOLF_GQZbfghjklqvwyzrict";

    // It is best to make fewer, larger requests to the crypto module to
// avoid system call overhead. So, random numbers are generated in a
// pool. The pool is a Buffer that is larger than the initial random
// request size by this multiplier. The pool is enlarged if subsequent
// requests exceed the maximum buffer size.
    public static final int POOL_SIZE_MULTIPLIER = 128;
    ByteBuffer pool;

    private void fillPool(int bytesLength) {
        if (pool == null || pool.capacity() < bytesLength) {
            pool = ByteBuffer.allocate(bytesLength * POOL_SIZE_MULTIPLIER);

            //crypto.randomFillSync(pool)
        } else if (pool.position() + bytesLength > pool.capacity()) {
        }
    }

    protected byte[] random(int bytesLength) {
        fillPool(bytesLength);
        return pool.array();
    }

    public String customRandom(String alphabet, int size) {
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
        int step = Double.valueOf(Math.ceil((1.6 * mask * size) / alphabet.length())).intValue();

        String id = "";
        while (true) {
            byte[] bytes = random(step);
            int i = step;
            while (i-- > 0) {
                // Adding `|| ''` refuses a random byte that exceeds the alphabet size.
                int index = bytes[i] & mask;
                id += index >= alphabet.length() ? "" : alphabet.charAt(index);
                if (id.length() == size) {
                    return id;
                }
            }
        }
    }

    public String customAlphabet(String alphabet, int size) {
        if (size < 1) {
            size = 21;
        }
        return customRandom(alphabet, size);
    }

    public String nanoid(int size) {
        if (size < 1) {
            size = 21;
        }
        fillPool(size);
        StringBuilder b = new StringBuilder("");
        // We are reading directly from the random pool to avoid creating new array
        byte[] bytes = random(size);
        for (int i = 0; i < size; i++) {
            // It is incorrect to use bytes exceeding the alphabet size.
            // The following mask reduces the random byte in the 0-255 value
            // range to the 0-63 value range. Therefore, adding hacks, such
            // as empty string fallback or magic numbers, is unneccessary because
            // the bitmask trims bytes down to the alphabet size.
            b.append(urlAlphabet.charAt(bytes[i] & 63));
        }
        return b.toString();
    }

}
