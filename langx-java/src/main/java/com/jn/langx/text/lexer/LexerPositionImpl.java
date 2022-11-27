package com.jn.langx.text.lexer;
class LexerPositionImpl implements LexerPosition {
    private final int myOffset;

    private final int myState;

    LexerPositionImpl(int offset, int state) {
        this.myOffset = offset;
        this.myState = state;
    }

    public int getOffset() {
        return this.myOffset;
    }

    public int getState() {
        return this.myState;
    }
}
