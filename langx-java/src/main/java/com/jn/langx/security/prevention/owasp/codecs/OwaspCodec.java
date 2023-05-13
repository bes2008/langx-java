package com.jn.langx.security.prevention.owasp.codecs;


import com.jn.langx.util.collection.pushback.PushbackSequence;

/**
 * The Codec interface defines a set of methods for encoding and decoding application level encoding schemes,
 * such as HTML entity encoding and percent encoding (aka URL encoding). Codecs are used in output encoding
 * and canonicalization.  The design of these codecs allows for character-by-character decoding, which is
 * necessary to detect double-encoding and the use of multiple encoding schemes, both of which are techniques
 * used by attackers to bypass validation and bury encoded attacks in data.
 *
 */
public interface OwaspCodec<T> {
    /**
     * Encode a String so that it can be safely used in a specific context.
     *
     * @param input String
     *         the String to encode
     * @return the encoded String
     */
    String encode(char[] immune, String input);

    /**
     * Default implementation that should be overridden in specific codecs.
     *
     * @param immune
     *         array of chars to NOT encode.  Use with caution.
     * @param c
     *         the Character to encode
     * @return
     *         the encoded Character
     */
    String encodeCharacter(char[] immune, Character c);

    /**
     * Default codepoint implementation that should be overridden in specific codecs.
     *
     * @param codePoint int
     *         the integer to encode
     * @return
     *         the encoded Character
     */
    String encodeCharacter(char[] immune, int codePoint);

    /**
     * Decode a String that was encoded using the encode method in this Class
     *
     * @param input
     *         the String to decode
     * @return
     *        the decoded String
     */
    String decode(String input);

    /**
     * Returns the decoded version of the next character from the input string and advances the
     * current character in the {@code PushbackSequence}.  If the current character is not encoded, this
     * method <i>MUST</i> reset the {@code PushbackString}.
     *
     * @param input    the Character to decode
     *
     * @return the decoded Character
     */
    T decodeCharacter(PushbackSequence<T> input);

    /**
     * Lookup the hex value of any character that is not alphanumeric.
     * @param c The character to lookup.
     * @return return null if alphanumeric or the character code in hex.
     */
    String getHexForNonAlphanumeric(char c);

    /**
     * Lookup the hex value of any character that is not alphanumeric.
     * @param c The character to lookup.
     * @return return null if alphanumeric or the character code in hex.
     */
    String getHexForNonAlphanumeric(int c);

    /**
     * Convert the {@code char} parameter to its octal representation.
     * @param c the character for which to return the new representation.
     * @return the octal representation.
     */
    String toOctal(char c);

    /**
     * Convert the {@code char} parameter to its hexadecimal representation.
     * @param c the character for which to return the new representation.
     * @return the hexadecimal representation.
     */
    String toHex(char c);

    /**
     * Convert the {@code int} parameter to its hexadecimal representation.
     * @param c the character for which to return the new representation.
     * @return the hexadecimal representation.
     */
    String toHex(int c);

    /**
     * Utility to search a char[] for a specific char.
     *
     * @return True if the supplied array contains the specified character. False otherwise.
     */
    boolean containsCharacter(char c, char[] array);

}
