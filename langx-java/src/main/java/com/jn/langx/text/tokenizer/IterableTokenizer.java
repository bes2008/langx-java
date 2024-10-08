package com.jn.langx.text.tokenizer;

import java.util.Iterator;

/**
 * Tokenizer break up text into individual Objects. These objects may be
 * Strings, Words, or other Objects.  A Tokenizer extends the Iterator
 * interface, but provides a lookahead operation {@code peek()}.  An
 * implementation of this interface is expected to have a constructor that
 * takes a single argument, a Reader.
 *
 * @since 5.1.0
 */
public interface IterableTokenizer<T> extends Tokenizer<T>, Iterator<T> {
    /**
     * Returns the next token, without removing it, from the Tokenizer, so
     * that the same token will be again returned on the next call to
     * next() or peek().
     *
     * @return the next token in the token stream.
     * @throws java.util.NoSuchElementException If the token stream has no more tokens.
     */
    T peek();
}
