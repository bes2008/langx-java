package com.jn.langx.text.lexer;

public interface Token {
    int getType();

    int getTokenStart();

    /**
     *  @return 获取当前token的结束位置 ,不包含
     */
    int getTokenEnd();

    String getTokenText();
}
