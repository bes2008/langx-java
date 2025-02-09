package com.jn.langx.text.transform.translate;


import java.io.IOException;
import java.io.Writer;

/**
 * Helper subclass to CharSequenceTranslator to remove unpaired surrogates.
 *
 * @since 4.8.0
 */
class UnicodeUnpairedSurrogateRemover extends CodePointTranslator {
    /**
     * Implementation of translate that throws out unpaired surrogates.
     * {@inheritDoc}
     */
    @Override
    public boolean translate(final int codepoint, final Writer out) throws IOException {
        // It's a surrogate. Write nothing and say we've translated.
        return codepoint >= Character.MIN_SURROGATE && codepoint <= Character.MAX_SURROGATE;
        // It's not a surrogate. Don't translate it.
    }
}

