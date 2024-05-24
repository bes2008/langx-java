package com.jn.langx.text.lexer;

class LexerPositionImpl implements LexerPosition {
    /**
     * token 开始偏移量
     */
    private final int offset;

    private final int state;

    LexerPositionImpl(int offset, int state) {
        this.offset = offset;
        this.state = state;
    }

    public int getOffset() {
        return this.offset;
    }

    public int getState() {
        return this.state;
    }
}
