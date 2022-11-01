package com.jn.langx.text.tokenizer;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


/**
 * An abstract tokenizer. A tokenizer extending AbstractTokenizer need only
 * implement the {@code getNext()} method. This implementation does not
 * allow null tokens, since
 * null is used in the protected nextToken field to signify that no more
 * tokens are available.
 */

public abstract class AbstractTokenizer<T> implements IterableTokenizer<T> {

    protected T nextToken;

    /**
     * Internally fetches the next token.
     *
     * @return the next token in the token stream, or null if none exists.
     */
    protected abstract T getNext();

    /**
     * Returns the next token from this Tokenizer.
     *
     * @return the next token in the token stream.
     * @throws java.util.NoSuchElementException if the token stream has no more tokens.
     */
    @Override
    public T next() {
        if (nextToken == null) {
            nextToken = getNext();
        }
        T result = nextToken;
        nextToken = null;
        if (result == null) {
            throw new NoSuchElementException();
        }
        return result;
    }

    /**
     * Returns {@code true} if this Tokenizer has more elements.
     */
    @Override
    public boolean hasNext() {
        if (nextToken == null) {
            nextToken = getNext();
        }
        return nextToken != null;
    }

    /**
     * This is an optional operation, by default not supported.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * This is an optional operation, by default supported.
     *
     * @return The next token in the token stream.
     * @throws java.util.NoSuchElementException if the token stream has no more tokens.
     */
    @Override
    public T peek() {
        if (nextToken == null) {
            nextToken = getNext();
        }
        if (nextToken == null) {
            throw new NoSuchElementException();
        }
        return nextToken;
    }

    // Assume that the text we are being asked to tokenize is usually more than 10 tokens; save 5 reallocations
    private static final int DEFAULT_TOKENIZE_LIST_SIZE = 64;

    /**
     * Returns text as a List of tokens.
     *
     * @return A list of all tokens remaining in the underlying Reader
     */
    @Override
    public List<T> tokenize() {
        ArrayList<T> result = new ArrayList<T>(DEFAULT_TOKENIZE_LIST_SIZE);
        while (hasNext()) {
            result.add(next());
        }
        if (result.size() <= DEFAULT_TOKENIZE_LIST_SIZE / 4) {
            result.trimToSize();
        }
        return result;
    }

}
