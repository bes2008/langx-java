package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;

/**
 * This class defines a number of constants that are used in the course of
 * processing ASN.1 BER elements.  It is intended for internal use only and
 * should not be referenced by classes outside of the LDAP SDK.
 */
public final class ASN1Constants {
    /**
     * Prevent this class from being instantiated.
     */
    private ASN1Constants() {
        // No implementation is required.
    }


    /**
     * A pre-allocated array of zero elements, which can be used for sequence or
     * set elements that do not encapsulate any other elements.
     */
    @NonNull
    static final ASN1Element[] NO_ELEMENTS = new ASN1Element[0];


    /**
     * A byte array that should be used as the default value for an ASN.1 Boolean
     * element with a boolean value of "FALSE".
     */
    @NonNull
    static final byte[] BOOLEAN_VALUE_FALSE = {(byte) 0x00};


    /**
     * A byte array that should be used as the default value for an ASN.1 Boolean
     * element with a boolean value of "TRUE".
     */
    @NonNull
    static final byte[] BOOLEAN_VALUE_TRUE = {(byte) 0xFF};




    /**
     * The BER type for the universal Boolean element.
     */
    public static final byte UNIVERSAL_BOOLEAN_TYPE = 0x01;


    /**
     * The BER type for the universal integer element.
     */
    public static final byte UNIVERSAL_INTEGER_TYPE = 0x02;


    /**
     * The BER type for the universal bit string element.
     */
    public static final byte UNIVERSAL_BIT_STRING_TYPE = 0x03;


    /**
     * The BER type for the universal octet string element.
     */
    public static final byte UNIVERSAL_OCTET_STRING_TYPE = 0x04;


    /**
     * The BER type for the universal null element.
     */
    public static final byte UNIVERSAL_NULL_TYPE = 0x05;


    /**
     * The BER type for the universal object identifier element.
     */
    public static final byte UNIVERSAL_OBJECT_IDENTIFIER_TYPE = 0x06;


    /**
     * The BER type for the universal enumerated element.
     */
    public static final byte UNIVERSAL_ENUMERATED_TYPE = 0x0A;


    /**
     * The BER type for the universal UTF-8 string element.
     */
    public static final byte UNIVERSAL_UTF_8_STRING_TYPE = 0x0C;


    /**
     * The BER type for the universal numeric string element.
     */
    public static final byte UNIVERSAL_NUMERIC_STRING_TYPE = 0x12;


    /**
     * The BER type for the universal printable string element.
     */
    public static final byte UNIVERSAL_PRINTABLE_STRING_TYPE = 0x13;


    /**
     * The BER type for the universal IA5 string element.
     */
    public static final byte UNIVERSAL_IA5_STRING_TYPE = 0x16;


    /**
     * The BER type for the universal UTC time element.
     */
    public static final byte UNIVERSAL_UTC_TIME_TYPE = 0x17;


    /**
     * The BER type for the universal generalized time element.
     */
    public static final byte UNIVERSAL_GENERALIZED_TIME_TYPE = 0x18;


    /**
     * The BER type for the universal sequence element.
     */
    public static final byte UNIVERSAL_SEQUENCE_TYPE = 0x30;


    /**
     * The BER type for the universal set element.
     */
    public static final byte UNIVERSAL_SET_TYPE = 0x31;


    /**
     * A byte array that should be used as the value for an ASN.1 element if it
     * does not have a value (i.e., the value length is zero bytes).
     */
    @NonNull
    public static final byte[] NO_VALUE = new byte[0];


    /**
     * A mask that may be used when building a BER type in the universal class.
     * To build the type, perform a bitwise OR with one of the
     * {@code TYPE_MASK_*_CLASS} constants, one of the
     * {@code TYPE_MASK_PC_*} constants, and a byte that represents the desired
     * tag number.  Note that this method only works for tag numbers between zero
     * and thirty, since tag numbers greater than thirty require a multi-byte
     * type, but none of the LDAP specifications attempt to use a tag number
     * greater than twenty-five, so it is highly unlikely that you will ever
     * encounter the need for a multi-byte type in LDAP.
     */
    public static final byte TYPE_MASK_UNIVERSAL_CLASS = 0x00;


    /**
     * A mask that may be used when building a BER type in the application class.
     * To build the type, perform a bitwise OR with one of the
     * {@code TYPE_MASK_*_CLASS} constants, one of the
     * {@code TYPE_MASK_PC_*} constants, and a byte that represents the desired
     * tag number.  Note that this method only works for tag numbers between zero
     * and thirty, since tag numbers greater than thirty require a multi-byte
     * type, but none of the LDAP specifications attempt to use a tag number
     * greater than twenty-five, so it is highly unlikely that you will ever
     * encounter the need for a multi-byte type in LDAP.
     */
    public static final byte TYPE_MASK_APPLICATION_CLASS = 0x40;



    /**
     * A mask that may be used when building a BER type in the context-specific
     * class.  To build the type, perform a bitwise OR with one of the
     * {@code TYPE_MASK_*_CLASS} constants, one of the
     * {@code TYPE_MASK_PC_*} constants, and a byte that represents the desired
     * tag number.  Note that this method only works for tag numbers between zero
     * and thirty, since tag numbers greater than thirty require a multi-byte
     * type, but none of the LDAP specifications attempt to use a tag number
     * greater than twenty-five, so it is highly unlikely that you will ever
     * encounter the need for a multi-byte type in LDAP.
     */
    public static final byte TYPE_MASK_CONTEXT_SPECIFIC_CLASS = (byte) 0x80;




    /**
     * A mask that may be used when building a BER type in the private class.  To
     * build the type, perform a bitwise OR with one of the
     * {@code TYPE_MASK_*_CLASS} constants, one of the
     * {@code TYPE_MASK_PC_*} constants, and a byte that represents the desired
     * tag number.  Note that this method only works for tag numbers between zero
     * and thirty, since tag numbers greater than thirty require a multi-byte
     * type, but none of the LDAP specifications attempt to use a tag number
     * greater than twenty-five, so it is highly unlikely that you will ever
     * encounter the need for a multi-byte type in LDAP.
     */
    public static final byte TYPE_MASK_PRIVATE_CLASS = (byte) 0xC0;




    /**
     * A mask that may be used when building a BER type with a primitive value
     * (i.e., a value that is not known to be comprised of a concatenation of the
     * encoded representations of zero or more BER elements).  To build the type,
     * perform a bitwise OR with one of the {@code TYPE_MASK_*_CLASS} constants,
     * one of the {@code TYPE_MASK_PC_*} constants, and a byte that represents the
     * desired tag number.  Note that this method only works for tag numbers
     * between zero and thirty, since tag numbers greater than thirty require a
     * multi-byte type, but none of the LDAP specifications attempt to use a tag
     * number greater than twenty-five, so it is highly unlikely that you will
     * ever encounter the need for a multi-byte type in LDAP.
     */
    public static final byte TYPE_MASK_PC_PRIMITIVE = 0x00;



    /**
     * A mask that may be used when building a BER type with a constructed value
     * (i.e., a value that is comprised of a concatenation of the encoded
     * representations of zero or more BER elements).  To build the type, perform
     * a bitwise OR with one of the {@code TYPE_MASK_*_CLASS} constants, one of
     * the {@code TYPE_MASK_PC_*} constants, and a byte that represents the
     * desired tag number.  Note that this method only works for tag numbers
     * between zero and thirty, since tag numbers greater than thirty require a
     * multi-byte type, but none of the LDAP specifications attempt to use a tag
     * number greater than twenty-five, so it is highly unlikely that you will
     * ever encounter the need for a multi-byte type in LDAP.
     */
    public static final byte TYPE_MASK_PC_CONSTRUCTED = 0x20;

}
