package com.jn.langx.text.transform.translate;


/**
 * Translates codepoints to their Unicode escaped value suitable for Java source.
 *
 * @since 4.8.0
 */
class JavaUnicodeEscaper extends UnicodeEscaper {

    /**
     * <p>
     * Constructs a {@code JavaUnicodeEscaper} above the specified value (exclusive).
     * </p>
     *
     * @param codepoint
     *            above which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper above(final int codepoint) {
        return outsideOf(0, codepoint);
    }

    /**
     * <p>
     * Constructs a {@code JavaUnicodeEscaper} below the specified value (exclusive).
     * </p>
     *
     * @param codepoint
     *            below which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper below(final int codepoint) {
        return outsideOf(codepoint, Integer.MAX_VALUE);
    }

    /**
     * <p>
     * Constructs a {@code JavaUnicodeEscaper} between the specified values (inclusive).
     * </p>
     *
     * @param codepointLow
     *            above which to escape
     * @param codepointHigh
     *            below which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper between(final int codepointLow, final int codepointHigh) {
        return new JavaUnicodeEscaper(codepointLow, codepointHigh, true);
    }

    /**
     * <p>
     * Constructs a {@code JavaUnicodeEscaper} outside of the specified values (exclusive).
     * </p>
     *
     * @param codepointLow
     *            below which to escape
     * @param codepointHigh
     *            above which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static JavaUnicodeEscaper outsideOf(final int codepointLow, final int codepointHigh) {
        return new JavaUnicodeEscaper(codepointLow, codepointHigh, false);
    }

    /**
     * <p>
     * Constructs a {@code JavaUnicodeEscaper} for the specified range. This is the underlying method for the
     * other constructors/builders. The {@code below} and {@code above} boundaries are inclusive when
     * {@code between} is {@code true} and exclusive when it is {@code false}.
     * </p>
     *
     * @param below
     *            int value representing the lowest codepoint boundary
     * @param above
     *            int value representing the highest codepoint boundary
     * @param between
     *            whether to escape between the boundaries or outside them
     */
    public JavaUnicodeEscaper(final int below, final int above, final boolean between) {
        super(below, above, between);
    }

    /**
     * Converts the given codepoint to a hex string of the form {@code "\\uXXXX\\uXXXX"}.
     *
     * @param codepoint
     *            a Unicode code point
     * @return The hex string for the given codepoint
     */
    @Override
    protected String toUtf16Escape(final int codepoint) {
        final char[] surrogatePair = Character.toChars(codepoint);
        return "\\u" + hex(surrogatePair[0]) + "\\u" + hex(surrogatePair[1]);
    }

}
