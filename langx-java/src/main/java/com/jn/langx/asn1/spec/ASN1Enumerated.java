package com.jn.langx.asn1.spec;

import com.jn.langx.annotation.NonNull;

import static com.jn.langx.asn1.spec.ASN1Messages.*;



/**
 * This class provides an ASN.1 enumerated element.  Enumerated elements are
 * very similar to integer elements, and the only real difference between them
 * is that the individual values of an enumerated element have a symbolic
 * significance (i.e., each value is associated with a particular meaning),
 * although this does not impact its encoding other than through the use of a
 * different default BER type.
 */
public final class ASN1Enumerated
       extends ASN1Element
{
  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = -5915912036130847725L;



  // The int value for this element.
  private final int intValue;



  /**
   * Creates a new ASN.1 enumerated element with the default BER type and the
   * provided int value.
   *
   * @param  intValue  The int value to use for this element.
   */
  public ASN1Enumerated(final int intValue)
  {
    super(ASN1Constants.UNIVERSAL_ENUMERATED_TYPE,
         ASN1Integer.encodeIntValue(intValue));

    this.intValue = intValue;
  }



  /**
   * Creates a new ASN.1 enumerated element with the specified BER type and the
   * provided int value.
   *
   * @param  type      The BER type to use for this element.
   * @param  intValue  The int value to use for this element.
   */
  public ASN1Enumerated(final byte type, final int intValue)
  {
    super(type, ASN1Integer.encodeIntValue(intValue));

    this.intValue = intValue;
  }



  /**
   * Creates a new ASN.1 enumerated element with the specified BER type and the
   * provided int and pre-encoded values.
   *
   * @param  type      The BER type to use for this element.
   * @param  intValue  The int value to use for this element.
   * @param  value     The pre-encoded value to use for this element.
   */
  private ASN1Enumerated(final byte type, final int intValue,
                         @NonNull final byte[] value)
  {
    super(type, value);

    this.intValue = intValue;
  }



  /**
   * Retrieves the int value for this element.
   *
   * @return  The int value for this element.
   */
  public int intValue()
  {
    return intValue;
  }



  /**
   * Decodes the contents of the provided byte array as an enumerated element.
   *
   * @param  elementBytes  The byte array to decode as an ASN.1 enumerated
   *                       element.
   *
   * @return  The decoded ASN.1 enumerated element.
   *
   * @throws  ASN1Exception  If the provided array cannot be decoded as an
   *                         enumerated element.
   */
  @NonNull()
  public static ASN1Enumerated decodeAsEnumerated(
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

      final byte[] value = new byte[length];
      System.arraycopy(elementBytes, valueStartPos, value, 0, length);

      int intValue;
      switch (value.length)
      {
        case 1:
          intValue = (value[0] & 0xFF);
          if ((value[0] & 0x80) != 0x00)
          {
            intValue |= 0xFFFFFF00;
          }
          break;

        case 2:
          intValue = ((value[0] & 0xFF) << 8) | (value[1] & 0xFF);
          if ((value[0] & 0x80) != 0x00)
          {
            intValue |= 0xFFFF0000;
          }
          break;

        case 3:
          intValue = ((value[0] & 0xFF) << 16) | ((value[1] & 0xFF) << 8) |
                     (value[2] & 0xFF);
          if ((value[0] & 0x80) != 0x00)
          {
            intValue |= 0xFF000000;
          }
          break;

        case 4:
          intValue = ((value[0] & 0xFF) << 24) | ((value[1] & 0xFF) << 16) |
                     ((value[2] & 0xFF) << 8) | (value[3] & 0xFF);
          break;

        default:
          throw new ASN1Exception(ERR_ENUMERATED_INVALID_LENGTH.get(
                                       value.length));
      }

      return new ASN1Enumerated(elementBytes[0], intValue, value);
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
   * Decodes the provided ASN.1 element as an enumerated element.
   *
   * @param  element  The ASN.1 element to be decoded.
   *
   * @return  The decoded ASN.1 enumerated element.
   *
   * @throws  ASN1Exception  If the provided element cannot be decoded as an
   *                         enumerated element.
   */
  @NonNull()
  public static ASN1Enumerated decodeAsEnumerated(
                                    @NonNull final ASN1Element element)
         throws ASN1Exception
  {
    int intValue;
    final byte[] value = element.getValue();
    switch (value.length)
    {
      case 1:
        intValue = (value[0] & 0xFF);
        if ((value[0] & 0x80) != 0x00)
        {
          intValue |= 0xFFFFFF00;
        }
        break;

      case 2:
        intValue = ((value[0] & 0xFF) << 8) | (value[1] & 0xFF);
        if ((value[0] & 0x80) != 0x00)
        {
          intValue |= 0xFFFF0000;
        }
        break;

      case 3:
        intValue = ((value[0] & 0xFF) << 16) | ((value[1] & 0xFF) << 8) |
                   (value[2] & 0xFF);
        if ((value[0] & 0x80) != 0x00)
        {
          intValue |= 0xFF000000;
        }
        break;

      case 4:
        intValue = ((value[0] & 0xFF) << 24) | ((value[1] & 0xFF) << 16) |
                   ((value[2] & 0xFF) << 8) | (value[3] & 0xFF);
        break;

      default:
        throw new ASN1Exception(ERR_ENUMERATED_INVALID_LENGTH.get(
                                     value.length));
    }

    return new ASN1Enumerated(element.getType(), intValue, value);
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public void toString(@NonNull final StringBuilder buffer)
  {
    buffer.append(intValue);
  }
}
