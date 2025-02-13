package com.jn.langx.text.lexer;

/**
 * Token接口定义了文本解析中token的基本操作
 * 一个token代表文本中具有特定意义的字符序列，如关键字、标识符、操作符等
 */
public interface Token {
    /**
     * 获取token的类型
     *
     * @return token的类型，通常是一个整数，代表在语法规则中的特定含义
     */
    int getType();

    /**
     * 获取token的起始位置
     *
     * @return token在文本中的起始位置，通常以字符数表示，从0开始计数
     */
    int getTokenStart();

    /**
     * 获取当前token的结束位置 ,不包含
     *
     * @return token在文本中的结束位置，通常以字符数表示，不包括该位置上的字符
     */
    int getTokenEnd();

    /**
     * 获取token的文本内容
     *
     * @return token所代表的文本字符串
     */
    String getTokenText();
}
