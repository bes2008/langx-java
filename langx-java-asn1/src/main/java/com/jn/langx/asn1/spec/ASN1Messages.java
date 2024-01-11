package com.jn.langx.asn1.spec;


import com.jn.langx.util.logging.Loggers;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;


/**
 * This enum defines a set of message keys for messages in the
 * com.unboundid.asn1 package, which correspond to messages in the
 * unboundid-ldapsdk-asn1.properties properties file.
 * <BR><BR>
 * This source file was generated from the properties file.
 * Do not edit it directly.
 */
enum ASN1Messages {
    /**
     * Invalid length of zero bytes for an ASN.1 big integer element.
     */
    ERR_BIG_INTEGER_DECODE_EMPTY_VALUE("Invalid length of zero bytes for an ASN.1 big integer element."),


    /**
     * Unable to decode an ASN.1 bit string element because the element value is empty.
     */
    ERR_BIT_STRING_DECODE_EMPTY_VALUE("Unable to decode an ASN.1 bit string element because the element value is empty."),


    /**
     * Unable to decode an ASN.1 bit string element because the first byte of the value, which is used to indicate the number of padding bits needed in the last byte, has an invalid value of {0,number,0}.  The value must be between 0 and 7, inclusive.
     */
    ERR_BIT_STRING_DECODE_INVALID_PADDING_BIT_COUNT("Unable to decode an ASN.1 bit string element because the first byte of the value, which is used to indicate the number of padding bits needed in the last byte, has an invalid value of {0,number,0}.  The value must be between 0 and 7, inclusive."),


    /**
     * Unable to decode an ASN.1 bit string element because the first byte of the value, which is used to indicate the number of padding bits needed in the last byte, has a nonzero value, but the value does not have any more bytes.
     */
    ERR_BIT_STRING_DECODE_NONZERO_PADDING_BIT_COUNT_WITH_NO_MORE_BYTES("Unable to decode an ASN.1 bit string element because the first byte of the value, which is used to indicate the number of padding bits needed in the last byte, has a nonzero value, but the value does not have any more bytes."),


    /**
     * Unable to parse the provided string as a bit string because the string is not comprised entirely of the characters ''1'' and ''0''.
     */
    ERR_BIT_STRING_DECODE_STRING_INVALID_CHAR("Unable to parse the provided string as a bit string because the string is not comprised entirely of the characters ''1'' and ''0''."),


    /**
     * Unable to get the bit string value as a byte array because the bit string contains {0,number,0} bits, which is not a multiple of eight.
     */
    ERR_BIT_STRING_GET_BYTES_NOT_MULTIPLE_OF_EIGHT_BITS("Unable to get the bit string value as a byte array because the bit string contains {0,number,0} bits, which is not a multiple of eight."),


    /**
     * ASN.1 Boolean elements must have a value whose length is exactly one byte.
     */
    ERR_BOOLEAN_INVALID_LENGTH("ASN.1 Boolean elements must have a value whose length is exactly one byte."),


    /**
     * Unable to decode the provided byte array as an ASN.1 BER element:  {0}
     */
    ERR_ELEMENT_DECODE_EXCEPTION("Unable to decode the provided byte array as an ASN.1 BER element:  {0}"),


    /**
     * The decoded length of {0,number,0} does not match the number of bytes remaining in the provided array ({1,number,0}).
     */
    ERR_ELEMENT_LENGTH_MISMATCH("The decoded length of {0,number,0} does not match the number of bytes remaining in the provided array ({1,number,0})."),


    /**
     * Invalid value length of {0,number,0} for an ASN.1 enumerated element.  Enumerated element values must have a length between 1 and 4 bytes.
     */
    ERR_ENUMERATED_INVALID_LENGTH("Invalid value length of {0,number,0} for an ASN.1 enumerated element.  Enumerated element values must have a length between 1 and 4 bytes."),


    /**
     * Unable to parse the provided string as an ASN.1 generalized time value because the character at position {0,number,0} is not a digit.
     */
    ERR_GENERALIZED_TIME_STRING_CHAR_NOT_DIGIT("Unable to parse the provided string as an ASN.1 generalized time value because the character at position {0,number,0} is not a digit."),


    /**
     * Unable to parse the provided string as an ASN.1 generalized time value because the character at position {0,number,0} is neither a decimal point (to separate the seconds component from a sub-second component) or a ''Z'' (to indicate that the timestamp is in the UTC time zone).
     */
    ERR_GENERALIZED_TIME_STRING_CHAR_NOT_PERIOD("Unable to parse the provided string as an ASN.1 generalized time value because the character at position {0,number,0} is neither a decimal point (to separate the seconds component from a sub-second component) or a ''Z'' (to indicate that the timestamp is in the UTC time zone)."),


    /**
     * Unable to parse the provided string as an ASN.1 generalized time value because the string does not end with ''Z'' to indicate that the timestamp is in the UTC time zone.
     */
    ERR_GENERALIZED_TIME_STRING_DOES_NOT_END_WITH_Z("Unable to parse the provided string as an ASN.1 generalized time value because the string does not end with ''Z'' to indicate that the timestamp is in the UTC time zone."),


    /**
     * Unable to parse the provided string as an ASN.1 generalized time value because the day-of-month component is not between 1 and 31, inclusive.
     */
    ERR_GENERALIZED_TIME_STRING_INVALID_DAY("Unable to parse the provided string as an ASN.1 generalized time value because the day-of-month component is not between 1 and 31, inclusive."),


    /**
     * Unable to parse the provided string as an ASN.1 generalized time value because the hour component is not between 0 and 23, inclusive.
     */
    ERR_GENERALIZED_TIME_STRING_INVALID_HOUR("Unable to parse the provided string as an ASN.1 generalized time value because the hour component is not between 0 and 23, inclusive."),


    /**
     * Unable to parse the provided string as an ASN.1 generalized time value because the minute component is not between 0 and 59, inclusive.
     */
    ERR_GENERALIZED_TIME_STRING_INVALID_MINUTE("Unable to parse the provided string as an ASN.1 generalized time value because the minute component is not between 0 and 59, inclusive."),


    /**
     * Unable to parse the provided string as an ASN.1 generalized time value because the month component is not between 1 and 12, inclusive.
     */
    ERR_GENERALIZED_TIME_STRING_INVALID_MONTH("Unable to parse the provided string as an ASN.1 generalized time value because the month component is not between 1 and 12, inclusive."),


    /**
     * Unable to parse the provided string as an ASN.1 generalized time value because the second component is not between 0 and 60, inclusive.
     */
    ERR_GENERALIZED_TIME_STRING_INVALID_SECOND("Unable to parse the provided string as an ASN.1 generalized time value because the second component is not between 0 and 60, inclusive."),


    /**
     * Unable to parse the provided string as an ASN.1 generalized time value because the string is shorter than the minimum valid length of 15 characters.
     */
    ERR_GENERALIZED_TIME_STRING_TOO_SHORT("Unable to parse the provided string as an ASN.1 generalized time value because the string is shorter than the minimum valid length of 15 characters."),


    /**
     * Unable to create an ASN.1 IA5 string with the provided value because the value contains one or more non-ASCII characters.
     */
    ERR_IA5_STRING_DECODE_VALUE_NOT_IA5("Unable to create an ASN.1 IA5 string with the provided value because the value contains one or more non-ASCII characters."),


    /**
     * Invalid value length of {0,number,0} for an ASN.1 integer element.  Integer element values must have a length between 1 and 4 bytes.
     */
    ERR_INTEGER_INVALID_LENGTH("Invalid value length of {0,number,0} for an ASN.1 integer element.  Integer element values must have a length between 1 and 4 bytes."),


    /**
     * Invalid value length of {0,number,0} for an ASN.1 long element.  Long element values must have a length between 1 and 8 bytes.
     */
    ERR_LONG_INVALID_LENGTH("Invalid value length of {0,number,0} for an ASN.1 long element.  Long element values must have a length between 1 and 8 bytes."),


    /**
     * ASN.1 null elements must not have a value.
     */
    ERR_NULL_HAS_VALUE("ASN.1 null elements must not have a value."),


    /**
     * Unable to create an ASN.1 numeric string with the provided value because the value contains one or more characters that are not allowed in numeric strings.  A numeric string must only contain ASCII numeric digits or the ASCII space character.
     */
    ERR_NUMERIC_STRING_DECODE_VALUE_NOT_NUMERIC("Unable to create an ASN.1 numeric string with the provided value because the value contains one or more characters that are not allowed in numeric strings.  A numeric string must only contain ASCII numeric digits or the ASCII space character."),


    /**
     * Unable to decode the provided ASN.1 element as an object identifier because the element value is empty.
     */
    ERR_OID_DECODE_EMPTY_VALUE("Unable to decode the provided ASN.1 element as an object identifier because the element value is empty."),


    /**
     * Unable to decode the provided ASN.1 element as an object identifier because the last byte of the encoded value has its most significant bit set to one, which indicates that there should be at least one more byte of data.
     */
    ERR_OID_DECODE_INCOMPLETE_VALUE("Unable to decode the provided ASN.1 element as an object identifier because the last byte of the encoded value has its most significant bit set to one, which indicates that there should be at least one more byte of data."),


    /**
     * Unable to parse string ''{0}'' as a valid OID because the first component has a value of {1,number,0} but the first component of an OID can only be 0, 1, or 2.
     */
    ERR_OID_ENCODE_INVALID_FIRST_COMPONENT("Unable to parse string ''{0}'' as a valid OID because the first component has a value of {1,number,0} but the first component of an OID can only be 0, 1, or 2."),


    /**
     * Unable to parse string ''{0}'' as a valid OID because the first component has a value of {1,number,0} and the second component has a value of {2,number,0}.  If the value of the first component is 0 or 1, then the value of the second component must be between 0 and 39, inclusive.
     */
    ERR_OID_ENCODE_INVALID_SECOND_COMPONENT("Unable to parse string ''{0}'' as a valid OID because the first component has a value of {1,number,0} and the second component has a value of {2,number,0}.  If the value of the first component is 0 or 1, then the value of the second component must be between 0 and 39, inclusive."),


    /**
     * Unable to parse string ''{0}'' as a valid OID because it does not have at least two components
     */
    ERR_OID_ENCODE_NOT_ENOUGH_COMPONENTS("Unable to parse string ''{0}'' as a valid OID because it does not have at least two components"),


    /**
     * The provided object identifier is not a valid numeric OID
     */
    ERR_OID_ENCODE_NOT_NUMERIC("The provided object identifier is not a valid numeric OID"),


    /**
     * Unable to create an ASN.1 printable string with the provided value because the value contains one or more characters that are not in the set of printable characters.  A printable string may contain ASCII characters from the following set:  all uppercase and lowercase letters, all digits, space, apostrophe, open and close parentheses, plus sign, minus sign, comma, period, forward slash, colon, equal sign, and question mark.
     */
    ERR_PRINTABLE_STRING_DECODE_VALUE_NOT_PRINTABLE("Unable to create an ASN.1 printable string with the provided value because the value contains one or more characters that are not in the set of printable characters.  A printable string may contain ASCII characters from the following set:  all uppercase and lowercase letters, all digits, space, apostrophe, open and close parentheses, plus sign, minus sign, comma, period, forward slash, colon, equal sign, and question mark."),


    /**
     * The end of the input stream was reached before the first length byte could be read.
     */
    ERR_READ_END_BEFORE_FIRST_LENGTH("The end of the input stream was reached before the first length byte could be read."),


    /**
     * The end of the input stream was reached before the full length could be read.
     */
    ERR_READ_END_BEFORE_LENGTH_END("The end of the input stream was reached before the full length could be read."),


    /**
     * The end of the input stream was reached before the full value could be read.
     */
    ERR_READ_END_BEFORE_VALUE_END("The end of the input stream was reached before the full value could be read."),


    /**
     * The element indicated that it required {0,number,0} bytes to hold the value, but this is larger than the maximum of {1,number,0} bytes that the client has been configured to accept.
     */
    ERR_READ_LENGTH_EXCEEDS_MAX("The element indicated that it required {0,number,0} bytes to hold the value, but this is larger than the maximum of {1,number,0} bytes that the client has been configured to accept."),


    /**
     * The element indicated that it required {0,number,0} bytes to encode the multi-byte length, but multi-byte lengths must be encoded in 1 to 4 bytes.
     */
    ERR_READ_LENGTH_TOO_LONG("The element indicated that it required {0,number,0} bytes to encode the multi-byte length, but multi-byte lengths must be encoded in 1 to 4 bytes."),


    /**
     * The SASL client indicated that a wrapped message contained {0,number,0} bytes, but this is larger than the maximum of {1,number,0} bytes that the client has been configured to accept.
     */
    ERR_READ_SASL_LENGTH_EXCEEDS_MAX("The SASL client indicated that a wrapped message contained {0,number,0} bytes, but this is larger than the maximum of {1,number,0} bytes that the client has been configured to accept."),


    /**
     * Unable to decode the provided byte array as a sequence:  {0}
     */
    ERR_SEQUENCE_BYTES_DECODE_EXCEPTION("Unable to decode the provided byte array as a sequence:  {0}"),


    /**
     * Unable to decode the provided byte array as a sequence because the decoded length of an embedded element exceeds the number of bytes remaining.
     */
    ERR_SEQUENCE_BYTES_DECODE_LENGTH_EXCEEDS_AVAILABLE("Unable to decode the provided byte array as a sequence because the decoded length of an embedded element exceeds the number of bytes remaining."),


    /**
     * Unable to decode the provided ASN.1 element {0} as a sequence:  {1}
     */
    ERR_SEQUENCE_DECODE_EXCEPTION("Unable to decode the provided ASN.1 element {0} as a sequence:  {1}"),


    /**
     * Unable to decode the provided ASN.1 element {0} as a sequence because the decoded length of an embedded element exceeds the number of bytes remaining.
     */
    ERR_SEQUENCE_DECODE_LENGTH_EXCEEDS_AVAILABLE("Unable to decode the provided ASN.1 element {0} as a sequence because the decoded length of an embedded element exceeds the number of bytes remaining."),


    /**
     * Unable to decode the provided byte array as a set:  {0}
     */
    ERR_SET_BYTES_DECODE_EXCEPTION("Unable to decode the provided byte array as a set:  {0}"),


    /**
     * Unable to decode the provided byte array as a set because the decoded length of an embedded element exceeds the number of bytes remaining.
     */
    ERR_SET_BYTES_DECODE_LENGTH_EXCEEDS_AVAILABLE("Unable to decode the provided byte array as a set because the decoded length of an embedded element exceeds the number of bytes remaining."),


    /**
     * Unable to decode the provided ASN.1 element {0} as a set:  {1}
     */
    ERR_SET_DECODE_EXCEPTION("Unable to decode the provided ASN.1 element {0} as a set:  {1}"),


    /**
     * Unable to decode the provided ASN.1 element {0} as a set because the decoded length of an embedded element exceeds the number of bytes remaining.
     */
    ERR_SET_DECODE_LENGTH_EXCEEDS_AVAILABLE("Unable to decode the provided ASN.1 element {0} as a set because the decoded length of an embedded element exceeds the number of bytes remaining."),


    /**
     * Unable to read SASL-encoded data because the end of the input stream was reached after reading only {0,number,0} bytes of the expected {1,number,0} bytes of wrapped data.
     */
    ERR_STREAM_READER_EOS_READING_SASL_DATA("Unable to read SASL-encoded data because the end of the input stream was reached after reading only {0,number,0} bytes of the expected {1,number,0} bytes of wrapped data."),


    /**
     * Unable to read SASL-encoded data because the end of the input stream was reached after reading only {0,number,0} bytes of the expected four-byte SASL length header.
     */
    ERR_STREAM_READER_EOS_READING_SASL_LENGTH("Unable to read SASL-encoded data because the end of the input stream was reached after reading only {0,number,0} bytes of the expected four-byte SASL length header."),


    /**
     * The ASN.1 stream reader has already read beyond the end of this sequence (expected sequence of length {0} to end at {1} bytes into the stream, but {2} bytes have already been read from the stream).
     */
    ERR_STREAM_READER_SEQUENCE_READ_PAST_END("The ASN.1 stream reader has already read beyond the end of this sequence (expected sequence of length {0} to end at {1} bytes into the stream, but {2} bytes have already been read from the stream)."),


    /**
     * The ASN.1 stream reader has already read beyond the end of this set (expected set of length {0} to end at {1} bytes into the stream, but {2} bytes have already been read from the stream).
     */
    ERR_STREAM_READER_SET_READ_PAST_END("The ASN.1 stream reader has already read beyond the end of this set (expected set of length {0} to end at {1} bytes into the stream, but {2} bytes have already been read from the stream)."),


    /**
     * Unable to parse the provided string as an ASN.1 UTC time value:  {0}
     */
    ERR_UTC_TIME_STRING_CANNOT_PARSE("Unable to parse the provided string as an ASN.1 UTC time value:  {0}"),


    /**
     * Unable to parse the provided string as an ASN.1 UTC time value because the character at position {0,number,0} is not a digit.
     */
    ERR_UTC_TIME_STRING_CHAR_NOT_DIGIT("Unable to parse the provided string as an ASN.1 UTC time value because the character at position {0,number,0} is not a digit."),


    /**
     * Unable to parse the provided string as an ASN.1 UTC time value because the string does not end with ''Z'' to indicate that the timestamp is in the UTC time zone.
     */
    ERR_UTC_TIME_STRING_DOES_NOT_END_WITH_Z("Unable to parse the provided string as an ASN.1 UTC time value because the string does not end with ''Z'' to indicate that the timestamp is in the UTC time zone."),


    /**
     * Unable to parse the provided string as an ASN.1 UTC time value because the day-of-month component is not between 1 and 31, inclusive.
     */
    ERR_UTC_TIME_STRING_INVALID_DAY("Unable to parse the provided string as an ASN.1 UTC time value because the day-of-month component is not between 1 and 31, inclusive."),


    /**
     * Unable to parse the provided string as an ASN.1 UTC time value because the hour component is not between 0 and 23, inclusive.
     */
    ERR_UTC_TIME_STRING_INVALID_HOUR("Unable to parse the provided string as an ASN.1 UTC time value because the hour component is not between 0 and 23, inclusive."),


    /**
     * Unable to parse the provided string as an ASN.1 UTC time value because the string does not have a length of 13 characters.
     */
    ERR_UTC_TIME_STRING_INVALID_LENGTH("Unable to parse the provided string as an ASN.1 UTC time value because the string does not have a length of 13 characters."),


    /**
     * Unable to parse the provided string as an ASN.1 UTC time value because the minute component is not between 0 and 59, inclusive.
     */
    ERR_UTC_TIME_STRING_INVALID_MINUTE("Unable to parse the provided string as an ASN.1 UTC time value because the minute component is not between 0 and 59, inclusive."),


    /**
     * Unable to parse the provided string as an ASN.1 UTC time value because the month component is not between 1 and 12, inclusive.
     */
    ERR_UTC_TIME_STRING_INVALID_MONTH("Unable to parse the provided string as an ASN.1 UTC time value because the month component is not between 1 and 12, inclusive."),


    /**
     * Unable to parse the provided string as an ASN.1 UTC time value because the second component is not between 0 and 60, inclusive.
     */
    ERR_UTC_TIME_STRING_INVALID_SECOND("Unable to parse the provided string as an ASN.1 UTC time value because the second component is not between 0 and 60, inclusive."),


    /**
     * Unable to decode the provided ASN.1 element as a UTF-8 string element because the value is not valid UTF-8.
     */
    ERR_UTF_8_STRING_DECODE_VALUE_NOT_UTF_8("Unable to decode the provided ASN.1 element as a UTF-8 string element because the value is not valid UTF-8.");


    /**
     * Indicates whether the unit tests are currently running.
     */
    private static final boolean IS_WITHIN_UNIT_TESTS =
            Boolean.getBoolean("com.unboundid.ldap.sdk.RunningUnitTests") ||
                    Boolean.getBoolean("com.unboundid.directory.server.RunningUnitTests");


    /**
     * A pre-allocated array of zero objects to use for messages
     * that do not require any arguments.
     */
    private static final Object[] NO_ARGS = new Object[0];


    /**
     * The resource bundle that will be used to load the properties file.
     */
    private static final ResourceBundle RESOURCE_BUNDLE;

    static {
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle("unboundid-ldapsdk-asn1");
        } catch (final Exception e) {
            Loggers.getLogger(ASN1Messages.class).warn("can't find the resource bundle: unboundid-ldapsdk-asn1");
        }
        RESOURCE_BUNDLE = rb;
    }


    /**
     * The map that will be used to hold the unformatted message strings, indexed by property name.
     */
    private static final ConcurrentHashMap<ASN1Messages, String> MESSAGE_STRINGS = new ConcurrentHashMap<ASN1Messages, String>(100);


    /**
     * The map that will be used to hold the message format objects, indexed by property name.
     */
    private static final ConcurrentHashMap<ASN1Messages, MessageFormat> MESSAGES = new ConcurrentHashMap<ASN1Messages, MessageFormat>(100);


    // The default text for this message
    private final String defaultText;


    /**
     * Creates a new message key.
     */
    ASN1Messages(final String defaultText) {
        this.defaultText = defaultText;
    }


    /**
     * Retrieves a localized version of the message.
     * This method should only be used for messages that do not take any
     * arguments.
     *
     * @return A localized version of the message.
     */
    public String get() {
        MessageFormat f = MESSAGES.get(this);
        if (f == null) {
            if (RESOURCE_BUNDLE == null) {
                f = new MessageFormat(defaultText);
            } else {
                try {
                    f = new MessageFormat(RESOURCE_BUNDLE.getString(name()));
                } catch (final Exception e) {
                    f = new MessageFormat(defaultText);
                }
            }
            MESSAGES.putIfAbsent(this, f);
        }

        final String formattedMessage;
        synchronized (f) {
            formattedMessage = f.format(NO_ARGS);
        }

        if (IS_WITHIN_UNIT_TESTS) {
            if (formattedMessage.contains("{0}") ||
                    formattedMessage.contains("{0,number,0}") ||
                    formattedMessage.contains("{1}") ||
                    formattedMessage.contains("{1,number,0}") ||
                    formattedMessage.contains("{2}") ||
                    formattedMessage.contains("{2,number,0}") ||
                    formattedMessage.contains("{3}") ||
                    formattedMessage.contains("{3,number,0}") ||
                    formattedMessage.contains("{4}") ||
                    formattedMessage.contains("{4,number,0}") ||
                    formattedMessage.contains("{5}") ||
                    formattedMessage.contains("{5,number,0}") ||
                    formattedMessage.contains("{6}") ||
                    formattedMessage.contains("{6,number,0}") ||
                    formattedMessage.contains("{7}") ||
                    formattedMessage.contains("{7,number,0}") ||
                    formattedMessage.contains("{8}") ||
                    formattedMessage.contains("{8,number,0}") ||
                    formattedMessage.contains("{9}") ||
                    formattedMessage.contains("{9,number,0}") ||
                    formattedMessage.contains("{10}") ||
                    formattedMessage.contains("{10,number,0}")) {
                throw new IllegalArgumentException("Message " + getClass().getName() + '.' + name() + " contains an un-replaced token:  " + formattedMessage);
            }
        }

        return formattedMessage;
    }


    /**
     * Retrieves a localized version of the message.
     *
     * @param args The arguments to use to format the message.
     * @return A localized version of the message.
     */
    public String get(final Object... args) {
        MessageFormat f = MESSAGES.get(this);
        if (f == null) {
            if (RESOURCE_BUNDLE == null) {
                f = new MessageFormat(defaultText);
            } else {
                try {
                    f = new MessageFormat(RESOURCE_BUNDLE.getString(name()));
                } catch (final Exception e) {
                    f = new MessageFormat(defaultText);
                }
            }
            MESSAGES.putIfAbsent(this, f);
        }

        final String formattedMessage;
        synchronized (f) {
            formattedMessage = f.format(args);
        }

        if (IS_WITHIN_UNIT_TESTS) {
            if (formattedMessage.contains("{0}") ||
                    formattedMessage.contains("{0,number,0}") ||
                    formattedMessage.contains("{1}") ||
                    formattedMessage.contains("{1,number,0}") ||
                    formattedMessage.contains("{2}") ||
                    formattedMessage.contains("{2,number,0}") ||
                    formattedMessage.contains("{3}") ||
                    formattedMessage.contains("{3,number,0}") ||
                    formattedMessage.contains("{4}") ||
                    formattedMessage.contains("{4,number,0}") ||
                    formattedMessage.contains("{5}") ||
                    formattedMessage.contains("{5,number,0}") ||
                    formattedMessage.contains("{6}") ||
                    formattedMessage.contains("{6,number,0}") ||
                    formattedMessage.contains("{7}") ||
                    formattedMessage.contains("{7,number,0}") ||
                    formattedMessage.contains("{8}") ||
                    formattedMessage.contains("{8,number,0}") ||
                    formattedMessage.contains("{9}") ||
                    formattedMessage.contains("{9,number,0}") ||
                    formattedMessage.contains("{10}") ||
                    formattedMessage.contains("{10,number,0}")) {
                throw new IllegalArgumentException(
                        "Message " + getClass().getName() + '.' + name() +
                                " contains an un-replaced token:  " + formattedMessage);
            }
        }

        return formattedMessage;
    }


    /**
     * Retrieves a string representation of this message key.
     *
     * @return A string representation of this message key.
     */
    @Override()
    public String toString() {
        String s = MESSAGE_STRINGS.get(this);
        if (s == null) {
            if (RESOURCE_BUNDLE == null) {
                s = defaultText;
            } else {
                try {
                    s = RESOURCE_BUNDLE.getString(name());
                } catch (final Exception e) {
                    s = defaultText;
                }
                MESSAGE_STRINGS.putIfAbsent(this, s);
            }
        }

        return s;
    }
}

