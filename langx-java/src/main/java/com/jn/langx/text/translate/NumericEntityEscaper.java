package com.jn.langx.text.translate;

import com.jn.langx.util.ranges.IntRange;

import java.io.IOException;
import java.io.Writer;

/**
 * Translates codepoints to their XML numeric entity escaped value.
 *
 * @since 4.8.0
 */
class NumericEntityEscaper extends CodePointTranslator {

    /** whether to escape between the boundaries or outside them. */
    private final boolean between;
    /** range from lowest codepoint to highest codepoint. */
    private final IntRange range;
    /**
     * <p>Constructs a {@code NumericEntityEscaper} for the specified range. This is
     * the underlying method for the other constructors/builders. The {@code below}
     * and {@code above} boundaries are inclusive when {@code between} is
     * {@code true} and exclusive when it is {@code false}.</p>
     *
     * @param below int value representing the lowest codepoint boundary
     * @param above int value representing the highest codepoint boundary
     * @param between whether to escape between the boundaries or outside them
     */
    private NumericEntityEscaper(final int below, final int above, final boolean between) {
        this.range = new IntRange(below, above);
        this.between = between;
    }

    /**
     * <p>Constructs a {@code NumericEntityEscaper} for all characters.</p>
     */
    public NumericEntityEscaper() {
        this(0, Integer.MAX_VALUE, true);
    }

    /**
     * <p>Constructs a {@code NumericEntityEscaper} below the specified value (exclusive).</p>
     *
     * @param codepoint below which to escape
     * @return The newly created {@code NumericEntityEscaper} instance
     */
    public static NumericEntityEscaper below(final int codepoint) {
        return outsideOf(codepoint, Integer.MAX_VALUE);
    }

    /**
     * <p>Constructs a {@code NumericEntityEscaper} above the specified value (exclusive).</p>
     *
     * @param codepoint above which to escape
     * @return The newly created {@code NumericEntityEscaper} instance
     */
    public static NumericEntityEscaper above(final int codepoint) {
        return outsideOf(0, codepoint);
    }

    /**
     * <p>Constructs a {@code NumericEntityEscaper} between the specified values (inclusive).</p>
     *
     * @param codepointLow above which to escape
     * @param codepointHigh below which to escape
     * @return The newly created {@code NumericEntityEscaper} instance
     */
    public static NumericEntityEscaper between(final int codepointLow, final int codepointHigh) {
        return new NumericEntityEscaper(codepointLow, codepointHigh, true);
    }

    /**
     * <p>Constructs a {@code NumericEntityEscaper} outside of the specified values (exclusive).</p>
     *
     * @param codepointLow below which to escape
     * @param codepointHigh above which to escape
     * @return The newly created {@code NumericEntityEscaper} instance
     */
    public static NumericEntityEscaper outsideOf(final int codepointLow, final int codepointHigh) {
        return new NumericEntityEscaper(codepointLow, codepointHigh, false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean translate(final int codepoint, final Writer out) throws IOException {
        if (this.between != this.range.contains(codepoint)) {
            return false;
        }
        out.write("&#");
        out.write(Integer.toString(codepoint, 10));
        out.write(';');
        return true;
    }
}
