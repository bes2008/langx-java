package com.jn.langx.text.lexer;

public class BaseToken implements Token {
    private int type;
    private int startOffset;

    private int endOffset;
    private String text;

    public BaseToken(int type, int startOffset, int endOffset, String text){
        this.type = type;
        this.startOffset=startOffset;
        this.endOffset=endOffset;
        this.text=text;
    }
    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getTokenStart() {
        return startOffset;
    }

    @Override
    public int getTokenEnd() {
        return endOffset;
    }

    @Override
    public String getTokenText() {
        return text;
    }
}
