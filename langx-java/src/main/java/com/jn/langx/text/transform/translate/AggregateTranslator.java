package com.jn.langx.text.transform.translate;


import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes a sequence of translators one after the other. Execution ends whenever
 * the first translator consumes codepoints from the input.
 *
 * @since 4.8.0
 */
class AggregateTranslator extends CharSequenceTranslator {

    /**
     * Translator list.
     */
    private final List<CharSequenceTranslator> translators = new ArrayList<CharSequenceTranslator>();

    /**
     * Specify the translators to be used at creation time.
     *
     * @param translators CharSequenceTranslator array to aggregate
     */
    public AggregateTranslator(final CharSequenceTranslator... translators) {
        if (translators != null) {
            for (final CharSequenceTranslator translator : translators) {
                if (translator != null) {
                    this.translators.add(translator);
                }
            }
        }
    }

    /**
     * The first translator to consume codepoints from the input is the 'winner'.
     * Execution stops with the number of consumed codepoints being returned.
     * {@inheritDoc}
     */
    @Override
    public int translate(final CharSequence input, final int index, final Writer out) throws IOException {
        for (final CharSequenceTranslator translator : translators) {
            final int consumed = translator.translate(input, index, out);
            if (consumed != 0) {
                return consumed;
            }
        }
        return 0;
    }

}
