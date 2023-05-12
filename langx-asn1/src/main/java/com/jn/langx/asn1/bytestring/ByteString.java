package com.jn.langx.asn1.bytestring;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.asn1.spec.ASN1OctetString;

import java.io.Serializable;


/**
 * This interface defines a set of methods for treating a value as either a
 * string or byte array.
 */
public interface ByteString
        extends Serializable
{
    /**
     * Retrieves a byte array containing the binary value for this byte string.
     *
     * @return  A byte array containing the binary value for this byte string.
     */
    @NonNull()
    byte[] getValue();



    /**
     * Retrieves the value for this byte string as a {@code String}.
     *
     * @return  The value for this byte string as a {@code String}.
     */
    @NonNull()
    String stringValue();



    /**
     * Appends the value of this byte string to the provided buffer.  It must not
     * use the {@link ByteStringBuffer#append(ByteString)} method, since that
     * method relies on this method.
     *
     * @param  buffer  The buffer to which the value should be appended.
     */
    void appendValueTo(@NonNull ByteStringBuffer buffer);



    /**
     * Converts this byte string to an ASN.1 octet string.
     *
     * @return  An ASN.1 octet string with the value of this byte string.
     */
    @NonNull()
    ASN1OctetString toASN1OctetString();
}
