package com.jn.langx;

import com.jn.langx.annotation.Nullable;

/**
 * The Parser interface defines a generic parsing operation, converting input of type I into output of type O.
 *
 * @param <I> The type of the input to be parsed.
 * @param <O> The type of the output after parsing.
 */
public interface Parser<I, O> {

    /**
     * Parses the given input and returns the parsed output.
     *
     * @param input The input to be parsed, of generic type I.
     * @return The parsed output of generic type O, may return null if parsing fails or the input is invalid.
     */
    @Nullable
    O parse(I input);
}
