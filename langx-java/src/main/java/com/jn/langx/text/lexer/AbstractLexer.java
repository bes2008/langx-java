package com.jn.langx.text.lexer;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

public abstract class AbstractLexer implements Lexer {

    protected abstract void startInternal(@NonNull CharSequence buf, int startOffset, int endOffset, int initialState);

    private void startMeasured(@NonNull CharSequence buf, int startOffset, int endOffset, int initialState) {
        startInternal(buf, startOffset, endOffset, initialState);
    }

    public final void start(@NonNull CharSequence buf, int start, int end) {
        Preconditions.checkNotNullArgument(buf, "buf");
        startMeasured(buf, start, end, 0);
    }

    public final void start(@NonNull CharSequence buf) {
        Preconditions.checkNotNullArgument(buf, "buf");
        startMeasured(buf, 0, buf.length(), 0);
    }

    @NonNull
    public CharSequence getTokenSequence() {
        return getBufferSequence().subSequence(getTokenStart(), getTokenEnd());
    }

    @NonNull
    public LexerPosition getCurrentPosition() {
        int offset = getTokenStart();
        int intState = getState();
        return new LexerPositionImpl(offset, intState);
    }

    public void restore(@NonNull LexerPosition position) {
        Preconditions.checkNotNullArgument(position, "position");
        startInternal(getBufferSequence(), position.getOffset(), getBufferEnd(), position.getState());
    }
}
