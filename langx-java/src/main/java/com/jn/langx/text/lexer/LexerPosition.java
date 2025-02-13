package com.jn.langx.text.lexer;


/**
 * LexerPosition接口定义了词法分析器位置的规范
 * 它提供了获取当前解析位置的偏移量和状态的方法
 */
public interface LexerPosition {
    /**
     * 获取当前解析位置的偏移量
     * 偏移量表示从解析开始到当前位置的字符数
     *
     * @return 当前解析位置的偏移量
     */
    int getOffset();

    /**
     * 获取当前解析状态
     * 状态可以用来表示解析器当前所处的状态，比如正常状态、注释状态等
     *
     * @return 当前解析状态
     */
    int getState();
}
