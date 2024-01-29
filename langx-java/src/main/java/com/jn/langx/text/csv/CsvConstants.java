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
     * Unicode line separator.
     */
    static final String LINE_SEPARATOR = "\u2028";

    /**
     * Unicode next line.
     */
    static final String NEXT_LINE = "\u0085";

    /**
     * Unicode paragraph separator.
     */
    static final String PARAGRAPH_SEPARATOR = "\u2029";

    static final char PIPE = '|';

    /**
     * ASCII record separator
     */
    static final char RS = 30;

    /**
     * Undefined state for the lookahead char
     */
    static final int UNDEFINED = -2;

    /**
     * ASCII unit separator
     */
    static final char US = 31;

}
