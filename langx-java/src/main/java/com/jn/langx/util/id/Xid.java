package com.jn.langx.util.id;


import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>A globally unique identifier for objects.</p>
 *
 * <p>Consists of 12 bytes, divided as follows:</p>
 * <table border="1">
 * <caption>layout</caption>
 * <tr>
 * <td>0</td><td>1</td><td>2</td><td>3</td><td>4</td><td>5</td><td>6</td><td>7</td><td>8</td><td>9</td><td>10</td><td>11</td>
 * </tr>
 * <tr>
 * <td colspan="4">time</td><td colspan="5">random value</td><td colspan="3">inc</td>
 * </tr>
 * </table>
 *
 * <p>Instances of this class are immutable.</p>
 *
 *
 * @since 4.4.6
 */
public final class Xid implements Comparable<Xid> {
    private static final int ID_LENGTH = 12;
    private static final int LOW_ORDER_THREE_BYTES = 0x00ffffff;

    // Use primitives to represent the 5-byte random value.
    private static final int RANDOM_VALUE1;
    private static final short RANDOM_VALUE2;

    private static final AtomicInteger NEXT_COUNTER = new AtomicInteger(new SecureRandom().nextInt());

    private static final char[] BASE32_HEX_CHARS = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v',
    };
    private static final int[] BASE32_LOOKUP_TABLE = {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, // '0', '1', '2', '3', '4', '5', '6', '7'
            0x08, 0x09, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, // '8', '9', ':', ';', '<', '=', '>', '?'
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, // '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G'
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, // 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O'
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, // 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W'
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, // 'X', 'Y', 'Z', '[', '\', ']', '^', '_'
            0xFF, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F, 0x10, // '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g'
            0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17, 0x18, // 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o'
            0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F, 0xFF, // 'p', 'q', 'r', 's', 't', 'u', 'v', 'w'
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF  // 'x', 'y', 'z', '{', '|', '}', '~', 'DEL'
    };

    private final int timestamp;
    private final int counter;
    private final int randomValue1;
    private final short randomValue2;

    static {
        try {
            SecureRandom secureRandom = new SecureRandom();
            RANDOM_VALUE1 = secureRandom.nextInt(0x01000000);
            RANDOM_VALUE2 = (short) secureRandom.nextInt(0x00008000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create a new object id.
     */
    public Xid() {
        this(new Date());
    }

    /**
     * Constructs a new instance using the given date.
     *
     * @param date the date
     */
    public Xid(final Date date) {
        this(dateToTimestampSeconds(date), NEXT_COUNTER.getAndIncrement() & LOW_ORDER_THREE_BYTES, false);
    }

    /**
     * Constructs a new instances using the given date and counter.
     *
     * @param date    the date
     * @param counter the counter
     * @throws IllegalArgumentException if the high order byte of counter is not zero
     */
    public Xid(final Date date,
               final int counter) {
        this(dateToTimestampSeconds(date), counter, true);
    }

    /**
     * Creates an Xid using the given time, machine identifier, process identifier, and counter.
     *
     * @param timestamp the time in seconds
     * @param counter   the counter
     * @throws IllegalArgumentException if the high order byte of counter is not zero
     */
    public Xid(final int timestamp,
               final int counter) {
        this(timestamp, counter, true);
    }

    private Xid(final int timestamp,
                final int counter,
                final boolean checkCounter) {
        this(timestamp, RANDOM_VALUE1, RANDOM_VALUE2, counter, checkCounter);
    }

    private Xid(final int timestamp,
                final int randomValue1,
                final short randomValue2,
                final int counter,
                final boolean checkCounter) {
        if ((randomValue1 & 0xff000000) != 0) {
            throw new IllegalArgumentException("The machine identifier must be between 0 and 16777215 (it must fit in three bytes).");
        }
        if (checkCounter && ((counter & 0xff000000) != 0)) {
            throw new IllegalArgumentException("The counter must be between 0 and 16777215 (it must fit in three bytes).");
        }
        this.timestamp = timestamp;
        this.counter = counter & LOW_ORDER_THREE_BYTES;
        this.randomValue1 = randomValue1;
        this.randomValue2 = randomValue2;
    }

    /**
     * Constructs a new instance from a 24-byte hexadecimal string representation.
     *
     * @param hexString the string to convert
     * @throws IllegalArgumentException if the string is not a valid hex string representation of an Xid
     */
    public Xid(final String hexString) {
        this(parseHexString(hexString));
    }

    /**
     * Constructs a new instance from the given byte array
     *
     * @param bytes the byte array
     * @throws IllegalArgumentException if array is null or not of length 12
     */
    public Xid(final byte[] bytes) {
        this(ByteBuffer.wrap(isTrue("bytes has length of 12", bytes, paramNotNull("bytes", bytes).length == 12)));
    }

    /**
     * Constructs a new instance from the given ByteBuffer
     *
     * @param buffer the ByteBuffer
     * @throws IllegalArgumentException if the buffer is null or does not have at least 12 bytes remaining
     */
    public Xid(final ByteBuffer buffer) {
        paramNotNull("buffer", buffer);
        isTrue("buffer.remaining() >=12", buffer.remaining() >= ID_LENGTH);

        // Note: Cannot use ByteBuffer.getInt because it depends on tbe buffer's byte order
        // and Xid's are always in big-endian order.
        timestamp = makeInt(buffer.get(), buffer.get(), buffer.get(), buffer.get());
        randomValue1 = makeInt((byte) 0, buffer.get(), buffer.get(), buffer.get());
        randomValue2 = makeShort(buffer.get(), buffer.get());
        counter = makeInt((byte) 0, buffer.get(), buffer.get(), buffer.get());
    }

    // Factory methods

    /**
     * Gets a new object id.
     *
     * @return the new id
     */
    public static Xid get() {
        return new Xid();
    }

    public static String string() {
        return get().toHexString();
    }

    public static byte[] bytes() {
        return get().toByteArray();
    }

    /**
     * Gets a new object id with the given date value and all other bits zeroed.
     * <p>
     * The returned object id will compare as less than or equal to any other object id within the same second as the given date, and
     * less than any later date.
     * </p>
     *
     * @param date the date
     * @return the Xid
     */
    public static Xid getSmallestWithDate(final Date date) {
        return new Xid(dateToTimestampSeconds(date), 0, (short) 0, 0, false);
    }

    /**
     * Convert to a byte array.  Note that the numbers are stored in big-endian order.
     *
     * @return the byte array
     */
    public byte[] toByteArray() {
        ByteBuffer buffer = ByteBuffer.allocate(ID_LENGTH);
        putToByteBuffer(buffer);
        return buffer.array();  // using .allocate ensures there is a backing array that can be returned
    }

    /**
     * Convert to bytes and put those bytes to the provided ByteBuffer.
     * Note that the numbers are stored in big-endian order.
     *
     * @param buffer the ByteBuffer
     * @throws IllegalArgumentException if the buffer is null or does not have at least 12 bytes remaining
     */
    public void putToByteBuffer(final ByteBuffer buffer) {
        paramNotNull("buffer", buffer);
        isTrue("buffer.remaining() >=12", buffer.remaining() >= ID_LENGTH);

        buffer.put(int3(timestamp));
        buffer.put(int2(timestamp));
        buffer.put(int1(timestamp));
        buffer.put(int0(timestamp));
        buffer.put(int2(randomValue1));
        buffer.put(int1(randomValue1));
        buffer.put(int0(randomValue1));
        buffer.put(short1(randomValue2));
        buffer.put(short0(randomValue2));
        buffer.put(int2(counter));
        buffer.put(int1(counter));
        buffer.put(int0(counter));
    }

    /**
     * Gets the timestamp (number of seconds since the Unix epoch).
     *
     * @return the timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }

    /**
     * Gets the timestamp as a {@code Date} instance.
     *
     * @return the Date
     */
    public Date getDate() {
        return new Date((timestamp & 0xFFFFFFFFL) * 1000L);
    }

    /**
     * Converts this instance into a 20-byte hexadecimal string representation.
     *
     * @return a string representation of the Xid in hexadecimal format
     */
    public String toHexString() {
        return base32Hex(toByteArray());
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Xid other = (Xid) o;

        if (counter != other.counter) {
            return false;
        }
        if (timestamp != other.timestamp) {
            return false;
        }

        if (randomValue1 != other.randomValue1) {
            return false;
        }

        return randomValue2 == other.randomValue2;
    }

    @Override
    public int hashCode() {
        int result = timestamp;
        result = 31 * result + counter;
        result = 31 * result + randomValue1;
        result = 31 * result + randomValue2;
        return result;
    }

    @Override
    public int compareTo(final Xid other) {
        if (other == null) {
            throw new NullPointerException();
        }

        byte[] byteArray = toByteArray();
        byte[] otherByteArray = other.toByteArray();
        for (int i = 0; i < ID_LENGTH; i++) {
            if (byteArray[i] != otherByteArray[i]) {
                return ((byteArray[i] & 0xff) < (otherByteArray[i] & 0xff)) ? -1 : 1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return toHexString();
    }

    /**
     * Checks if a string could be an {@code Xid}.
     *
     * @param hexString a potential Xid as a String.
     * @return whether the string could be an object id
     * @throws IllegalArgumentException if hexString is null
     */
    public static boolean isValid(final String hexString) {
        if (hexString == null) {
            throw new IllegalArgumentException();
        }

        int len = hexString.length();
        if (len != 20) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            char c = hexString.charAt(i);
            if (c >= '0' && c <= '9') {
                continue;
            }
            if (c >= 'a' && c <= 'v') {
                continue;
            }

            return false;
        }

        return true;
    }

    private static byte[] parseHexString(final String id) {
        if (!isValid(id)) {
            throw new IllegalArgumentException("invalid hexadecimal representation of an Xid: [" + id + "]");
        }

        return base32Hex(id);
    }

    /**
     * Encodes byte array to Base32 String.
     *
     * @param bytes Bytes to encode.
     * @return Encoded byte array <code>bytes</code> as a String.
     */
    private static String base32Hex(final byte[] bytes) {
        int i = 0;
        int index = 0;
        int digit;
        int currByte;
        int nextByte;
        StringBuilder base32 = new StringBuilder((bytes.length + 7) * 8 / 5);

        while (i < bytes.length) {
            currByte = (bytes[i] >= 0) ? bytes[i] : (bytes[i] + 256); // unsigned

            if (index > 3) { // Current digit spanning a byte boundary?
                if ((i + 1) < bytes.length) {
                    nextByte = (bytes[i + 1] >= 0) ? bytes[i + 1] : (bytes[i + 1] + 256);
                } else {
                    nextByte = 0;
                }

                digit = currByte & (0xFF >> index);
                index = (index + 5) % 8;
                digit <<= index;
                digit |= nextByte >> (8 - index);
                i++;
            } else {
                digit = (currByte >> (8 - (index + 5))) & 0x1F;
                index = (index + 5) % 8;
                if (index == 0) {
                    i++;
                }
            }
            base32.append(BASE32_HEX_CHARS[digit]);
        }

        return base32.toString();
    }

    /**
     * Decodes the given Base32 String to a raw byte array.
     *
     * @return Decoded <code>base32</code> String as a raw byte array.
     */
    private static byte[] base32Hex(final String base32) {
        int i;
        int index;
        int lookup;
        int offset;
        int digit;
        byte[] bytes = new byte[base32.length() * 5 / 8];

        for (i = 0, index = 0, offset = 0; i < base32.length(); i++) {
            lookup = base32.charAt(i) - '0';

            /* Skip chars outside the lookup table. */
            if (lookup < 0 || lookup >= BASE32_LOOKUP_TABLE.length) {
                continue;
            }

            digit = BASE32_LOOKUP_TABLE[lookup];

            /* If this digit is not in the table, ignore it. */
            if (digit == 0xFF) {
                continue;
            }

            if (index <= 3) {
                index = (index + 5) % 8;
                if (index == 0) {
                    bytes[offset] |= digit;
                    offset++;
                    if (offset >= bytes.length) {
                        break;
                    }
                } else {
                    bytes[offset] |= digit << (8 - index);
                }
            } else {
                index = (index + 5) % 8;
                bytes[offset] |= (digit >>> index);
                offset++;

                if (offset >= bytes.length) {
                    break;
                }
                bytes[offset] |= digit << (8 - index);
            }
        }
        return bytes;
    }

    private static int dateToTimestampSeconds(final Date time) {
        return (int) (time.getTime() / 1000);
    }

    // Big-Endian helpers, in this class because all other BSON numbers are little-endian

    private static int makeInt(final byte b3, final byte b2, final byte b1, final byte b0) {
        return (((b3) << 24) |
                ((b2 & 0xff) << 16) |
                ((b1 & 0xff) << 8) |
                b0 & 0xff);
    }

    private static short makeShort(final byte b1, final byte b0) {
        return (short) (((b1 & 0xff) << 8) | b0 & 0xff);
    }

    private static byte int3(final int x) {
        return (byte) (x >> 24);
    }

    private static byte int2(final int x) {
        return (byte) (x >> 16);
    }

    private static byte int1(final int x) {
        return (byte) (x >> 8);
    }

    private static byte int0(final int x) {
        return (byte) (x);
    }

    private static byte short1(final short x) {
        return (byte) (x >> 8);
    }

    private static byte short0(final short x) {
        return (byte) (x);
    }

    public static void isTrue(final String name,
                              final boolean condition) {
        if (!condition) {
            throw new IllegalStateException("state should be: " + name);
        }
    }

    static <T> T isTrue(final String name,
                        final T value,
                        final boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException("state should be: " + name);
        }
        return value;
    }

    static <T> T paramNotNull(final String name,
                              final T value) {
        if (value == null) {
            throw new IllegalArgumentException(name + " can not be null");
        }
        return value;
    }
}