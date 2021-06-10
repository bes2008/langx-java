package com.jn.langx.codec;

/**
 * Defines common decoding methods for byte array decoders.
 */
public interface BinaryDecoder extends Decoder<byte[], byte[]> {

    /**
     * Decodes a byte array and returns the results as a byte array.
     *
     * @param source A byte array which has been encoded with the appropriate encoder
     * @return a byte array that contains decoded content
     * @throws CodecException A decoder exception is thrown if a Decoder encounters a failure condition during the decode process.
     */
    byte[] decode(byte[] source) throws CodecException;
}

