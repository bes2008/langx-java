package com.jn.langx.codec;

/**
 * Provides the highest level of abstraction for Encoders.
 * <p>
 * This is the sister interface of {@link Decoder}.  Every implementation of Encoder provides this
 * common generic interface which allows a user to pass a generic Object to any Encoder implementation
 * in the codec package.
 */
public interface Encoder<I,O> {

    /**
     * Encodes an "Object" and returns the encoded content as an Object. The Objects here may just be
     * <code>byte[]</code> or <code>String</code>s depending on the implementation used.
     *
     * @param source An object to encode
     * @return An "encoded" Object
     * @throws CodecException An encoder exception is thrown if the encoder experiences a failure condition during the encoding
     *                          process.
     */
    O encode(I source) throws CodecException;
}

