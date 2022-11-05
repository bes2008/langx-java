package com.jn.langx.text.csv;

import com.jn.langx.text.translate.SinglePassTranslator;
import com.jn.langx.util.Chars;
import com.jn.langx.util.Strings;

import java.io.IOException;
import java.io.Writer;


/**
 * This class holds inner classes for escaping/unescaping Comma Separated Values.
 * <p>
 * In general the use a high level API like <a href="https://commons.apache.org/proper/commons-csv/">Apache Commons
 * CSV</a> should be preferred over these low level classes.
 * </p>
 *
 * @see <a href="https://commons.apache.org/proper/commons-csv/apidocs/index.html">Apache Commons CSV</a>
 *
 * @since 4.8.0
 */
public final class CsvTranslators {

    /** Comma character. */
    private static final char CSV_DELIMITER = ',';
    /** Quote character. */
    private static final char CSV_QUOTE = '"';
    /** Quote character converted to string. */
    private static final String CSV_QUOTE_STR = String.valueOf(CSV_QUOTE);
    /** Escaped quote string. */
    private static final String CSV_ESCAPED_QUOTE_STR = CSV_QUOTE_STR + CSV_QUOTE_STR;
    /** CSV key characters in an array. */
    private static final char[] CSV_SEARCH_CHARS =
            new char[] {CSV_DELIMITER, CSV_QUOTE, Chars.CR, Chars.LF};

    /** Hidden constructor. */
    private CsvTranslators() { }

    /**
     * Translator for escaping Comma Separated Values.
     */
    public static class CsvEscaper extends SinglePassTranslator {

        @Override
       public void translateWhole(final CharSequence input, final Writer out) throws IOException {
            final String inputSting = input.toString();
            if (Strings.containsNone(inputSting, CSV_SEARCH_CHARS)) {
                out.write(inputSting);
            } else {
                // input needs quoting
                out.write(CSV_QUOTE);
                out.write(Strings.replace(inputSting, CSV_QUOTE_STR, CSV_ESCAPED_QUOTE_STR));
                out.write(CSV_QUOTE);
            }
        }
    }

    /**
     * Translator for unescaping escaped Comma Separated Value entries.
     */
    public static class CsvUnescaper extends SinglePassTranslator {

        @Override
       public void translateWhole(final CharSequence input, final Writer out) throws IOException {
            // is input not quoted?
            if (input.charAt(0) != CSV_QUOTE || input.charAt(input.length() - 1) != CSV_QUOTE) {
                out.write(input.toString());
                return;
            }

            // strip quotes
            final String quoteless = input.subSequence(1, input.length() - 1).toString();

            if (Strings.containsAny(quoteless, CSV_SEARCH_CHARS)) {
                // deal with escaped quotes; ie) ""
                out.write(Strings.replace(quoteless, CSV_ESCAPED_QUOTE_STR, CSV_QUOTE_STR));
            } else {
                out.write(quoteless);
            }
        }
    }
}
