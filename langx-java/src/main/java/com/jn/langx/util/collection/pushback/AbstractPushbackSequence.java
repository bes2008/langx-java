package com.jn.langx.util.collection.pushback;



/**
 * This Abstract class provides the generic logic for using a {@link PushbackSequence}
 * in regards to iterating strings.  The final Impl is intended for the user to supply
 * a type T such that the pushback interface can be utilized for sequences
 * of type T.  Presently this generic class is limited by the fact that
 * input is a String.
 *
 * @param <T>
 */
public abstract class AbstractPushbackSequence<T> implements PushbackSequence<T> {
    protected String input;
    protected T pushback;
    protected T temp;
    protected int index = 0;
    protected int mark = 0;

    protected AbstractPushbackSequence(String input) {
        this.input = input;
    }

    /**
     * {@inheritDoc}
     */
    public void pushback(T c) {
        pushback = c;
    }

    /**
     * {@inheritDoc}
     */
    public int index() {
        return index;
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNext() {
        if (pushback != null)
            return true;
        if (input == null)
            return false;
        if (input.length() == 0)
            return false;
        return index < input.length();
    }
}
