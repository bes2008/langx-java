package com.jn.langx.text.lexer;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

public abstract class LexerBase extends Lexer {
    @NonNull
    public LexerPosition getCurrentPosition() {
        int offset = getTokenStart();
        int intState = getState();
        return new LexerPositionImpl(offset, intState);
    }

    public void restore(@NonNull LexerPosition position) {
        Preconditions.checkNotNullArgument(position,"position");
        startInternal(getBufferSequence(), position.getOffset(), getBufferEnd(), position.getState());
    }
}
