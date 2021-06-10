package com.jn.langx.codec;


/**
 * Defines common encoding methods for byte array encoders.
 */
public interface BinaryEncoder extends Encoder<byte[], byte[]> {

    /**
     * Encodes a byte array and return the encoded data as a byte array.
     *
     * @param source Data to be encoded
     * @return A byte array containing the encoded data
     * @throws CodecException thrown if the Encoder encounters a failure condition during the encoding process.
     */
    byte[] encode(byte[] source) throws CodecException;
}

