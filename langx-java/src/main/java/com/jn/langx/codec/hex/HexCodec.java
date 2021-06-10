package com.jn.langx.codec.hex;


import com.jn.langx.codec.BinaryCodec;
import com.jn.langx.codec.CodecException;
import com.jn.langx.util.io.Charsets;

import java.nio.charset.Charset;

import static com.jn.langx.codec.hex.Hex.decodeHex;

/**
 * Converts hexadecimal Strings. The charset used for certain operation can be set, the default is set in
 * {@link #DEFAULT_CHARSET_NAME}
 * <p>
 * This class is thread-safe.
 */
public class HexCodec implements BinaryCodec {

    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";


    private final Charset charset;

    /**
     * Creates a new codec with the default charset name {@link #DEFAULT_CHARSET}
     */
    public HexCodec() {
        // use default encoding
        this.charset = DEFAULT_CHARSET;
    }

    /**
     * Creates a new codec with the given Charset.
     *
     * @param charset the charset.
     * @since 1.7
     */
    public HexCodec(final Charset charset) {
        this.charset = charset;
    }

    /**
     * Creates a new codec with the given charset name.
     *
     * @param charsetName the charset name.
     * @throws java.nio.charset.UnsupportedCharsetException If the named charset is unavailable
     * @since 1.7 throws UnsupportedCharsetException if the named charset is unavailable
     */
    public HexCodec(final String charsetName) {
        this(Charset.forName(charsetName));
    }

    /**
     * Converts an array of character bytes representing hexadecimal values into an array of bytes of those same values.
     * The returned array will be half the length of the passed array, as it takes two characters to represent any given
     * byte. An exception is thrown if the passed char array has an odd number of elements.
     *
     * @param array An array of character bytes containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied byte array (representing characters).
     * @throws CodecException Thrown if an odd number of characters is supplied to this function
     * @see {@link Hex#decodeHex(char[])}
     */
    @Override
    public byte[] decode(final byte[] array) throws CodecException {
        return decodeHex(new String(array, getCharset()).toCharArray());
    }

    /**
     * Converts a String or an array of character bytes representing hexadecimal values into an array of bytes of those
     * same values. The returned array will be half the length of the passed String or array, as it takes two characters
     * to represent any given byte. An exception is thrown if the passed char array has an odd number of elements.
     *
     * @param object A String or, an array of character bytes containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied byte array (representing characters).
     * @throws CodecException Thrown if an odd number of characters is supplied to this function or the object is not a String or
     *                          char[]
     * @see {@link Hex#decodeHex(char[])}
     */
    public byte[] decodeObject(final Object object) throws CodecException {
        try {
            final char[] charArray = object instanceof String ? ((String) object).toCharArray() : (char[]) object;
            return decodeHex(charArray);
        } catch (final ClassCastException e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    /**
     * Converts an array of bytes into an array of bytes for the characters representing the hexadecimal values of each
     * byte in order. The returned array will be double the length of the passed array, as it takes two characters to
     * represent any given byte.
     * <p>
     * The conversion from hexadecimal characters to the returned bytes is performed with the charset named by
     * {@link #getCharset()}.
     * </p>
     *
     * @param array a byte[] to convert to Hex characters
     * @return A byte[] containing the bytes of the hexadecimal characters
     * @see {@link Hex#encodeHex(byte[])}
     * @since 1.7 No longer throws IllegalStateException if the charsetName is invalid.
     */
    @Override
    public byte[] encode(final byte[] array) {
        return Hex.encodeHexString(array).getBytes(this.getCharset());
    }

    /**
     * Converts a String or an array of bytes into an array of characters representing the hexadecimal values of each
     * byte in order. The returned array will be double the length of the passed String or array, as it takes two
     * characters to represent any given byte.
     * <p>
     * The conversion from hexadecimal characters to bytes to be encoded to performed with the charset named by
     * {@link #getCharset()}.
     * </p>
     *
     * @param object a String, or byte[] to convert to Hex characters
     * @return A char[] containing hexadecimal characters
     * @throws CodecException Thrown if the given object is not a String or byte[]
     * @see {@link Hex#encodeHex(byte[])}
     */
    public char[] encodeObject(final Object object) throws CodecException {
        try {
            final byte[] byteArray = object instanceof String ?
                    ((String) object).getBytes(this.getCharset()) : (byte[]) object;
            return Hex.encodeHex(byteArray);
        } catch (final ClassCastException e) {
            throw new CodecException(e.getMessage(), e);
        }
    }

    /**
     * Gets the charset.
     *
     * @return the charset.
     * @since 1.7
     */
    public Charset getCharset() {
        return this.charset;
    }

    /**
     * Gets the charset name.
     *
     * @return the charset name.
     * @since 1.4
     */
    public String getCharsetName() {
        return this.charset.name();
    }

    /**
     * Returns a string representation of the object, which includes the charset name.
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return super.toString() + "[charsetName=" + this.charset + "]";
    }
}
