package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.io.bytes.Utf8s;

import static com.jn.langx.asn1.spec.ASN1Messages.*;


/**
 * This class provides an ASN.1 numeric string element that can hold any
 * empty or non-empty string comprised only of the ASCII numeric digits '0'
 * through '9' and the ASCII space.
 */
public final class ASN1NumericString
       extends ASN1Element
{
  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = -3972798266250943461L;



  // The string value for this element.
  @NonNull private final String stringValue;



  /**
   * Creates a new ASN.1 numeric string element with the default BER type and
   * the provided value.
   *
   * @param  stringValue  The string value to use for this element.  It may be
   *                      {@code null} or empty if the value should be empty.
   *                      It must only contain ASCII digit and space characters.
   *
   * @throws  ASN1Exception  If the provided string does not represent a valid
   *                         numeric string.
   */
  public ASN1NumericString(@Nullable final String stringValue)
         throws ASN1Exception
  {
    this(ASN1Constants.UNIVERSAL_NUMERIC_STRING_TYPE, stringValue);
  }



  /**
   * Creates a new ASN.1 numeric string element with the specified BER type
   * and the provided value.
   *
   * @param  type         The BER type for this element.
   * @param  stringValue  The string value to use for this element.  It may be
   *                      {@code null} or empty if the value should be empty.
   *                      It must only contain ASCII digit and space characters.
   *
   * @throws  ASN1Exception  If the provided string does not represent a valid
   *                         numeric string.
   */
  public ASN1NumericString(final byte type,
                           @Nullable final String stringValue)
         throws ASN1Exception
  {
    this(type, stringValue, Utf8s.getBytes(stringValue));
  }



  /**
   * Creates a new ASN.1 numeric string element with the specified BER type
   * and the provided value.
   *
   * @param  type          The BER type for this element.
   * @param  stringValue   The string value to use for this element.  It may be
   *                       {@code null} or empty if the value should be empty.
   *                       It must only contain ASCII digit and space
   *                       characters.
   * @param  encodedValue  The bytes that comprise the encoded element value.
   *
   * @throws  ASN1Exception  If the provided string does not represent a valid
   *                         numeric string.
   */
  private ASN1NumericString(final byte type,
                            @Nullable final String stringValue,
                            @NonNull final byte[] encodedValue)
          throws ASN1Exception
  {
    super(type, encodedValue);

    if (stringValue == null)
    {
      this.stringValue = "";
    }
    else
    {
      this.stringValue = stringValue;
      for (final char c : stringValue.toCharArray())
      {
        if ((c >= '0') && (c <= '9'))
        {
          // ASCII digits are allowed in numeric strings.
        }
        else if (c == ' ')
        {
          // The space is allowed in numeric strings.
        }
        else
        {
          throw new ASN1Exception(
               ERR_NUMERIC_STRING_DECODE_VALUE_NOT_NUMERIC.get());
        }
      }
    }
  }



  /**
   * Retrieves the string value for this element.
   *
   * @return  The string value for this element.
   */
  @NonNull()
  public String stringValue()
  {
    return stringValue;
  }



  /**
   * Decodes the contents of the provided byte array as a numeric string
   * element.
   *
   * @param  elementBytes  The byte array to decode as an ASN.1 numeric string
   *                       element.
   *
   * @return  The decoded ASN.1 numeric string element.
   *
   * @throws  ASN1Exception  If the provided array cannot be decoded as a
   *                         numeric string element.
   */
  @NonNull()
  public static ASN1NumericString decodeAsNumericString(
                                       @NonNull final byte[] elementBytes)
         throws ASN1Exception
  {
    try
    {
      int valueStartPos = 2;
      int length = (elementBytes[1] & 0x7F);
      if (length != elementBytes[1])
      {
        final int numLengthBytes = length;

        length = 0;
        for (int i=0; i < numLengthBytes; i++)
        {
          length <<= 8;
          length |= (elementBytes[valueStartPos++] & 0xFF);
        }
      }

      if ((elementBytes.length - valueStartPos) != length)
      {
        throw new ASN1Exception(ERR_ELEMENT_LENGTH_MISMATCH.get(length,
                                     (elementBytes.length - valueStartPos)));
      }

      final byte[] elementValue = new byte[length];
      System.arraycopy(elementBytes, valueStartPos, elementValue, 0, length);

      return new ASN1NumericString(elementBytes[0],
           Utf8s.toUTF8String(elementValue), elementValue);
    }
    catch (final ASN1Exception ae)
    {
      //Debug.debugException(ae);
      throw ae;
    }
    catch (final Exception e)
    {
      //Debug.debugException(e);
      throw new ASN1Exception(ERR_ELEMENT_DECODE_EXCEPTION.get(e), e);
    }
  }



  /**
   * Decodes the provided ASN.1 element as a numeric string element.
   *
   * @param  element  The ASN.1 element to be decoded.
   *
   * @return  The decoded ASN.1 numeric string element.
   *
   * @throws  ASN1Exception  If the provided element cannot be decoded as a
   *                         numeric string element.
   */
  @NonNull()
  public static ASN1NumericString decodeAsNumericString(
                                       @NonNull final ASN1Element element)
         throws ASN1Exception
  {
    final byte[] elementValue = element.getValue();
    return new ASN1NumericString(element.getType(),
         Utf8s.toUTF8String(elementValue), elementValue);
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public void toString(@NonNull final StringBuilder buffer)
  {
    buffer.append(stringValue);
  }
}
