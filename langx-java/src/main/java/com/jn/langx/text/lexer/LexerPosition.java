package com.jn.langx.text.lexer;


public interface LexerPosition {
    /**
     * 偏移量
     */
    int getOffset();

    int getState();
}
