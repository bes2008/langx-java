package com.jn.langx.util.collection.pushback;

public interface PushbackSequence<T> {

    void pushback(T c);

    /**
     * Get the current index of the PushbackString. Typically used in error messages.
     * @return The current index of the PushbackSequence.
     */
    int index();

    /**
     * Determine if this sequence has another element.
     *
     * @return True if there is another element in this sequence. False otherwise.
     */
    boolean hasNext();

    /**
     * Return the next element in the Sequence and increment the current index.
     * @return The next element in the Sequence.
     */
    T next();

    /**
     * Return the next element in the Sequence in Hex format and increment the current index.
     * @return The next element in the Sequence in Hex format (if that makes sense for this Sequence's type).
     */
    T nextHex();

    /**
     * Return the next element in the Sequence in Octal format and increment the current index.
     * @return The next element in the Sequence in Octal format (if that makes sense for this Sequence's type).
     */
    T nextOctal();

    /**
     * Return the next element in the Sequence without affecting the current index.
     * @return the next element in the Sequence.
     */
    T peek();

    /**
     * Test to see if the next element in the Sequence matches the supplied value without affecting the current index.
     * @param c The value to match against.
     * @return True if the next element matches the supplied value. False otherwise.
     */
    boolean peek(T c);

    /**
     * Mark the location of the current index.
     */
    void mark();

    /**
     * Set the index back to the last marked location.
     */
    void reset();

    /**
     * Not at all sure what this method is intended to do.  There
     * is a line in HTMLEntityCodec that said calling this method
     * is a "kludge around PushbackString..."
     * @return Return the remaining portion of the sequence, with any pushback appended to the front (if any).
     */
    String remainder();

}
