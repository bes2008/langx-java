package com.jn.langx.text.transform.translate;

import com.jn.langx.Transformer;
import com.jn.langx.codec.hex.Hex;
import com.jn.langx.util.Preconditions;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

/**
 * An API for translating text.
 * Its core use is to escape and unescape text. Because escaping and unescaping
 * is completely contextual, the API does not present two separate signatures.
 *
 * @since 4.8.0
 */
public abstract class CharSequenceTranslator implements Transformer<CharSequence, String> {

    /**
     * Array containing the hexadecimal alphabet.
     */
    static final char[] HEX_DIGITS = Hex.DECIMAL_TO_DIGITS_UPPER;

    /**
     * Translate a set of codepoints, represented by an int index into a CharSequence,
     * into another set of codepoints. The number of codepoints consumed must be returned,
     * and the only IOExceptions thrown must be from interacting with the Writer so that
     * the top level API may reliably ignore StringWriter IOExceptions.
     *
     * @param input CharSequence that is being translated
     * @param index int representing the current point of translation
     * @param out   Writer to translate the text to
     * @return int count of codepoints consumed
     * @throws IOException if and only if the Writer produces an IOException
     */
    protected abstract int translate(CharSequence input, int index, Writer out) throws IOException;

    /**
     * Helper for non-Writer usage.
     *
     * @param input CharSequence to be translated
     * @return String output of translation
     */
    public final String transform(final CharSequence input) {
        if (input == null) {
            return null;
        }
        try {
            final StringWriter writer = new StringWriter(input.length() * 2);
            translate(input, writer);
            return writer.toString();
        } catch (final IOException ioe) {
            // this should never ever happen while writing to a StringWriter
            throw new RuntimeException(ioe);
        }
    }

    /**
     * Translate an input onto a Writer. This is intentionally final as its algorithm is
     * tightly coupled with the abstract method of this class.
     *
     * @param input CharSequence that is being translated
     * @param out   Writer to translate the text to
     * @throws IOException if and only if the Writer produces an IOException
     */
    private void translate(final CharSequence input, final Writer out) throws IOException {
        Preconditions.checkTrue(out != null, "The Writer must not be null");
        if (input == null) {
            return;
        }
        int pos = 0;
        final int len = input.length();
        while (pos < len) {
            final int consumed = translate(input, pos, out);

            if (consumed == 0) {
                // 没有处理任何数据时，将数据写回去。
                final char c1 = input.charAt(pos);
                out.write(c1);
                pos++;
                if (Character.isHighSurrogate(c1) && pos < len) {
                    final char c2 = input.charAt(pos);
                    if (Character.isLowSurrogate(c2)) {
                        out.write(c2);
                        pos++;
                    }
                }
                continue;
            }
            // contract with translators is that they have to understand codepoints
            // and they just took care of a surrogate pair
            for (int pt = 0; pt < consumed; pt++) {
                pos += Character.charCount(Character.codePointAt(input, pos));
            }
        }
    }

    /**
     * Helper method to create a merger of this translator with another set of
     * translators. Useful in customizing the standard functionality.
     *
     * @param translators CharSequenceTranslator array of translators to merge with this one
     * @return CharSequenceTranslator merging this translator with the others
     */
    public final CharSequenceTranslator with(final CharSequenceTranslator... translators) {
        final CharSequenceTranslator[] newArray = new CharSequenceTranslator[translators.length + 1];
        newArray[0] = this;
        System.arraycopy(translators, 0, newArray, 1, translators.length);
        return new AggregateTranslator(newArray);
    }

    /**
     * <p>Returns an upper case hexadecimal {@code String} for the given
     * character.</p>
     *
     * @param codepoint The codepoint to convert.
     * @return An upper case hexadecimal {@code String}
     */
    public static String hex(final int codepoint) {
        return Integer.toHexString(codepoint).toUpperCase(Locale.ENGLISH);
    }

}
