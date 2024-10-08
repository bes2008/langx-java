package com.jn.langx.text.translate;


import java.io.IOException;
import java.io.Writer;

/**
 * Translates codepoints to their Unicode escaped value.
 *
 * @since 4.8.0
 */
class UnicodeEscaper extends CodePointTranslator {

    /** int value representing the lowest codepoint boundary. */
    private final int below;
    /** int value representing the highest codepoint boundary. */
    private final int above;
    /** whether to escape between the boundaries or outside them. */
    private final boolean between;

    /**
     * <p>Constructs a {@code UnicodeEscaper} for all characters.
     * </p>
     */
    public UnicodeEscaper() {
        this(0, Integer.MAX_VALUE, true);
    }

    /**
     * <p>Constructs a {@code UnicodeEscaper} for the specified range. This is
     * the underlying method for the other constructors/builders. The {@code below}
     * and {@code above} boundaries are inclusive when {@code between} is
     * {@code true} and exclusive when it is {@code false}.</p>
     *
     * @param below int value representing the lowest codepoint boundary
     * @param above int value representing the highest codepoint boundary
     * @param between whether to escape between the boundaries or outside them
     */
    protected UnicodeEscaper(final int below, final int above, final boolean between) {
        this.below = below;
        this.above = above;
        this.between = between;
    }

    /**
     * <p>Constructs a {@code UnicodeEscaper} below the specified value (exclusive).</p>
     *
     * @param codepoint below which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static UnicodeEscaper below(final int codepoint) {
        return outsideOf(codepoint, Integer.MAX_VALUE);
    }

    /**
     * <p>Constructs a {@code UnicodeEscaper} above the specified value (exclusive).</p>
     *
     * @param codepoint above which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static UnicodeEscaper above(final int codepoint) {
        return outsideOf(0, codepoint);
    }

    /**
     * <p>Constructs a {@code UnicodeEscaper} outside of the specified values (exclusive).</p>
     *
     * @param codepointLow below which to escape
     * @param codepointHigh above which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static UnicodeEscaper outsideOf(final int codepointLow, final int codepointHigh) {
        return new UnicodeEscaper(codepointLow, codepointHigh, false);
    }

    /**
     * <p>Constructs a {@code UnicodeEscaper} between the specified values (inclusive).</p>
     *
     * @param codepointLow above which to escape
     * @param codepointHigh below which to escape
     * @return The newly created {@code UnicodeEscaper} instance
     */
    public static UnicodeEscaper between(final int codepointLow, final int codepointHigh) {
        return new UnicodeEscaper(codepointLow, codepointHigh, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean translate(final int codepoint, final Writer out) throws IOException {
        if (between) {
            if (codepoint < below || codepoint > above) {
                return false;
            }
        } else {
            if (codepoint >= below && codepoint <= above) {
                return false;
            }
        }

        if (codepoint > 0xffff) {
            out.write(toUtf16Escape(codepoint));
        } else {
          out.write("\\u");
          out.write(HEX_DIGITS[(codepoint >> 12) & 15]);
          out.write(HEX_DIGITS[(codepoint >> 8) & 15]);
          out.write(HEX_DIGITS[(codepoint >> 4) & 15]);
          out.write(HEX_DIGITS[(codepoint) & 15]);
        }
        return true;
    }

    /**
     * Converts the given codepoint to a hex string of the form {@code "\\uXXXX"}.
     *
     * @param codepoint
     *            a Unicode code point
     * @return The hex string for the given codepoint
     *
     */
    protected String toUtf16Escape(final int codepoint) {
        return "\\u" + hex(codepoint);
    }
}
