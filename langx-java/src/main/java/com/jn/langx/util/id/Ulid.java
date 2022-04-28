package com.jn.langx.util.id;

import com.jn.langx.security.Securitys;
import com.jn.langx.util.Objs;

import java.io.Serializable;
import java.util.Random;

/*
 * https://github.com/ulid/spec
 */
public class Ulid {
    private static final char[] ENCODING_CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K',
            'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'V', 'W', 'X',
            'Y', 'Z',
    };

    private static final byte[] DECODING_CHARS = {
            // 0
            -1, -1, -1, -1, -1, -1, -1, -1,
            // 8
            -1, -1, -1, -1, -1, -1, -1, -1,
            // 16
            -1, -1, -1, -1, -1, -1, -1, -1,
            // 24
            -1, -1, -1, -1, -1, -1, -1, -1,
            // 32
            -1, -1, -1, -1, -1, -1, -1, -1,
            // 40
            -1, -1, -1, -1, -1, -1, -1, -1,
            // 48
            0, 1, 2, 3, 4, 5, 6, 7,
            // 56
            8, 9, -1, -1, -1, -1, -1, -1,
            // 64
            -1, 10, 11, 12, 13, 14, 15, 16,
            // 72
            17, 1, 18, 19, 1, 20, 21, 0,
            // 80
            22, 23, 24, 25, 26, -1, 27, 28,
            // 88
            29, 30, 31, -1, -1, -1, -1, -1,
            // 96
            -1, 10, 11, 12, 13, 14, 15, 16,
            // 104
            17, 1, 18, 19, 1, 20, 21, 0,
            // 112
            22, 23, 24, 25, 26, -1, 27, 28,
            // 120
            29, 30, 31,
    };

    private static final int MASK = 0x1F;
    private static final int MASK_BITS = 5;
    private static final long TIMESTAMP_OVERFLOW_MASK = 0xFFFF000000000000L;
    private static final long TIMESTAMP_MSB_MASK = 0xFFFFFFFFFFFF0000L;
    private static final long RANDOM_MSB_MASK = 0xFFFFL;

    private final Random random;

    public Ulid() {
        this(Securitys.getSecureRandom());
    }

    public Ulid(Random random) {
        Objs.requireNonNull(random, "random must not be null!");
        this.random = random;
    }

    public void appendULID(StringBuilder stringBuilder) {
        Objs.requireNonNull(stringBuilder, "stringBuilder must not be null!");
        internalAppendULID(stringBuilder, System.currentTimeMillis(), random);
    }

    public String nextULID() {
        return nextULID(System.currentTimeMillis());
    }

    public String nextULID(long timestamp) {
        return internalUIDString(timestamp, random);
    }

    public Ulid.Value nextValue() {
        return nextValue(System.currentTimeMillis());
    }

    public Ulid.Value nextValue(long timestamp) {
        return internalNextValue(timestamp, random);
    }

    /**
     * Returns the next monotonic value. If an overflow happened while incrementing
     * the random part of the given previous ULID value then the returned value will
     * have a zero random part.
     *
     * @param previousUlid the previous ULID value.
     * @return the next monotonic value.
     */
    public Ulid.Value nextMonotonicValue(Ulid.Value previousUlid) {
        return nextMonotonicValue(previousUlid, System.currentTimeMillis());
    }

    /**
     * Returns the next monotonic value. If an overflow happened while incrementing
     * the random part of the given previous ULID value then the returned value will
     * have a zero random part.
     *
     * @param previousUlid the previous ULID value.
     * @param timestamp    the timestamp of the next ULID value.
     * @return the next monotonic value.
     */
    public Ulid.Value nextMonotonicValue(Ulid.Value previousUlid, long timestamp) {
        Objs.requireNonNull(previousUlid, "previousUlid must not be null!");
        if (previousUlid.timestamp() == timestamp) {
            return previousUlid.increment();
        }
        return nextValue(timestamp);
    }

    /**
     * Returns the next monotonic value or empty if an overflow happened while incrementing
     * the random part of the given previous ULID value.
     *
     * @param previousUlid the previous ULID value.
     * @return the next monotonic value or empty if an overflow happened.
     */
    public Ulid.Value nextStrictlyMonotonicValue(Ulid.Value previousUlid) {
        return nextStrictlyMonotonicValue(previousUlid, System.currentTimeMillis());
    }

    /**
     * Returns the next monotonic value or empty if an overflow happened while incrementing
     * the random part of the given previous ULID value.
     *
     * @param previousUlid the previous ULID value.
     * @param timestamp    the timestamp of the next ULID value.
     * @return the next monotonic value or empty if an overflow happened.
     */
    public Ulid.Value nextStrictlyMonotonicValue(Ulid.Value previousUlid, long timestamp) {
        Ulid.Value result = nextMonotonicValue(previousUlid, timestamp);
        if (result.compareTo(previousUlid) < 1) {
            return null;
        }
        return result;
    }

    public static Ulid.Value parseULID(String ulidString) {
        Objs.requireNonNull(ulidString, "ulidString must not be null!");
        if (ulidString.length() != 26) {
            throw new IllegalArgumentException("ulidString must be exactly 26 chars long.");
        }

        String timeString = ulidString.substring(0, 10);
        long time = internalParseCrockford(timeString);
        if ((time & TIMESTAMP_OVERFLOW_MASK) != 0) {
            throw new IllegalArgumentException("ulidString must not exceed '7ZZZZZZZZZZZZZZZZZZZZZZZZZ'!");
        }
        String part1String = ulidString.substring(10, 18);
        String part2String = ulidString.substring(18);
        long part1 = internalParseCrockford(part1String);
        long part2 = internalParseCrockford(part2String);

        long most = (time << 16) | (part1 >>> 24);
        long least = part2 | (part1 << 40);
        return new Ulid.Value(most, least);
    }

    public static Ulid.Value fromBytes(byte[] data) {
        Objs.requireNonNull(data, "data must not be null!");
        if (data.length != 16) {
            throw new IllegalArgumentException("data must be 16 bytes in length!");
        }
        long mostSignificantBits = 0;
        long leastSignificantBits = 0;
        for (int i = 0; i < 8; i++) {
            mostSignificantBits = (mostSignificantBits << 8) | (data[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            leastSignificantBits = (leastSignificantBits << 8) | (data[i] & 0xff);
        }
        return new Ulid.Value(mostSignificantBits, leastSignificantBits);
    }

    public static class Value
            implements Comparable<Ulid.Value>, Serializable {
        private static final long serialVersionUID = -3563159514112487717L;

        /*
         * The most significant 64 bits of this ULID.
         */
        private final long mostSignificantBits;

        /*
         * The least significant 64 bits of this ULID.
         */
        private final long leastSignificantBits;

        public Value(long mostSignificantBits, long leastSignificantBits) {
            this.mostSignificantBits = mostSignificantBits;
            this.leastSignificantBits = leastSignificantBits;
        }

        /**
         * Returns the most significant 64 bits of this ULID's 128 bit value.
         *
         * @return The most significant 64 bits of this ULID's 128 bit value
         */
        public long getMostSignificantBits() {
            return mostSignificantBits;
        }

        /**
         * Returns the least significant 64 bits of this ULID's 128 bit value.
         *
         * @return The least significant 64 bits of this ULID's 128 bit value
         */
        public long getLeastSignificantBits() {
            return leastSignificantBits;
        }


        public long timestamp() {
            return mostSignificantBits >>> 16;
        }

        public byte[] toBytes() {
            byte[] result = new byte[16];
            for (int i = 0; i < 8; i++) {
                result[i] = (byte) ((mostSignificantBits >> ((7 - i) * 8)) & 0xFF);
            }
            for (int i = 8; i < 16; i++) {
                result[i] = (byte) ((leastSignificantBits >> ((15 - i) * 8)) & 0xFF);
            }

            return result;
        }

        public Ulid.Value increment() {
            long lsb = leastSignificantBits;
            if (lsb != 0xFFFFFFFFFFFFFFFFL) {
                return new Ulid.Value(mostSignificantBits, lsb + 1);
            }
            long msb = mostSignificantBits;
            if ((msb & RANDOM_MSB_MASK) != RANDOM_MSB_MASK) {
                return new Ulid.Value(msb + 1, 0);
            }
            return new Ulid.Value(msb & TIMESTAMP_MSB_MASK, 0);
        }

        @Override
        public int hashCode() {
            long hilo = mostSignificantBits ^ leastSignificantBits;
            return ((int) (hilo >> 32)) ^ (int) hilo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Ulid.Value value = (Ulid.Value) o;

            return mostSignificantBits == value.mostSignificantBits
                    && leastSignificantBits == value.leastSignificantBits;
        }

        @Override
        public int compareTo(Ulid.Value val) {
            // The ordering is intentionally set up so that the ULIDs
            // can simply be numerically compared as two numbers
            return (this.mostSignificantBits < val.mostSignificantBits ? -1 :
                    (this.mostSignificantBits > val.mostSignificantBits ? 1 :
                            (this.leastSignificantBits < val.leastSignificantBits ? -1 :
                                    (this.leastSignificantBits > val.leastSignificantBits ? 1 :
                                            0))));
        }

        @Override
        public String toString() {
            char[] buffer = new char[26];

            internalWriteCrockford(buffer, timestamp(), 10, 0);
            long value = ((mostSignificantBits & 0xFFFFL) << 24);
            long interim = (leastSignificantBits >>> 40);
            value = value | interim;
            internalWriteCrockford(buffer, value, 8, 10);
            internalWriteCrockford(buffer, leastSignificantBits, 8, 18);

            return new String(buffer);
        }
    }

    /*
     * http://crockford.com/wrmg/base32.html
     */
    static void internalAppendCrockford(StringBuilder builder, long value, int count) {
        for (int i = count - 1; i >= 0; i--) {
            int index = (int) ((value >>> (i * MASK_BITS)) & MASK);
            builder.append(ENCODING_CHARS[index]);
        }
    }

    static long internalParseCrockford(String input) {
        Objs.requireNonNull(input, "input must not be null!");
        int length = input.length();
        if (length > 12) {
            throw new IllegalArgumentException("input length must not exceed 12 but was " + length + "!");
        }

        long result = 0;
        for (int i = 0; i < length; i++) {
            char current = input.charAt(i);
            byte value = -1;
            if (current < DECODING_CHARS.length) {
                value = DECODING_CHARS[current];
            }
            if (value < 0) {
                throw new IllegalArgumentException("Illegal character '" + current + "'!");
            }
            result |= ((long) value) << ((length - 1 - i) * MASK_BITS);
        }
        return result;
    }

    /*
     * http://crockford.com/wrmg/base32.html
     */
    static void internalWriteCrockford(char[] buffer, long value, int count, int offset) {
        for (int i = 0; i < count; i++) {
            int index = (int) ((value >>> ((count - i - 1) * MASK_BITS)) & MASK);
            buffer[offset + i] = ENCODING_CHARS[index];
        }
    }

    static String internalUIDString(long timestamp, Random random) {
        checkTimestamp(timestamp);

        char[] buffer = new char[26];

        internalWriteCrockford(buffer, timestamp, 10, 0);
        // could use nextBytes(byte[] bytes) instead
        internalWriteCrockford(buffer, random.nextLong(), 8, 10);
        internalWriteCrockford(buffer, random.nextLong(), 8, 18);

        return new String(buffer);
    }

    static void internalAppendULID(StringBuilder builder, long timestamp, Random random) {
        checkTimestamp(timestamp);

        internalAppendCrockford(builder, timestamp, 10);
        // could use nextBytes(byte[] bytes) instead
        internalAppendCrockford(builder, random.nextLong(), 8);
        internalAppendCrockford(builder, random.nextLong(), 8);
    }

    static Ulid.Value internalNextValue(long timestamp, Random random) {
        checkTimestamp(timestamp);
        // could use nextBytes(byte[] bytes) instead
        long mostSignificantBits = random.nextLong();
        long leastSignificantBits = random.nextLong();
        mostSignificantBits &= 0xFFFF;
        mostSignificantBits |= (timestamp << 16);
        return new Ulid.Value(mostSignificantBits, leastSignificantBits);
    }

    private static void checkTimestamp(long timestamp) {
        if ((timestamp & TIMESTAMP_OVERFLOW_MASK) != 0) {
            throw new IllegalArgumentException("ULID does not support timestamps after +10889-08-02T05:31:50.655Z!");
        }
    }
}

