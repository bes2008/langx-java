package com.jn.langx.text.lexer;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

interface Lexer {

    void start(@NonNull CharSequence buf, int start, int end);

    void start(@NonNull CharSequence buf);

    /**
     * @return 获取当前 token 的文本
     */
    @NonNull
    String getTokenText();

    int getState();

    @Nullable
    TokenType getTokenType();

    /**
     * @return 获取当前token的开始 offset,包含
     */
    int getTokenStart();

    /**
     *  @return 获取当前token的结束位置 ,不包含
     */
    int getTokenEnd();

    /**
     * 前进
     */
    void next();

    @NonNull
    LexerPosition getCurrentPosition();

    /**
     * 从指定的位置开始
     */
    void restore(@NonNull LexerPosition position);

    @NonNull
    CharSequence getBufferSequence();

    int getBufferEnd();


}