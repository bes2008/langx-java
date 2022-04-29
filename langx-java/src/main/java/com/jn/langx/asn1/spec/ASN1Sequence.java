package com.jn.langx.asn1.spec;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotMutable;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.asn1.bytestring.ByteStringBuffer;

import java.util.ArrayList;
import java.util.Collection;

import static  com.jn.langx.asn1.spec.ASN1Messages.*;



/**
 * This class provides an ASN.1 sequence element, which is used to hold an
 * ordered set of zero or more other elements (potentially including additional
 * "envelope" element types like other sequences and/or sets).
 */
@NotMutable()
public final class ASN1Sequence
       extends ASN1Element
{
  /**
   * The serial version UID for this serializable class.
   */
  private static final long serialVersionUID = 7294248008273774906L;



  /*
   * NOTE:  This class uses lazy initialization for the encoded value.  The
   * encoded value should only be needed by the getValue() method, which is used
   * by ASN1Element.encode().  Even though this class is externally immutable,
   * that does not by itself make it completely threadsafe, because weirdness in
   * the Java memory model could allow the assignment to be performed out of
   * order.  By passing the value through a volatile variable any time the value
   * is set other than in the constructor (which will always be safe) we ensure
   * that this reordering cannot happen.
   *
   * In the majority of cases, passing the value through assignments to
   * valueBytes through a volatile variable is much faster than declaring
   * valueBytes itself to be volatile because a volatile variable cannot be held
   * in CPU caches or registers and must only be accessed from memory visible to
   * all threads.  Since the value may be read much more often than it is
   * written, passing it through a volatile variable rather than making it
   * volatile directly can help avoid that penalty when possible.
   */



  // The set of ASN.1 elements contained in this sequence.
  @NonNull private final ASN1Element[] elements;

  // The encoded representation of the value, if available.
  @Nullable private byte[] encodedValue;

  // A volatile variable used to guard publishing the encodedValue array.  See
  // the note above to explain why this is needed.
  @Nullable
  private volatile byte[] encodedValueGuard;



  /**
   * Creates a new ASN.1 sequence with the default BER type and no encapsulated
   * elements.
   */
  public ASN1Sequence()
  {
    super(ASN1Constants.UNIVERSAL_SEQUENCE_TYPE);

    elements     = ASN1Constants.NO_ELEMENTS;
    encodedValue = ASN1Constants.NO_VALUE;
  }



  /**
   * Creates a new ASN.1 sequence with the specified BER type and no
   * encapsulated elements.
   *
   * @param  type  The BER type to use for this element.
   */
  public ASN1Sequence(final byte type)
  {
    super(type);

    elements     = ASN1Constants.NO_ELEMENTS;
    encodedValue = ASN1Constants.NO_VALUE;
  }



  /**
   * Creates a new ASN.1 sequence with the default BER type and the provided set
   * of elements.
   *
   * @param  elements  The set of elements to include in this sequence.
   */
  public ASN1Sequence(@Nullable final ASN1Element... elements)
  {
    super(ASN1Constants.UNIVERSAL_SEQUENCE_TYPE);

    if (elements == null)
    {
      this.elements = ASN1Constants.NO_ELEMENTS;
    }
    else
    {
      this.elements = elements;
    }

    encodedValue = null;
  }



  /**
   * Creates a new ASN.1 sequence with the default BER type and the provided set
   * of elements.
   *
   * @param  elements  The set of elements to include in this sequence.
   */
  public ASN1Sequence(
              @Nullable final Collection<? extends ASN1Element> elements)
  {
    super(ASN1Constants.UNIVERSAL_SEQUENCE_TYPE);

    if ((elements == null) || elements.isEmpty())
    {
      this.elements = ASN1Constants.NO_ELEMENTS;
    }
    else
    {
      this.elements = new ASN1Element[elements.size()];
      elements.toArray(this.elements);
    }

    encodedValue = null;
  }



  /**
   * Creates a new ASN.1 sequence with the specified BER type and the provided
   * set of elements.
   *
   * @param  type      The BER type to use for this element.
   * @param  elements  The set of elements to include in this sequence.
   */
  public ASN1Sequence(final byte type,
                      @Nullable final ASN1Element... elements)
  {
    super(type);

    if (elements == null)
    {
      this.elements = ASN1Constants.NO_ELEMENTS;
    }
    else
    {
      this.elements = elements;
    }

    encodedValue = null;
  }



  /**
   * Creates a new ASN.1 sequence with the specified BER type and the provided
   * set of elements.
   *
   * @param  type      The BER type to use for this element.
   * @param  elements  The set of elements to include in this sequence.
   */
  public ASN1Sequence(final byte type,
              @Nullable final Collection<? extends ASN1Element> elements)
  {
    super(type);

    if ((elements == null) || elements.isEmpty())
    {
      this.elements = ASN1Constants.NO_ELEMENTS;
    }
    else
    {
      this.elements = new ASN1Element[elements.size()];
      elements.toArray(this.elements);
    }

    encodedValue = null;
  }



  /**
   * Creates a new ASN.1 sequence with the specified type, set of elements, and
   * encoded value.
   *
   * @param  type      The BER type to use for this element.
   * @param  elements  The set of elements to include in this sequence.
   * @param  value     The pre-encoded value for this element.
   */
  private ASN1Sequence(final byte type,
                       @NonNull final ASN1Element[] elements,
                       @NonNull final byte[] value)
  {
    super(type);

    this.elements = elements;
    encodedValue  = value;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  @NonNull()
  byte[] getValueArray()
  {
    return getValue();
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  int getValueOffset()
  {
    return 0;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public int getValueLength()
  {
    return getValue().length;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  @NonNull()
  public byte[] getValue()
  {
    if (encodedValue == null)
    {
      encodedValueGuard = encodeElements(elements);
      encodedValue = encodedValueGuard;
    }

    return encodedValue;
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public void encodeTo(@NonNull final ByteStringBuffer buffer)
  {
    buffer.append(getType());

    if (elements.length == 0)
    {
      buffer.append((byte) 0x00);
      return;
    }

    // In this case, it will likely be faster to just go ahead and append
    // encoded representations of all of the elements and insert the length
    // later once we know it.
    final int originalLength = buffer.length();
    for (final ASN1Element e : elements)
    {
      e.encodeTo(buffer);
    }

    buffer.insert(originalLength,
                  encodeLength(buffer.length() - originalLength));
  }



  /**
   * Encodes the provided set of elements to a byte array suitable for use as
   * the element value.
   *
   * @param  elements  The set of elements to be encoded.
   *
   * @return  A byte array containing the encoded elements.
   */
  @NonNull()
  static byte[] encodeElements(@Nullable final ASN1Element[] elements)
  {
    if ((elements == null) || (elements.length == 0))
    {
      return ASN1Constants.NO_VALUE;
    }

    int totalLength = 0;
    final int numElements = elements.length;
    final byte[][] encodedElements = new byte[numElements][];
    for (int i=0; i < numElements; i++)
    {
      encodedElements[i] = elements[i].encode();
      totalLength += encodedElements[i].length;
    }

    int pos = 0;
    final byte[] b = new byte[totalLength];
    for (int i=0; i < numElements; i++)
    {
      System.arraycopy(encodedElements[i], 0, b, pos,
                       encodedElements[i].length);
      pos += encodedElements[i].length;
    }

    return b;
  }



  /**
   * Retrieves the set of encapsulated elements held in this sequence.
   *
   * @return  The set of encapsulated elements held in this sequence.
   */
  @NonNull()
  public ASN1Element[] elements()
  {
    return elements;
  }



  /**
   * Decodes the contents of the provided byte array as a sequence element.
   *
   * @param  elementBytes  The byte array to decode as an ASN.1 sequence
   *                       element.
   *
   * @return  The decoded ASN.1 sequence element.
   *
   * @throws  ASN1Exception  If the provided array cannot be decoded as a
   *                         sequence element.
   */
  @NonNull()
  public static ASN1Sequence decodeAsSequence(
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

      int numElements = 0;
      final ArrayList<ASN1Element> elementList = new ArrayList<ASN1Element>(5);
      try
      {
        int pos = 0;
        while (pos < value.length)
        {
          final byte type = value[pos++];

          final byte firstLengthByte = value[pos++];
          int l = (firstLengthByte & 0x7F);
          if (l != firstLengthByte)
          {
            final int numLengthBytes = l;
            l = 0;
            for (int i=0; i < numLengthBytes; i++)
            {
              l <<= 8;
              l |= (value[pos++] & 0xFF);
            }
          }

          final int posPlusLength = pos + l;
          if ((l < 0) || (posPlusLength < 0) || (posPlusLength > value.length))
          {
            throw new ASN1Exception(
                 ERR_SEQUENCE_BYTES_DECODE_LENGTH_EXCEEDS_AVAILABLE.get());
          }

          elementList.add(new ASN1Element(type, value, pos, l));
          pos += l;
          numElements++;
        }
      }
      catch (final ASN1Exception ae)
      {
        throw ae;
      }
      catch (final Exception e)
      {
        //Debug.debugException(e);
        throw new ASN1Exception(ERR_SEQUENCE_BYTES_DECODE_EXCEPTION.get(e), e);
      }

      int i = 0;
      final ASN1Element[] elements = new ASN1Element[numElements];
      for (final ASN1Element e : elementList)
      {
        elements[i++] = e;
      }

      return new ASN1Sequence(elementBytes[0], elements, value);
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
   * Decodes the provided ASN.1 element as a sequence element.
   *
   * @param  element  The ASN.1 element to be decoded.
   *
   * @return  The decoded ASN.1 sequence element.
   *
   * @throws  ASN1Exception  If the provided element cannot be decoded as a
   *                         sequence element.
   */
  @NonNull()
  public static ASN1Sequence decodeAsSequence(
                                  @NonNull final ASN1Element element)
         throws ASN1Exception
  {
    int numElements = 0;
    final ArrayList<ASN1Element> elementList = new ArrayList<ASN1Element>(5);
    final byte[] value = element.getValue();

    try
    {
      int pos = 0;
      while (pos < value.length)
      {
        final byte type = value[pos++];

        final byte firstLengthByte = value[pos++];
        int length = (firstLengthByte & 0x7F);
        if (length != firstLengthByte)
        {
          final int numLengthBytes = length;
          length = 0;
          for (int i=0; i < numLengthBytes; i++)
          {
            length <<= 8;
            length |= (value[pos++] & 0xFF);
          }
        }

        final int posPlusLength = pos + length;
        if ((length < 0) || (posPlusLength < 0) ||
            (posPlusLength > value.length))
        {
          throw new ASN1Exception(
               ERR_SEQUENCE_DECODE_LENGTH_EXCEEDS_AVAILABLE.get(
                    String.valueOf(element)));
        }

        elementList.add(new ASN1Element(type, value, pos, length));
        pos += length;
        numElements++;
      }
    }
    catch (final ASN1Exception ae)
    {
      throw ae;
    }
    catch (final Exception e)
    {
      //Debug.debugException(e);
      throw new ASN1Exception(
           ERR_SEQUENCE_DECODE_EXCEPTION.get(String.valueOf(element), e), e);
    }

    int i = 0;
    final ASN1Element[] elements = new ASN1Element[numElements];
    for (final ASN1Element e : elementList)
    {
      elements[i++] = e;
    }

    return new ASN1Sequence(element.getType(), elements, value);
  }



  /**
   * {@inheritDoc}
   */
  @Override()
  public void toString(@NonNull final StringBuilder buffer)
  {
    buffer.append('[');
    for (int i=0; i < elements.length; i++)
    {
      if (i > 0)
      {
        buffer.append(',');
      }
      elements[i].toString(buffer);
    }
    buffer.append(']');
  }
}
