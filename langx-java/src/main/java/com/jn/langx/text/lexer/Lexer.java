package com.jn.langx.text.lexer;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

interface Lexer {

    void start(@NonNull CharSequence buf, int start, int end);

    void start(@NonNull CharSequence buf);

    @NonNull
    CharSequence getTokenSequence();

    int getState();

    @Nullable
    TokenType getTokenType();

    int getTokenStart();

    int getTokenEnd();

    /**
     * 前进
     */
    void advance();

    @NonNull
    LexerPosition getCurrentPosition();

    // 重来
    void restore(@NonNull LexerPosition position);

    @NonNull
    CharSequence getBufferSequence();

    int getBufferEnd();


}