package com.jn.langx.text.tokenizer;


import java.util.List;

/**
 * Tokenizer break up text into individual Objects. These objects may be
 * Strings, Words, or other Objects.  A Tokenizer extends the Iterator
 * interface, but provides a lookahead operation {@code peek()}.  An
 * implementation of this interface is expected to have a constructor that
 * takes a single argument, a Reader.
 *
 * @since 5.1.0
 */
public interface Tokenizer<T> {

    /**
     * Returns all tokens of this Tokenizer as a List for convenience.
     *
     * @return A list of all the tokens
     */
    List<T> tokenize();

}
