package com.jn.langx.text.tokenizer;


import java.util.Iterator;
import java.util.List;

/**
 * Tokenizer break up text into individual Objects. These objects may be
 * Strings, Words, or other Objects.  A Tokenizer extends the Iterator
 * interface, but provides a lookahead operation {@code peek()}.  An
 * implementation of this interface is expected to have a constructor that
 * takes a single argument, a Reader.
 */
public interface Tokenizer<T> extends Iterator<T> {

    /**
     * Returns the next token, without removing it, from the Tokenizer, so
     * that the same token will be again returned on the next call to
     * next() or peek().
     *
     * @return the next token in the token stream.
     * @throws java.util.NoSuchElementException If the token stream has no more tokens.
     */
    T peek();

    /**
     * Returns all tokens of this Tokenizer as a List for convenience.
     *
     * @return A list of all the tokens
     */
    List<T> tokenize();

}
