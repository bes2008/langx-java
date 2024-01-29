package com.jn.langx.text.csv;

/**
 * Constants for this package.
 */
final class CsvConstants {
    private CsvConstants(){

    }

    /**
     * Starts a comment, the remainder of the line is the comment.
     */
    static final char COMMENT = '#';

    /**
     * The end of stream symbol
     */
    static final int END_OF_STREAM = -1;
    /**
     * Undefined state for the lookahead char
     */
    static final int UNDEFINED = -2;


}
