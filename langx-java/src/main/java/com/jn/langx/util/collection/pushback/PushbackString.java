package com.jn.langx.util.collection.pushback;

/**
 * The pushback string is used by Codecs to allow them to push decoded
 * characters back onto a string for further decoding. This is necessary to
 * detect double-encoding.
 *
 */
public class PushbackString extends AbstractPushbackSequence<Character> {

    /**
     * @param input Construct a PushbackString with the specified String.
     */
    public PushbackString(String input) {
        super(input);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.owasp.esapi.codecs.PushbackSequence#index()
     */
    @Override
    public int index() {
        return index;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.owasp.esapi.codecs.PushbackSequence#hasNext()
     */
    @Override
    public boolean hasNext() {
        if (pushback != null){
            return true;
        }
        if (input == null){
            return false;
        }
        if (input.length() == 0){
            return false;
        }
        if (index >= input.length()){
            return false;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.owasp.esapi.codecs.PushbackSequence#next()
     */
    public Character next() {
        if (pushback != null) {
            Character save = pushback;
            pushback = null;
            return save;
        }
        if (input == null){
            return null;
        }
        if (input.length() == 0){
            return null;
        }
        if (index >= input.length()){
            return null;
        }
        return Character.valueOf(input.charAt(index++));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.owasp.esapi.codecs.PushbackSequence#nextHex()
     */
    public Character nextHex() {
        Character c = next();
        if (c == null){
            return null;
        }
        if (isHexDigit(c)){
            return c;
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.owasp.esapi.codecs.PushbackSequence#nextOctal()
     */
    public Character nextOctal() {
        Character c = next();
        if (c == null){
            return null;
        }
        if (isOctalDigit(c)){
            return c;
        }
        return null;
    }

    /**
     * Returns true if the parameter character is a hexidecimal digit 0 through
     * 9, a through f, or A through F.
     *
     * @return true if it is a hexidecimal digit, false otherwise.
     */
    public static boolean isHexDigit(Character c) {
        if (c == null){
            return false;
        }
        char ch = c.charValue();
        return (ch >= '0' && ch <= '9') || (ch >= 'a' && ch <= 'f') || (ch >= 'A' && ch <= 'F');
    }

    /**
     * Returns true if the parameter character is an octal digit 0 through 7.
     *
     * @return true if it is an octal digit, false otherwise.
     */
    public static boolean isOctalDigit(Character c) {
        if (c == null){
            return false;
        }
        char ch = c.charValue();
        return ch >= '0' && ch <= '7';
    }

    /*
     * (non-Javadoc)
     *
     * @see org.owasp.esapi.codecs.PushbackSequence#peek()
     */
    public Character peek() {
        if (pushback != null){
            return pushback;
        }
        if (input == null){
            return null;
        }
        if (input.length() == 0){
            return null;
        }
        if (index >= input.length()){
            return null;
        }
        return Character.valueOf(input.charAt(index));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.owasp.esapi.codecs.PushbackSequence#peek(char)
     */
    public boolean peek(Character c) {
        if (pushback != null && pushback.charValue() == c){
            return true;
        }
        if (input == null){
            return false;
        }
        if (input.length() == 0){
            return false;
        }
        if (index >= input.length()){
            return false;
        }
        return input.charAt(index) == c;
    }

    /**
     * {@inheritDoc}
     */
    public void mark() {
        temp = pushback;
        mark = index;
    }

    /**
     * {@inheritDoc}
     */
    public void reset() {
        pushback = temp;
        index = mark;
    }

    /**
     * {@inheritDoc}
     */
    public String remainder() {
        String output = input.substring(index);
        if (pushback != null) {
            output = pushback + output;
        }
        return output;
    }
}