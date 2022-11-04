package com.jn.langx.util.escape;


/**
 * An object that converts literal text into a format safe for inclusion in a particular context
 * (such as an XML document). Typically (but not always), the inverse process of "unescaping" the
 * text is performed automatically by the relevant parser.
 * <p>
 * <p>For example, an XML escaper would convert the literal string {@code "Foo<Bar>"} into {@code
 * "Foo&lt;Bar&gt;"} to prevent {@code "<Bar>"} from being confused with an XML tag. When the
 * resulting XML document is parsed, the parser API will return this text as the original literal
 * string {@code "Foo<Bar>"}.
 * <p>
 * <p>An {@code Escaper} instance is required to be stateless, and safe when used concurrently by
 * multiple threads.
 * <p>
 * <p>Because, in general, escaping operates on the code points of a string and not on its
 * individual {@code char} values, it is not safe to assume that {@code escape(s)} is equivalent to
 * {@code escape(s.substring(0, n)) + escape(s.substring(n))} for arbitrary {@code n}. This is
 * because of the possibility of splitting a surrogate pair. The only case in which it is safe to
 * escape strings and concatenate the results is if you can rule out this possibility, either by
 * splitting an existing long string into short strings adaptively around {@linkplain
 * Character#isHighSurrogate surrogate} {@linkplain Character#isLowSurrogate pairs}, or by starting
 * with short strings already known to be free of unpaired surrogates.
 * <p>
 * <p>The two primary implementations of this interface are {@link CharEscaper} and {@link
 * UnicodeEscaper}. They are heavily optimized for performance and greatly simplify the task of
 * implementing new escapers. It is strongly recommended that when implementing a new escaper you
 * extend one of these classes. If you find that you are unable to achieve the desired behavior
 * using either of these classes, please contact the Java libraries team for advice.
 * <p>
 * <p>Popular escapers are defined as constants in classes like {@link
 * com.jn.langx.text.html.HtmlEscapers} and {@link com.jn.langx.text.xml.XmlEscapers}. To create
 * your own escapers, use {@link CharEscaperBuilder}, or extend {@code CharEscaper} or {@code
 * UnicodeEscaper}.
 */
public interface Escaper {

    /**
     * Returns the escaped form of a given literal string.
     * <p>
     * <p>Note that this method may treat input characters differently depending on the specific
     * escaper implementation.
     * <p>
     * <ul>
     * <li>{@link UnicodeEscaper} handles <a href="http://en.wikipedia.org/wiki/UTF-16">UTF-16</a>
     * correctly, including surrogate character pairs. If the input is badly formed the escaper
     * should throw {@link IllegalArgumentException}.
     * <li>{@link CharEscaper} handles Java characters independently and does not verify the input
     * for well formed characters. A {@code CharEscaper} should not be used in situations where
     * input is not guaranteed to be restricted to the Basic Multilingual Plane (BMP).
     * </ul>
     *
     * @param string the literal string to be escaped
     * @return the escaped form of {@code string}
     * @throws NullPointerException     if {@code string} is null
     * @throws IllegalArgumentException if {@code string} contains badly formed UTF-16 or cannot be
     *                                  escaped for any other reason
     */
    String escape(String string);

}
