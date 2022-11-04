package com.jn.langx.text.translate;


import com.jn.langx.text.csv.CsvTranslators;
import com.jn.langx.util.Emptys;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * Escapes and unescapes {@code String}s for Java, Java Script, HTML and XML.
 * </p>
 *
 * <p>
 * This code has been adapted from Apache Commons Lang 3.5.
 * </p>
 *
 * @since 4.8.0
 */
public class StringEscapes {

    /* ESCAPE TRANSLATORS */

    /**
     * Translator object for escaping Java.
     * <p>
     * While {@link #escapeJava(String)} is the expected method of use, this
     * object allows the Java escaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator ESCAPE_JAVA;

    static {
        final Map<CharSequence, CharSequence> escapeJavaMap = new HashMap<CharSequence, CharSequence>();
        escapeJavaMap.put("\"", "\\\"");
        escapeJavaMap.put("\\", "\\\\");
        ESCAPE_JAVA = new AggregateTranslator(
                new LookupTranslator(Collections.unmodifiableMap(escapeJavaMap)),
                new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE),
                JavaUnicodeEscaper.outsideOf(32, 0x7f)
        );
    }

    /**
     * Translator object for escaping EcmaScript/JavaScript.
     * <p>
     * While {@link #escapeEcmaScript(String)} is the expected method of use, this
     * object allows the EcmaScript escaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator ESCAPE_ECMASCRIPT;

    static {
        final Map<CharSequence, CharSequence> escapeEcmaScriptMap = new HashMap<CharSequence, CharSequence>();
        escapeEcmaScriptMap.put("'", "\\'");
        escapeEcmaScriptMap.put("\"", "\\\"");
        escapeEcmaScriptMap.put("\\", "\\\\");
        escapeEcmaScriptMap.put("/", "\\/");
        ESCAPE_ECMASCRIPT = new AggregateTranslator(
                new LookupTranslator(Collections.unmodifiableMap(escapeEcmaScriptMap)),
                new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE),
                JavaUnicodeEscaper.outsideOf(32, 0x7f)
        );
    }

    /**
     * Translator object for escaping Json.
     * <p>
     * While {@link #escapeJson(String)} is the expected method of use, this
     * object allows the Json escaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator ESCAPE_JSON;

    static {
        final Map<CharSequence, CharSequence> escapeJsonMap = new HashMap<CharSequence, CharSequence>();
        escapeJsonMap.put("\"", "\\\"");
        escapeJsonMap.put("\\", "\\\\");
        escapeJsonMap.put("/", "\\/");
        ESCAPE_JSON = new AggregateTranslator(
                new LookupTranslator(Collections.unmodifiableMap(escapeJsonMap)),
                new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_ESCAPE),
                JavaUnicodeEscaper.outsideOf(32, 0x7e)
        );
    }

    /**
     * Translator object for escaping XML 1.0.
     * <p>
     * While {@link #escapeXml10(String)} is the expected method of use, this
     * object allows the XML escaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator ESCAPE_XML10;

    static {
        final Map<CharSequence, CharSequence> escapeXml10Map = new HashMap<CharSequence, CharSequence>();
        escapeXml10Map.put("\u0000", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0001", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0002", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0003", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0004", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0005", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0006", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0007", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0008", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u000b", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u000c", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u000e", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u000f", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0010", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0011", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0012", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0013", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0014", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0015", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0016", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0017", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0018", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u0019", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u001a", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u001b", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u001c", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u001d", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u001e", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\u001f", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\ufffe", Emptys.EMPTY_STRING);
        escapeXml10Map.put("\uffff", Emptys.EMPTY_STRING);
        ESCAPE_XML10 = new AggregateTranslator(
                new LookupTranslator(EntityArrays.BASIC_ESCAPE),
                new LookupTranslator(EntityArrays.APOS_ESCAPE),
                new LookupTranslator(Collections.unmodifiableMap(escapeXml10Map)),
                NumericEntityEscaper.between(0x7f, 0x84),
                NumericEntityEscaper.between(0x86, 0x9f),
                new UnicodeUnpairedSurrogateRemover()
        );
    }

    /**
     * Translator object for escaping XML 1.1.
     * <p>
     * While {@link #escapeXml11(String)} is the expected method of use, this
     * object allows the XML escaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator ESCAPE_XML11;

    static {
        final Map<CharSequence, CharSequence> escapeXml11Map = new HashMap<CharSequence, CharSequence>();
        escapeXml11Map.put("\u0000", Emptys.EMPTY_STRING);
        escapeXml11Map.put("\u000b", "&#11;");
        escapeXml11Map.put("\u000c", "&#12;");
        escapeXml11Map.put("\ufffe", Emptys.EMPTY_STRING);
        escapeXml11Map.put("\uffff", Emptys.EMPTY_STRING);
        ESCAPE_XML11 = new AggregateTranslator(
                new LookupTranslator(EntityArrays.BASIC_ESCAPE),
                new LookupTranslator(EntityArrays.APOS_ESCAPE),
                new LookupTranslator(Collections.unmodifiableMap(escapeXml11Map)),
                NumericEntityEscaper.between(0x1, 0x8),
                NumericEntityEscaper.between(0xe, 0x1f),
                NumericEntityEscaper.between(0x7f, 0x84),
                NumericEntityEscaper.between(0x86, 0x9f),
                new UnicodeUnpairedSurrogateRemover()
        );
    }

    /**
     * Translator object for escaping HTML version 3.0.
     * <p>
     * While {@link #escapeHtml3(String)} is the expected method of use, this
     * object allows the HTML escaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator ESCAPE_HTML3 =
            new AggregateTranslator(
                    new LookupTranslator(EntityArrays.BASIC_ESCAPE),
                    new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE)
            );

    /**
     * Translator object for escaping HTML version 4.0.
     * <p>
     * While {@link #escapeHtml4(String)} is the expected method of use, this
     * object allows the HTML escaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator ESCAPE_HTML4 =
            new AggregateTranslator(
                    new LookupTranslator(EntityArrays.BASIC_ESCAPE),
                    new LookupTranslator(EntityArrays.ISO8859_1_ESCAPE),
                    new LookupTranslator(EntityArrays.HTML40_EXTENDED_ESCAPE)
            );

    /**
     * Translator object for escaping individual Comma Separated Values.
     * <p>
     * While {@link #escapeCsv(String)} is the expected method of use, this
     * object allows the CSV escaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator ESCAPE_CSV = new CsvTranslators.CsvEscaper();

    /**
     * Translator object for escaping Shell command language.
     *
     * @see <a href="http://pubs.opengroup.org/onlinepubs/7908799/xcu/chap2.html">Shell Command Language</a>
     */
    public static final CharSequenceTranslator ESCAPE_XSI;

    static {
        final Map<CharSequence, CharSequence> escapeXsiMap = new HashMap<CharSequence, CharSequence>();
        escapeXsiMap.put("|", "\\|");
        escapeXsiMap.put("&", "\\&");
        escapeXsiMap.put(";", "\\;");
        escapeXsiMap.put("<", "\\<");
        escapeXsiMap.put(">", "\\>");
        escapeXsiMap.put("(", "\\(");
        escapeXsiMap.put(")", "\\)");
        escapeXsiMap.put("$", "\\$");
        escapeXsiMap.put("`", "\\`");
        escapeXsiMap.put("\\", "\\\\");
        escapeXsiMap.put("\"", "\\\"");
        escapeXsiMap.put("'", "\\'");
        escapeXsiMap.put(" ", "\\ ");
        escapeXsiMap.put("\t", "\\\t");
        escapeXsiMap.put("\r\n", Emptys.EMPTY_STRING);
        escapeXsiMap.put("\n", Emptys.EMPTY_STRING);
        escapeXsiMap.put("*", "\\*");
        escapeXsiMap.put("?", "\\?");
        escapeXsiMap.put("[", "\\[");
        escapeXsiMap.put("#", "\\#");
        escapeXsiMap.put("~", "\\~");
        escapeXsiMap.put("=", "\\=");
        escapeXsiMap.put("%", "\\%");
        ESCAPE_XSI = new LookupTranslator(
                Collections.unmodifiableMap(escapeXsiMap)
        );
    }

    /* UNESCAPE TRANSLATORS */

    /**
     * Translator object for unescaping escaped Java.
     * <p>
     * While {@link #unescapeJava(String)} is the expected method of use, this
     * object allows the Java unescaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator UNESCAPE_JAVA;

    static {
        final Map<CharSequence, CharSequence> unescapeJavaMap = new HashMap<CharSequence, CharSequence>();
        unescapeJavaMap.put("\\\\", "\\");
        unescapeJavaMap.put("\\\"", "\"");
        unescapeJavaMap.put("\\'", "'");
        unescapeJavaMap.put("\\", Emptys.EMPTY_STRING);
        UNESCAPE_JAVA = new AggregateTranslator(
                new OctalUnescaper(),     // .between('\1', '\377'),
                new UnicodeUnescaper(),
                new LookupTranslator(EntityArrays.JAVA_CTRL_CHARS_UNESCAPE),
                new LookupTranslator(Collections.unmodifiableMap(unescapeJavaMap))
        );
    }

    /**
     * Translator object for unescaping escaped EcmaScript.
     * <p>
     * While {@link #unescapeEcmaScript(String)} is the expected method of use, this
     * object allows the EcmaScript unescaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator UNESCAPE_ECMASCRIPT = UNESCAPE_JAVA;

    /**
     * Translator object for unescaping escaped Json.
     * <p>
     * While {@link #unescapeJson(String)} is the expected method of use, this
     * object allows the Json unescaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator UNESCAPE_JSON = UNESCAPE_JAVA;

    /**
     * Translator object for unescaping escaped HTML 3.0.
     * <p>
     * While {@link #unescapeHtml3(String)} is the expected method of use, this
     * object allows the HTML unescaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator UNESCAPE_HTML3 =
            new AggregateTranslator(
                    new LookupTranslator(EntityArrays.BASIC_UNESCAPE),
                    new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE),
                    new NumericEntityUnescaper()
            );

    /**
     * Translator object for unescaping escaped HTML 4.0.
     * <p>
     * While {@link #unescapeHtml4(String)} is the expected method of use, this
     * object allows the HTML unescaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator UNESCAPE_HTML4 =
            new AggregateTranslator(
                    new LookupTranslator(EntityArrays.BASIC_UNESCAPE),
                    new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE),
                    new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE),
                    new NumericEntityUnescaper()
            );

    /**
     * Translator object for unescaping escaped XML.
     * <p>
     * While {@link #unescapeXml(String)} is the expected method of use, this
     * object allows the XML unescaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator UNESCAPE_XML =
            new AggregateTranslator(
                    new LookupTranslator(EntityArrays.BASIC_UNESCAPE),
                    new LookupTranslator(EntityArrays.APOS_UNESCAPE),
                    new NumericEntityUnescaper()
            );

    /**
     * Translator object for unescaping escaped Comma Separated Value entries.
     * <p>
     * While {@link #unescapeCsv(String)} is the expected method of use, this
     * object allows the CSV unescaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator UNESCAPE_CSV = new CsvTranslators.CsvUnescaper();

    /**
     * Translator object for unescaping escaped XSI Value entries.
     * <p>
     * While {@link #unescapeXSI(String)}  is the expected method of use, this
     * object allows the XSI unescaping functionality to be used
     * as the foundation for a custom translator.
     */
    public static final CharSequenceTranslator UNESCAPE_XSI = new StringEscapes.XsiUnescaper();

    /**
     * Translator object for unescaping backslash escaped entries.
     */
    static class XsiUnescaper extends CharSequenceTranslator {

        /**
         * Escaped backslash constant.
         */
        private static final char BACKSLASH = '\\';

        @Override
        public int translate(final CharSequence input, final int index, final Writer out) throws IOException {

            if (index != 0) {
                throw new IllegalStateException("XsiUnescaper should never reach the [1] index");
            }

            final String s = input.toString();

            int segmentStart = 0;
            int searchOffset = 0;
            while (true) {
                final int pos = s.indexOf(BACKSLASH, searchOffset);
                if (pos == -1) {
                    if (segmentStart < s.length()) {
                        out.write(s.substring(segmentStart));
                    }
                    break;
                }
                if (pos > segmentStart) {
                    out.write(s.substring(segmentStart, pos));
                }
                segmentStart = pos + 1;
                searchOffset = pos + 2;
            }

            return Character.codePointCount(input, 0, input.length());
        }
    }

    /* Helper functions */

    /**
     * <p>{@code StringEscapeUtils} instances should NOT be constructed in
     * standard programming.</p>
     *
     * <p>Instead, the class should be used as:</p>
     * <pre>StringEscapeUtils.escapeJava("foo");</pre>
     *
     * <p>This constructor is public to permit tools that require a JavaBean
     * instance to operate.</p>
     */
    public StringEscapes() {
        super();
    }

    /**
     * <p>Convenience wrapper for {@link java.lang.StringBuilder} providing escape methods.</p>
     *
     * <p>Example:</p>
     * <pre>
     * new Builder(ESCAPE_HTML4)
     *      .append("&lt;p&gt;")
     *      .escape("This is paragraph 1 and special chars like &amp; get escaped.")
     *      .append("&lt;/p&gt;&lt;p&gt;")
     *      .escape("This is paragraph 2 &amp; more...")
     *      .append("&lt;/p&gt;")
     *      .toString()
     * </pre>
     */
    public static final class Builder {

        /**
         * StringBuilder to be used in the Builder class.
         */
        private final StringBuilder sb;

        /**
         * CharSequenceTranslator to be used in the Builder class.
         */
        private final CharSequenceTranslator translator;

        /**
         * Builder constructor.
         *
         * @param translator a CharSequenceTranslator.
         */
        private Builder(final CharSequenceTranslator translator) {
            this.sb = new StringBuilder();
            this.translator = translator;
        }

        /**
         * <p>Escape {@code input} according to the given {@link CharSequenceTranslator}.</p>
         *
         * @param input the String to escape
         * @return {@code this}, to enable chaining
         */
        public Builder escape(final String input) {
            sb.append(translator.transform(input));
            return this;
        }

        /**
         * Literal append, no escaping being done.
         *
         * @param input the String to append
         * @return {@code this}, to enable chaining
         */
        public Builder append(final String input) {
            sb.append(input);
            return this;
        }

        /**
         * <p>Return the escaped string.</p>
         *
         * @return The escaped string
         */
        @Override
        public String toString() {
            return sb.toString();
        }
    }

    /**
     * Get a {@link Builder}.
     *
     * @param translator the text translator
     * @return {@link Builder}
     */
    public static Builder builder(final CharSequenceTranslator translator) {
        return new Builder(translator);
    }

    // Java and JavaScript
    //--------------------------------------------------------------------------

    /**
     * <p>Escapes the characters in a {@code String} using Java String rules.</p>
     *
     * <p>Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters {@code '\\'} and
     * {@code 't'}.</p>
     *
     * <p>The only difference between Java strings and JavaScript strings
     * is that in JavaScript, a single quote and forward-slash (/) are escaped.</p>
     *
     * <p>Example:</p>
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn't say, \"Stop!\"
     * </pre>
     *
     * @param input String to escape values in, may be null
     * @return String with escaped values, {@code null} if null string input
     */
    public static final String escapeJava(final String input) {
        return ESCAPE_JAVA.transform(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using EcmaScript String rules.</p>
     * <p>Escapes any values it finds into their EcmaScript String form.
     * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters {@code '\\'} and
     * {@code 't'}.</p>
     *
     * <p>The only difference between Java strings and EcmaScript strings
     * is that in EcmaScript, a single quote and forward-slash (/) are escaped.</p>
     *
     * <p>Note that EcmaScript is best known by the JavaScript and ActionScript dialects.</p>
     *
     * <p>Example:</p>
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn\'t say, \"Stop!\"
     * </pre>
     *
     * <b>Security Note.</b> We only provide backslash escaping in this method. For example, {@code '\"'} has the output
     * {@code '\\\"'} which could result in potential issues in the case where the string being escaped is being used
     * in an HTML tag like {@code <select onmouseover="..." />}. If you wish to have more rigorous string escaping, you
     * may consider the
     * <a href="https://www.owasp.org/index.php/Category:OWASP_Enterprise_Security_API_JAVA">ESAPI Libraries</a>.
     * Further, you can view the <a href="https://github.com/esapi">ESAPI GitHub Org</a>.
     *
     * @param input String to escape values in, may be null
     * @return String with escaped values, {@code null} if null string input
     */
    public static final String escapeEcmaScript(final String input) {
        return ESCAPE_ECMASCRIPT.transform(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using Json String rules.</p>
     * <p>Escapes any values it finds into their Json String form.
     * Deals correctly with quotes and control-chars (tab, backslash, cr, ff, etc.) </p>
     *
     * <p>So a tab becomes the characters {@code '\\'} and
     * {@code 't'}.</p>
     *
     * <p>The only difference between Java strings and Json strings
     * is that in Json, forward-slash (/) is escaped.</p>
     *
     * <p>See http://www.ietf.org/rfc/rfc4627.txt for further details.</p>
     *
     * <p>Example:</p>
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He didn't say, \"Stop!\"
     * </pre>
     *
     * @param input String to escape values in, may be null
     * @return String with escaped values, {@code null} if null string input
     */
    public static final String escapeJson(final String input) {
        return ESCAPE_JSON.transform(input);
    }

    /**
     * <p>Unescapes any Java literals found in the {@code String}.
     * For example, it will turn a sequence of {@code '\'} and
     * {@code 'n'} into a newline character, unless the {@code '\'}
     * is preceded by another {@code '\'}.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     */
    public static final String unescapeJava(final String input) {
        return UNESCAPE_JAVA.transform(input);
    }

    /**
     * <p>Unescapes any EcmaScript literals found in the {@code String}.</p>
     *
     * <p>For example, it will turn a sequence of {@code '\'} and {@code 'n'}
     * into a newline character, unless the {@code '\'} is preceded by another
     * {@code '\'}.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return A new unescaped {@code String}, {@code null} if null string input
     * @see #unescapeJava(String)
     */
    public static final String unescapeEcmaScript(final String input) {
        return UNESCAPE_ECMASCRIPT.transform(input);
    }

    /**
     * <p>Unescapes any Json literals found in the {@code String}.</p>
     *
     * <p>For example, it will turn a sequence of {@code '\'} and {@code 'n'}
     * into a newline character, unless the {@code '\'} is preceded by another
     * {@code '\'}.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return A new unescaped {@code String}, {@code null} if null string input
     * @see #unescapeJava(String)
     */
    public static final String unescapeJson(final String input) {
        return UNESCAPE_JSON.transform(input);
    }

    // HTML and XML
    //--------------------------------------------------------------------------

    /**
     * <p>Escapes the characters in a {@code String} using HTML entities.</p>
     *
     * <p>
     * For example:
     * </p>
     * <p>{@code "bread" &amp; "butter"}</p>
     * becomes:
     * <p>
     * {@code &amp;quot;bread&amp;quot; &amp;amp; &amp;quot;butter&amp;quot;}.
     * </p>
     *
     * <p>Supports all known HTML 4.0 entities, including funky accents.
     * Note that the commonly used apostrophe escape character (&amp;apos;)
     * is not a legal entity and so is not supported).</p>
     *
     * @param input the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     * @see <a href="http://hotwired.lycos.com/webmonkey/reference/special_characters/">ISO Entities</a>
     * @see <a href="http://www.w3.org/TR/REC-html32#latin1">HTML 3.2 Character Entities for ISO Latin-1</a>
     * @see <a href="http://www.w3.org/TR/REC-html40/sgml/entities.html">HTML 4.0 Character entity references</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#h-5.3">HTML 4.01 Character References</a>
     * @see <a href="http://www.w3.org/TR/html401/charset.html#code-position">HTML 4.01 Code positions</a>
     */
    public static final String escapeHtml4(final String input) {
        return ESCAPE_HTML4.transform(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using HTML entities.</p>
     * <p>Supports only the HTML 3.0 entities.</p>
     *
     * @param input the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     */
    public static final String escapeHtml3(final String input) {
        return ESCAPE_HTML3.transform(input);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Unescapes a string containing entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes. Supports HTML 4.0 entities.</p>
     *
     * <p>For example, the string {@code "&lt;Fran&ccedil;ais&gt;"}
     * will become {@code "<Fran�ais>"}</p>
     *
     * <p>If an entity is unrecognized, it is left alone, and inserted
     * verbatim into the result string. e.g. {@code "&gt;&zzzz;x"} will
     * become {@code ">&zzzz;x"}.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     */
    public static final String unescapeHtml4(final String input) {
        return UNESCAPE_HTML4.transform(input);
    }

    /**
     * <p>Unescapes a string containing entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes. Supports only HTML 3.0 entities.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     */
    public static final String unescapeHtml3(final String input) {
        return UNESCAPE_HTML3.transform(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using XML entities.</p>
     *
     * <p>For example: {@code "bread" & "butter"} =&gt;
     * {@code &quot;bread&quot; &amp; &quot;butter&quot;}.
     * </p>
     *
     * <p>Note that XML 1.0 is a text-only format: it cannot represent control
     * characters or unpaired Unicode surrogate codepoints, even after escaping.
     * {@code escapeXml10} will remove characters that do not fit in the
     * following ranges:</p>
     *
     * <p>{@code #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]}</p>
     *
     * <p>Though not strictly necessary, {@code escapeXml10} will escape
     * characters in the following ranges:</p>
     *
     * <p>{@code [#x7F-#x84] | [#x86-#x9F]}</p>
     *
     * <p>The returned string can be inserted into a valid XML 1.0 or XML 1.1
     * document. If you want to allow more non-text characters in an XML 1.1
     * document, use {@link #escapeXml11(String)}.</p>
     *
     * @param input the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     * @see #unescapeXml(java.lang.String)
     */
    public static String escapeXml10(final String input) {
        return ESCAPE_XML10.transform(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using XML entities.</p>
     *
     * <p>For example: {@code "bread" & "butter"} =&gt;
     * {@code &quot;bread&quot; &amp; &quot;butter&quot;}.
     * </p>
     *
     * <p>XML 1.1 can represent certain control characters, but it cannot represent
     * the null byte or unpaired Unicode surrogate codepoints, even after escaping.
     * {@code escapeXml11} will remove characters that do not fit in the following
     * ranges:</p>
     *
     * <p>{@code [#x1-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]}</p>
     *
     * <p>{@code escapeXml11} will escape characters in the following ranges:</p>
     *
     * <p>{@code [#x1-#x8] | [#xB-#xC] | [#xE-#x1F] | [#x7F-#x84] | [#x86-#x9F]}</p>
     *
     * <p>The returned string can be inserted into a valid XML 1.1 document. Do not
     * use it for XML 1.0 documents.</p>
     *
     * @param input the {@code String} to escape, may be null
     * @return a new escaped {@code String}, {@code null} if null string input
     * @see #unescapeXml(java.lang.String)
     */
    public static String escapeXml11(final String input) {
        return ESCAPE_XML11.transform(input);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Unescapes a string containing XML entity escapes to a string
     * containing the actual Unicode characters corresponding to the
     * escapes.</p>
     *
     * <p>Supports only the five basic XML entities (gt, lt, quot, amp, apos).
     * Does not support DTDs or external entities.</p>
     *
     * <p>Note that numerical \\u Unicode codes are unescaped to their respective
     * Unicode characters. This may change in future releases.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     * @see #escapeXml10(String)
     * @see #escapeXml11(String)
     */
    public static final String unescapeXml(final String input) {
        return UNESCAPE_XML.transform(input);
    }

    //-----------------------------------------------------------------------

    /**
     * <p>Returns a {@code String} value for a CSV column enclosed in double quotes,
     * if required.</p>
     *
     * <p>If the value contains a comma, newline or double quote, then the
     * String value is returned enclosed in double quotes.</p>
     *
     * <p>Any double quote characters in the value are escaped with another double quote.</p>
     *
     * <p>If the value does not contain a comma, newline or double quote, then the
     * String value is returned unchanged.</p>
     * <p>
     * see <a href="http://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia</a> and
     * <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
     *
     * @param input the input CSV column String, may be null
     * @return The input String, enclosed in double quotes if the value contains a comma,
     * newline or double quote, {@code null} if null string input
     */
    public static final String escapeCsv(final String input) {
        return ESCAPE_CSV.transform(input);
    }

    /**
     * <p>Returns a {@code String} value for an unescaped CSV column.</p>
     *
     * <p>If the value is enclosed in double quotes, and contains a comma, newline
     * or double quote, then quotes are removed.
     * </p>
     *
     * <p>Any double quote escaped characters (a pair of double quotes) are unescaped
     * to just one double quote.</p>
     *
     * <p>If the value is not enclosed in double quotes, or is and does not contain a
     * comma, newline or double quote, then the String value is returned unchanged.</p>
     * <p>
     * see <a href="http://en.wikipedia.org/wiki/Comma-separated_values">Wikipedia</a> and
     * <a href="http://tools.ietf.org/html/rfc4180">RFC 4180</a>.
     *
     * @param input the input CSV column String, may be null
     * @return The input String, with enclosing double quotes removed and embedded double
     * quotes unescaped, {@code null} if null string input
     */
    public static final String unescapeCsv(final String input) {
        return UNESCAPE_CSV.transform(input);
    }

    /**
     * <p>Escapes the characters in a {@code String} using XSI rules.</p>
     *
     * <p><b>Beware!</b> In most cases you don't want to escape shell commands but use multi-argument
     * methods provided by {@link java.lang.ProcessBuilder} or {@link java.lang.Runtime#exec(String[])}
     * instead.</p>
     *
     * <p>Example:</p>
     * <pre>
     * input string: He didn't say, "Stop!"
     * output string: He\ didn\'t\ say,\ \"Stop!\"
     * </pre>
     *
     * @param input String to escape values in, may be null
     * @return String with escaped values, {@code null} if null string input
     * @see <a href="http://pubs.opengroup.org/onlinepubs/7908799/xcu/chap2.html">Shell Command Language</a>
     */
    public static final String escapeXSI(final String input) {
        return ESCAPE_XSI.transform(input);
    }

    /**
     * <p>Unescapes the characters in a {@code String} using XSI rules.</p>
     *
     * @param input the {@code String} to unescape, may be null
     * @return a new unescaped {@code String}, {@code null} if null string input
     * @see StringEscapes#escapeXSI(String)
     */
    public static final String unescapeXSI(final String input) {
        return UNESCAPE_XSI.transform(input);
    }

}
