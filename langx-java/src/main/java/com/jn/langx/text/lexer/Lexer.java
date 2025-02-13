package com.jn.langx.text.lexer;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;

interface Lexer {

    /**
     * 前进到下一个token
     */
    void next();

    /**
     * 开始词法分析
     *
     * @param buf  输入的字符序列
     * @param start 分析的起始位置
     * @param end   分析的结束位置
     */
    void start(@NonNull CharSequence buf, int start, int end);

    /**
     * 开始词法分析，默认从字符序列的开始位置到结束位置
     *
     * @param buf 输入的字符序列
     */
    void start(@NonNull CharSequence buf);

    /**
     * @return 获取当前 token 的文本
     */
    @NonNull
    String getTokenText();

    /**
     * @return 当前词法分析器的状态
     */
    int getState();

    /**
     * @return 当前 token 的类型，如果没有则返回 null
     */
    @Nullable
    int getTokenType();

    /**
     * @return 获取当前token的开始 offset,包含
     */
    int getTokenStart();

    /**
     *  @return 获取当前token的结束位置 ,不包含
     */
    int getTokenEnd();


    /**
     * @return 获取当前词法分析器的位置信息
     */
    @NonNull
    LexerPosition getCurrentPosition();

    /**
     * 恢复词法分析器到指定的位置
     *
     * @param position 指定的位置信息
     */
    void restore(@NonNull LexerPosition position);

    /**
     * @return 获取当前分析的字符序列
     */
    @NonNull
    CharSequence getBufferSequence();

    /**
     * @return 分析的字符序列的结束位置
     */
    int getBufferEnd();


}
