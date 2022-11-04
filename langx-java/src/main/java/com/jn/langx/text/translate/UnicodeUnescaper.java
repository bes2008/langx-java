package com.jn.langx.text.translate;


import java.io.IOException;
import java.io.Writer;

/**
 * Translates escaped Unicode values of the form \\u+\d\d\d\d back to
 * Unicode. It supports multiple 'u' characters and will work with or
 * without the +.
 *
 * @since 4.8.0
 */
class UnicodeUnescaper extends CharSequenceTranslator {

    /**
     * {@inheritDoc}
     */
    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        if (input.charAt(index) == '\\' && index + 1 < input.length() && input.charAt(index + 1) == 'u') {
            // consume optional additional 'u' chars
            int i = 2;
            while (index + i < input.length() && input.charAt(index + i) == 'u') {
                i++;
            }

            if (index + i < input.length() && input.charAt(index + i) == '+') {
                i++;
            }

            if (index + i + 4 <= input.length()) {
                // Get 4 hex digits
                final CharSequence unicode = input.subSequence(index + i, index + i + 4);

                try {
                    final int value = Integer.parseInt(unicode.toString(), 16);
                    out.write((char) value);
                } catch (final NumberFormatException nfe) {
                    throw new IllegalArgumentException("Unable to parse unicode value: " + unicode, nfe);
                }
                return i + 4;
            }
            throw new IllegalArgumentException("Less than 4 hex digits in unicode value: '"
                    + input.subSequence(index, input.length())
                    + "' due to end of CharSequence");
        }
        return 0;
    }
}
