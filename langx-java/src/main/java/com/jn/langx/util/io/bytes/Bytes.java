package com.jn.langx.util.io.bytes;

import com.jn.langx.util.Chars;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * Utility methods for :
 * 1) reading and writing bytes.
 * 2) Converter a byte to int or long, or back
 */
public class Bytes {
    protected Bytes() { /* no instances */ }

    public static final byte SPACE = (byte) ' ';
    public static final byte TAB = (byte) '\t';
    public static final byte CARRIAGE_RETURN = (byte) '\r';
    public static final byte LINE_FEED = (byte) '\n';


    /**
     * Used to supply bytes.
     */
    public interface ByteSupplier {
        /**
         * The contract is similar to {@link InputStream#read()}, return
         * the byte as an unsigned int, -1 if there are no more bytes.
         *
         * @return the supplied byte or -1 if there are no more bytes
         * @throws IOException if supplying fails
         */
        int getAsByte() throws IOException;
    }

    /**
     * Used to consume bytes.
     */
    public interface ByteConsumer {
        /**
         * The contract is similar to {@link OutputStream#write(int)},
         * consume the lower eight bytes of the int as a byte.
         *
         * @param b the byte to consume
         * @throws IOException if consuming fails
         */
        void accept(int b) throws IOException;
    }

    /**
     * Reads the given byte array as a little endian long.
     *
     * @param bytes the byte array to convert
     * @return the number read
     */
    public static long fromLittleEndian(byte[] bytes) {
        return fromLittleEndian(bytes, 0, bytes.length);
    }

    /**
     * Reads the given byte array as a little endian long.
     *
     * @param bytes  the byte array to convert
     * @param off    the offset into the array that starts the value
     * @param length the number of bytes representing the value
     * @return the number read
     * @throws IllegalArgumentException if len is bigger than eight
     */
    public static long fromLittleEndian(byte[] bytes, final int off, final int length) {
        checkReadLength(length);
        long l = 0;
        for (int i = 0; i < length; i++) {
            l |= (bytes[off + i] & 0xffL) << (8 * i);
        }
        return l;
    }

    /**
     * Reads the given number of bytes from the given stream as a little endian long.
     *
     * @param in     the stream to read from
     * @param length the number of bytes representing the value
     * @return the number read
     * @throws IllegalArgumentException if len is bigger than eight
     * @throws IOException              if reading fails or the stream doesn't
     *                                  contain the given number of bytes anymore
     */
    public static long fromLittleEndian(InputStream in, int length) throws IOException {
        // somewhat duplicates the ByteSupplier version in order to save the creation of a wrapper object
        checkReadLength(length);
        long l = 0;
        for (int i = 0; i < length; i++) {
            long b = in.read();
            if (b == -1) {
                throw new IOException("premature end of data");
            }
            l |= (b << (i * 8));
        }
        return l;
    }

    /**
     * Reads the given number of bytes from the given supplier as a little endian long.
     * <p>
     * <p>Typically used by our InputStreams that need to count the
     * bytes read as well.</p>
     *
     * @param supplier the supplier for bytes
     * @param length   the number of bytes representing the value
     * @return the number read
     * @throws IllegalArgumentException if len is bigger than eight
     * @throws IOException              if the supplier fails or doesn't supply the
     *                                  given number of bytes anymore
     */
    public static long fromLittleEndian(ByteSupplier supplier, final int length) throws IOException {
        checkReadLength(length);
        long l = 0;
        for (int i = 0; i < length; i++) {
            long b = supplier.getAsByte();
            if (b == -1) {
                throw new IOException("premature end of data");
            }
            l |= (b << (i * 8));
        }
        return l;
    }

    /**
     * Reads the given number of bytes from the given input as little endian long.
     *
     * @param in     the input to read from
     * @param length the number of bytes representing the value
     * @return the number read
     * @throws IllegalArgumentException if len is bigger than eight
     * @throws IOException              if reading fails or the stream doesn't
     *                                  contain the given number of bytes anymore
     */
    public static long fromLittleEndian(DataInput in, int length) throws IOException {
        // somewhat duplicates the ByteSupplier version in order to save the creation of a wrapper object
        checkReadLength(length);
        long l = 0;
        for (int i = 0; i < length; i++) {
            long b = in.readUnsignedByte();
            l |= (b << (i * 8));
        }
        return l;
    }

    /**
     * Inserts the given value into the array as a little endian
     * sequence of the given length starting at the given offset.
     *
     * @param b      the array to write into
     * @param value  the value to insert
     * @param off    the offset into the array that receives the first byte
     * @param length the number of bytes to use to represent the value
     */
    public static void toLittleEndian(final byte[] b, final long value, final int off, final int length) {
        long num = value;
        for (int i = 0; i < length; i++) {
            b[off + i] = (byte) (num & 0xff);
            num >>= 8;
        }
    }

    public static long littleEndianValue(byte[] bytes, int offset, int length) {
        long value = 0;
        for (int i = length - 1; i >= 0; i--) {
            value = ((value << 8) | (bytes[offset + i] & 0xFF));
        }
        return value;
    }


    /**
     * Writes the given value to the given stream as a little endian
     * array of the given length.
     *
     * @param out    the stream to write to
     * @param value  the value to write
     * @param length the number of bytes to use to represent the value
     * @throws IOException if writing fails
     */
    public static void toLittleEndian(OutputStream out, final long value, final int length)
            throws IOException {
        // somewhat duplicates the ByteConsumer version in order to save the creation of a wrapper object
        long num = value;
        for (int i = 0; i < length; i++) {
            out.write((int) (num & 0xff));
            num >>= 8;
        }
    }

    /**
     * Provides the given value to the given consumer as a little endian
     * sequence of the given length.
     *
     * @param consumer the consumer to provide the bytes to
     * @param value    the value to provide
     * @param length   the number of bytes to use to represent the value
     * @throws IOException if writing fails
     */
    public static void toLittleEndian(ByteConsumer consumer, final long value, final int length)
            throws IOException {
        long num = value;
        for (int i = 0; i < length; i++) {
            consumer.accept((int) (num & 0xff));
            num >>= 8;
        }
    }

    /**
     * Writes the given value to the given stream as a little endian
     * array of the given length.
     *
     * @param out    the output to write to
     * @param value  the value to write
     * @param length the number of bytes to use to represent the value
     * @throws IOException if writing fails
     */
    public static void toLittleEndian(DataOutput out, final long value, final int length)
            throws IOException {
        // somewhat duplicates the ByteConsumer version in order to save the creation of a wrapper object
        long num = value;
        for (int i = 0; i < length; i++) {
            out.write((int) (num & 0xff));
            num >>= 8;
        }
    }

    public static byte toLowerCase(byte c) {
        return Chars.isUpperCase(c) ? (byte) (c + 32) : c;
    }

    public static byte toUpperCase(byte b) {
        return Chars.isLowerCase(b) ? (byte) (b - 32) : b;
    }

    public static boolean isLowerCase(byte value) {
        return value >= 'a' && value <= 'z';
    }

    public static boolean isUpperCase(byte value) {
        return value >= 'A' && value <= 'Z';
    }

    /**
     * {@link ByteSupplier} based on {@link InputStream}.
     */
    public static class InputStreamByteSupplier implements ByteSupplier {
        private final InputStream is;

        public InputStreamByteSupplier(InputStream is) {
            this.is = is;
        }

        @Override
        public int getAsByte() throws IOException {
            return is.read();
        }
    }

    /**
     * {@link ByteConsumer} based on {@link OutputStream}.
     */
    public static class OutputStreamByteConsumer implements ByteConsumer {
        private final OutputStream os;

        public OutputStreamByteConsumer(OutputStream os) {
            this.os = os;
        }

        @Override
        public void accept(int b) throws IOException {
            os.write(b);
        }
    }

    private static void checkReadLength(int length) {
        if (length > 8) {
            throw new IllegalArgumentException("can't read more than eight bytes into a long value");
        }
    }

    public static short bigEndianToShort(byte[] bs, int off) {
        int n = (bs[off] & 0xff) << 8;
        n |= (bs[++off] & 0xff);
        return (short) n;
    }

    public static int bigEndianToInt(byte[] bs, int off) {
        int n = bs[off] << 24;
        n |= (bs[++off] & 0xff) << 16;
        n |= (bs[++off] & 0xff) << 8;
        n |= (bs[++off] & 0xff);
        return n;
    }

    public static void bigEndianToInt(byte[] bs, int off, int[] ns) {
        for (int i = 0; i < ns.length; ++i) {
            ns[i] = bigEndianToInt(bs, off);
            off += 4;
        }
    }

    public static int bigEndianToInt(final byte b, final byte b2, final byte b3, final byte b4) {
        return (b << 24 & 0xFF000000) | (b2 << 16 & 0xFF0000) | (b3 << 8 & 0xFF00) | (b4 & 0xFF);
    }

    public static byte[] intToBigEndian(int n) {
        byte[] bs = new byte[4];
        intToBigEndian(n, bs, 0);
        return bs;
    }

    public static void intToBigEndian(int n, byte[] bs, int off) {
        bs[off] = (byte) (n >>> 24);
        bs[++off] = (byte) (n >>> 16);
        bs[++off] = (byte) (n >>> 8);
        bs[++off] = (byte) (n);
    }


    public static byte[] intToBigEndian(int[] ns) {
        byte[] bs = new byte[4 * ns.length];
        intToBigEndian(ns, bs, 0);
        return bs;
    }

    public static void intToBigEndian(int[] ns, byte[] bs, int off) {
        for (int n : ns) {
            intToBigEndian(n, bs, off);
            off += 4;
        }
    }

    public static long bigEndianToLong(byte[] bs, int off) {
        int hi = bigEndianToInt(bs, off);
        int lo = bigEndianToInt(bs, off + 4);
        return ( (hi & 0xffffffffL) << 32) |(lo & 0xffffffffL);
    }

    public static void bigEndianToLong(byte[] bs, int off, long[] ns) {
        for (int i = 0; i < ns.length; ++i) {
            ns[i] = bigEndianToLong(bs, off);
            off += 8;
        }
    }

    public static byte[] longToBigEndian(long n) {
        byte[] bs = new byte[8];
        longToBigEndian(n, bs, 0);
        return bs;
    }

    public static void longToBigEndian(long n, byte[] bs, int off) {
        intToBigEndian((int) (n >>> 32), bs, off);
        intToBigEndian((int) (n & 0xffffffffL), bs, off + 4);
    }

    public static byte[] longToBigEndian(long[] ns) {
        byte[] bs = new byte[8 * ns.length];
        longToBigEndian(ns, bs, 0);
        return bs;
    }

    public static void longToBigEndian(long[] ns, byte[] bs, int off) {
        for (long n : ns) {
            longToBigEndian(n, bs, off);
            off += 8;
        }
    }

    /**
     * @param value The number
     * @param bs    The target.
     * @param off   Position in target to start.
     * @param bytes number of bytes to write.
     */
    public static void longToBigEndian(long value, byte[] bs, int off, int bytes) {
        for (int i = bytes - 1; i >= 0; i--) {
            bs[i + off] = (byte) (value & 0xff);
            value >>>= 8;
        }
    }

    public static short littleEndianToShort(byte[] bs, int off) {
        int n = bs[off] & 0xff;
        n |= (bs[++off] & 0xff) << 8;
        return (short) n;
    }

    public static int littleEndianToInt(byte[] bs, int off) {
        int n = bs[off] & 0xff;
        n |= (bs[++off] & 0xff) << 8;
        n |= (bs[++off] & 0xff) << 16;
        n |= bs[++off] << 24;
        return n;
    }

    public static void littleEndianToInt(byte[] bs, int off, int[] ns) {
        for (int i = 0; i < ns.length; ++i) {
            ns[i] = littleEndianToInt(bs, off);
            off += 4;
        }
    }

    public static void littleEndianToInt(byte[] bs, int bOff, int[] ns, int nOff, int count) {
        for (int i = 0; i < count; ++i) {
            ns[nOff + i] = littleEndianToInt(bs, bOff);
            bOff += 4;
        }
    }

    public static int[] littleEndianToInt(byte[] bs, int off, int count) {
        int[] ns = new int[count];
        for (int i = 0; i < ns.length; ++i) {
            ns[i] = littleEndianToInt(bs, off);
            off += 4;
        }
        return ns;
    }

    public static byte[] shortToLittleEndian(short n) {
        byte[] bs = new byte[2];
        shortToLittleEndian(n, bs, 0);
        return bs;
    }

    public static void shortToLittleEndian(short n, byte[] bs, int off) {
        bs[off] = (byte) (n);
        bs[++off] = (byte) (n >>> 8);
    }


    public static byte[] shortToBigEndian(short n) {
        byte[] r = new byte[2];
        shortToBigEndian(n, r, 0);
        return r;
    }

    public static void shortToBigEndian(short n, byte[] bs, int off) {
        bs[off] = (byte) (n >>> 8);
        bs[++off] = (byte) (n);
    }


    public static byte[] intToLittleEndian(int n) {
        byte[] bs = new byte[4];
        intToLittleEndian(n, bs, 0);
        return bs;
    }

    public static void intToLittleEndian(int n, byte[] bs, int off) {
        bs[off] = (byte) (n);
        bs[++off] = (byte) (n >>> 8);
        bs[++off] = (byte) (n >>> 16);
        bs[++off] = (byte) (n >>> 24);
    }

    public static byte[] intToLittleEndian(int[] ns) {
        byte[] bs = new byte[4 * ns.length];
        intToLittleEndian(ns, bs, 0);
        return bs;
    }

    public static void intToLittleEndian(int[] ns, byte[] bs, int off) {
        for (int n : ns) {
            intToLittleEndian(n, bs, off);
            off += 4;
        }
    }

    public static long littleEndianToLong(byte[] bs, int off) {
        int lo = littleEndianToInt(bs, off);
        int hi = littleEndianToInt(bs, off + 4);
        return ( (hi & 0xffffffffL) << 32) |  (lo & 0xffffffffL);
    }

    public static void littleEndianToLong(byte[] bs, int off, long[] ns) {
        for (int i = 0; i < ns.length; ++i) {
            ns[i] = littleEndianToLong(bs, off);
            off += 8;
        }
    }

    public static void littleEndianToLong(byte[] bs, int bsOff, long[] ns, int nsOff, int nsLen) {
        for (int i = 0; i < nsLen; ++i) {
            ns[nsOff + i] = littleEndianToLong(bs, bsOff);
            bsOff += 8;
        }
    }

    public static byte[] longToLittleEndian(long n) {
        byte[] bs = new byte[8];
        longToLittleEndian(n, bs, 0);
        return bs;
    }

    public static void longToLittleEndian(long n, byte[] bs, int off) {
        intToLittleEndian((int) (n & 0xffffffffL), bs, off);
        intToLittleEndian((int) (n >>> 32), bs, off + 4);
    }

    public static byte[] longToLittleEndian(long[] ns) {
        byte[] bs = new byte[8 * ns.length];
        longToLittleEndian(ns, bs, 0);
        return bs;
    }

    public static void longToLittleEndian(long[] ns, byte[] bs, int off) {
        for (long n : ns) {
            longToLittleEndian(n, bs, off);
            off += 8;
        }
    }

    public static void longToLittleEndian(long[] ns, int nsOff, int nsLen, byte[] bs, int bsOff) {
        for (int i = 0; i < nsLen; ++i) {
            longToLittleEndian(ns[nsOff + i], bs, bsOff);
            bsOff += 8;
        }
    }


    public static byte[] toBytes(long value) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(value);
        buffer.flip();
        return buffer.array();
    }

    public static byte[] toBytes(double value) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putDouble(value);
        buffer.flip();
        return buffer.array();
    }


    public static byte[] toBytes(float value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(value);
        buffer.flip();
        return buffer.array();
    }


    public static byte[] toBytes(int value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(value);
        buffer.flip();
        return buffer.array();
    }

    public static byte[] toBytes(short value) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(value);
        buffer.flip();
        return buffer.array();
    }

    public static byte[] toBytes(char value) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putChar(value);
        buffer.flip();
        return buffer.array();
    }


}
