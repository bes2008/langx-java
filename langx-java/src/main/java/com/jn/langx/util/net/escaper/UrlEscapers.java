package com.jn.langx.util.net.escaper;


import com.jn.langx.util.escape.Escaper;

/**
 * {@code Escaper} instances suitable for strings to be included in particular sections of URLs.
 * <p>
 * <p>If the resulting URLs are inserted into an HTML or XML document, they will require additional
 * escaping with
 * }.
 */
public final class UrlEscapers {
    private UrlEscapers() {
    }

    // For each xxxEscaper() method, please add links to external reference pages
    // that are considered authoritative for the behavior of that escaper.

    static final String URL_FORM_PARAMETER_OTHER_SAFE_CHARS = "-_.*";

    static final String URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS =
            "-._~" // Unreserved characters.
                    + "!$'()*,;&=" // The subdelim characters (excluding '+').
                    + "@:"; // The gendelim characters permitted in paths.

    /**
     * Returns an {@link Escaper} instance that escapes strings so they can be safely included in <a
     * href="https://goo.gl/MplK6I">URL form parameter names and values</a>. Escaping is performed
     * with the UTF-8 character encoding. The caller is responsible for <a
     * href="https://goo.gl/9EfkM1">replacing any unpaired carriage return or line feed characters
     * with a CR+LF pair</a> on any non-file inputs before escaping them with this escaper.
     * <p>
     * <p>When escaping a String, the following rules apply:
     * <p>
     * <ul>
     * <li>The alphanumeric characters "a" through "z", "A" through "Z" and "0" through "9" remain
     * the same.
     * <li>The special characters ".", "-", "*", and "_" remain the same.
     * <li>The space character " " is converted into a plus sign "+".
     * <li>All other characters are converted into one or more bytes using UTF-8 encoding and each
     * byte is then represented by the 3-character string "%XY", where "XY" is the two-digit,
     * uppercase, hexadecimal representation of the byte value.
     * </ul>
     * <p>
     * <p>This escaper is suitable for escaping parameter names and values even when <a
     * href="https://goo.gl/utn6M">using the non-standard semicolon</a>, rather than the ampersand, as
     * a parameter delimiter. Nevertheless, we recommend using the ampersand unless you must
     * interoperate with systems that require semicolons.
     * <p>
     * <p><b>Note:</b> Unlike other escapers, URL escapers produce <a
     * href="https://url.spec.whatwg.org/#percent-encode">uppercase</a> hexadecimal sequences.
     */
    public static Escaper urlFormParameterEscaper() {
        return URL_FORM_PARAMETER_ESCAPER;
    }

    private static final Escaper URL_FORM_PARAMETER_ESCAPER =
            new PercentEscaper(URL_FORM_PARAMETER_OTHER_SAFE_CHARS, true);

    /**
     * Returns an {@link Escaper} instance that escapes strings so they can be safely included in <a
     * href="https://goo.gl/m2MIf0">URL path segments</a>. The returned escaper escapes all non-ASCII
     * characters, even though <a href="https://goo.gl/e7E0In">many of these are accepted in modern
     * URLs</a>. (<a href="https://goo.gl/jfVxXW">If the escaper were to leave these characters
     * unescaped, they would be escaped by the consumer at parse time, anyway.</a>) Additionally, the
     * escaper escapes the slash character ("/"). While slashes are acceptable in URL paths, they are
     * considered by the specification to be separators between "path segments." This implies that, if
     * you wish for your path to contain slashes, you must escape each segment separately and then
     * join them.
     * <p>
     * <p>When escaping a String, the following rules apply:
     * <p>
     * <ul>
     * <li>The alphanumeric characters "a" through "z", "A" through "Z" and "0" through "9" remain
     * the same.
     * <li>The unreserved characters ".", "-", "~", and "_" remain the same.
     * <li>The general delimiters "@" and ":" remain the same.
     * <li>The subdelimiters "!", "$", "&amp;", "'", "(", ")", "*", "+", ",", ";", and "=" remain
     * the same.
     * <li>The space character " " is converted into %20.
     * <li>All other characters are converted into one or more bytes using UTF-8 encoding and each
     * byte is then represented by the 3-character string "%XY", where "XY" is the two-digit,
     * uppercase, hexadecimal representation of the byte value.
     * </ul>
     * <p>
     * <p><b>Note:</b> Unlike other escapers, URL escapers produce <a
     * href="https://url.spec.whatwg.org/#percent-encode">uppercase</a> hexadecimal sequences.
     */
    public static Escaper urlPathSegmentEscaper() {
        return URL_PATH_SEGMENT_ESCAPER;
    }

    private static final Escaper URL_PATH_SEGMENT_ESCAPER =
            new PercentEscaper(URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS + "+", false);

    /**
     * Returns an {@link Escaper} instance that escapes strings so they can be safely included in a <a
     * href="https://goo.gl/xXEq4p">URL fragment</a>. The returned escaper escapes all non-ASCII
     * characters, even though <a href="https://goo.gl/e7E0In">many of these are accepted in modern
     * URLs</a>.
     * <p>
     * <p>When escaping a String, the following rules apply:
     * <p>
     * <ul>
     * <li>The alphanumeric characters "a" through "z", "A" through "Z" and "0" through "9" remain
     * the same.
     * <li>The unreserved characters ".", "-", "~", and "_" remain the same.
     * <li>The general delimiters "@" and ":" remain the same.
     * <li>The subdelimiters "!", "$", "&amp;", "'", "(", ")", "*", "+", ",", ";", and "=" remain
     * the same.
     * <li>The space character " " is converted into %20.
     * <li>Fragments allow unescaped "/" and "?", so they remain the same.
     * <li>All other characters are converted into one or more bytes using UTF-8 encoding and each
     * byte is then represented by the 3-character string "%XY", where "XY" is the two-digit,
     * uppercase, hexadecimal representation of the byte value.
     * </ul>
     * <p>
     * <p><b>Note:</b> Unlike other escapers, URL escapers produce <a
     * href="https://url.spec.whatwg.org/#percent-encode">uppercase</a> hexadecimal sequences.
     */
    public static Escaper urlFragmentEscaper() {
        return URL_FRAGMENT_ESCAPER;
    }

    private static final Escaper URL_FRAGMENT_ESCAPER =
            new PercentEscaper(URL_PATH_OTHER_SAFE_CHARS_LACKING_PLUS + "+/?", false);
}
