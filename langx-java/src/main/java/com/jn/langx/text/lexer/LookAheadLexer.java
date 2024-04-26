package com.jn.langx.text.lexer;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

public abstract class LookAheadLexer extends AbstractLexer {
    private int myTokenStart;
    private int myLastOffset;

    private int myLastState;

    private final AbstractLexer delegate;

    private final MutableRandomAccessQueue<Integer> myTypeCache;

    private final MutableRandomAccessQueue<Integer> myEndOffsetCache;

    public LookAheadLexer(@NonNull AbstractLexer delegate, int capacity) {
        Preconditions.checkNotNullArgument(delegate, "delegate");
        this.delegate = delegate;
        this.myTypeCache = new MutableRandomAccessQueue<Integer>(capacity);
        this.myEndOffsetCache = new MutableRandomAccessQueue<Integer>(capacity);
    }

    public LookAheadLexer(@NonNull AbstractLexer delegate) {
        this(delegate, 64);
    }

    public void next() {
        if (!this.myTypeCache.isEmpty()) {
            this.myTypeCache.pullFirst();
            this.myTokenStart = this.myEndOffsetCache.pullFirst();
        }
        if (this.myTypeCache.isEmpty()) {
            doLookAhead();
        }
    }

    private void doLookAhead() {
        this.myLastOffset = this.myTokenStart;
        this.myLastState = this.delegate.getState();
        addToken(this.delegate.getTokenType());
        this.delegate.next();
        assert !this.myTypeCache.isEmpty();
    }

    protected void addToken(int type) {
        addToken(this.delegate.getTokenEnd(), type);
    }

    protected void addToken(int endOffset, int type) {
        this.myTypeCache.addLast(type);
        this.myEndOffsetCache.addLast(endOffset);
    }

    @NonNull
    public CharSequence getBufferSequence() {
        if (this.delegate.getBufferSequence() == null) {
            throw new NullPointerException();
        }
        return this.delegate.getBufferSequence();
    }

    public int getBufferEnd() {
        return this.delegate.getBufferEnd();
    }

    protected int getCacheSize() {
        return this.myTypeCache.size();
    }

    protected void resetCacheSize(int size) {
        while (this.myTypeCache.size() > size) {
            this.myTypeCache.removeLast();
            this.myEndOffsetCache.removeLast();
        }
    }

    public int replaceCachedType(int index, int type) {
        return this.myTypeCache.set(index, type);
    }

    protected final int getCachedType(int index) {
        return this.myTypeCache.get(index);
    }

    protected final int getCachedOffset(int index) {
        return this.myEndOffsetCache.get(index);
    }

    public int getState() {
        int offset = this.myTokenStart - this.myLastOffset;
        return this.myLastState | offset << 16;
    }

    public int getTokenEnd() {
        return this.myEndOffsetCache.peekFirst();
    }

    public int getTokenStart() {
        return this.myTokenStart;
    }

    @NonNull
    @Override
    public LookAheadLexerPosition getCurrentPosition() {
        return new LookAheadLexerPosition(this, ImmutableUserMap.EMPTY);
    }

    @Override
    public final void restore(@NonNull LexerPosition position) {
        Preconditions.checkNotNullArgument(position, "position");
        restore((LookAheadLexerPosition) position);
    }

    protected void restore(@NonNull LookAheadLexerPosition position) {
        Preconditions.checkNotNullArgument(position, "position");
        start(this.delegate.getBufferSequence(), position.lastOffset, this.delegate.getBufferEnd(), position.lastState);
        for (int i = 0; i < position.advanceCount; i++) {
            next();
        }
    }

    public int getTokenType() {
        return this.myTypeCache.peekFirst();
    }

    public void start(@NonNull CharSequence buf, int startOffset, int endOffset, int initialState) {
        Preconditions.checkNotNullArgument(buf, "buffer");
        this.delegate.startInternal(buf, startOffset, endOffset, initialState & 0xFFFF);
        this.myTokenStart = startOffset;
        this.myTypeCache.clear();
        this.myEndOffsetCache.clear();
        next();
    }

    protected static class LookAheadLexerPosition implements LexerPosition {
        final int lastOffset;

        final int lastState;

        final int tokenStart;

        final int advanceCount;

        final ImmutableUserMap customMap;

        public LookAheadLexerPosition(@NonNull LookAheadLexer lookAheadLexer, @NonNull ImmutableUserMap map) {
            this.customMap = map;
            this.lastOffset = lookAheadLexer.myLastOffset;
            this.lastState = lookAheadLexer.myLastState;
            this.tokenStart = lookAheadLexer.myTokenStart;
            this.advanceCount = lookAheadLexer.myTypeCache.size() - 1;
        }

        @NonNull
        public ImmutableUserMap getCustomMap() {
            if (this.customMap == null)
                throw new NullPointerException();
            return this.customMap;
        }

        public int getOffset() {
            return this.tokenStart;
        }

        public int getState() {
            return this.lastState;
        }
    }


    @Override
    public Token getToken() {
        return delegate.getToken();
    }
}
