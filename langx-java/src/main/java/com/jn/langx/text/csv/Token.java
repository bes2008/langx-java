package com.jn.langx.text.csv;

import com.jn.langx.text.lexer.TokenType;


/**
 * Internal token representation.
 * <p/>
 * It is used as contract between the lexer and the parser.
 */
final class Token {

    /**
     * length of the initial token (content-)buffer
     */
    private static final int INITIAL_TOKEN_LENGTH = 50;

    enum CsvTokenType implements TokenType {
        /**
         * Token has no valid content, i.e. is in its initialized state.
         */
        INVALID,

        /**
         * Token with content, at beginning or in the middle of a line.
         */
        TOKEN,

        /**
         * Token (which can have content) when the end of file is reached.
         */
        EOF,

        /**
         * Token with content when the end of a line is reached.
         */
        EORECORD,

        /**
         * Token is a comment line.
         */
        COMMENT
    }

    /**
     * Token type
     */
    Token.CsvTokenType type = CsvTokenType.INVALID;

    /**
     * The content buffer.
     */
    final StringBuilder content = new StringBuilder(INITIAL_TOKEN_LENGTH);

    /**
     * Token ready flag: indicates a valid token with content (ready for the parser).
     */
    boolean isReady;

    void reset() {
        content.setLength(0);
        type = CsvTokenType.INVALID;
        isReady = false;
    }

    /**
     * Eases IDE debugging.
     *
     * @return a string helpful for debugging.
     */
    @Override
    public String toString() {
        return type.name() + " [" + content.toString() + "]";
    }
}
