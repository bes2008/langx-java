package com.jn.langx.text.lexer;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;

public abstract class Lexer {

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
        if (getBufferSequence().subSequence(getTokenStart(), getTokenEnd()) == null) {
            throw new NullPointerException();
        }
        return getBufferSequence().subSequence(getTokenStart(), getTokenEnd());
    }

    @NonNull
    public String getTokenText() {
        if (getTokenSequence().toString() == null)
            throw new NullPointerException();
        return getTokenSequence().toString();
    }

    public abstract int getState();

    @Nullable
    public abstract TokenType getTokenType();

    public abstract int getTokenStart();

    public abstract int getTokenEnd();

    /**
     * 前进
     */
    public abstract void advance();

    @NonNull
    public abstract LexerPosition getCurrentPosition();

    public abstract void restore(@NonNull LexerPosition position);

    @NonNull
    public abstract CharSequence getBufferSequence();

    public abstract int getBufferEnd();


}