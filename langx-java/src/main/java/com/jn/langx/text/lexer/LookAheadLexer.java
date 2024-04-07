package com.jn.langx.text.lexer;


import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;

public abstract class LookAheadLexer extends AbstractLexer {
    private int myLastOffset;

    private int myLastState;

    private final AbstractLexer delegate;

    private int myTokenStart;

    private final MutableRandomAccessQueue<TokenType> myTypeCache;

    private final MutableRandomAccessQueue<Integer> myEndOffsetCache;

    protected LookAheadLexer(@NonNull AbstractLexer delegate, int capacity) {
        this.delegate = delegate;
        this.myTypeCache = new MutableRandomAccessQueue<TokenType>(capacity);
        this.myEndOffsetCache = new MutableRandomAccessQueue<Integer>(capacity);
    }

    protected LookAheadLexer(@NonNull AbstractLexer baseLexer) {
        this(baseLexer, 64);
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
        lookAhead(this.delegate);
        assert !this.myTypeCache.isEmpty();
    }

    protected void lookAhead(@NonNull Lexer lexer) {
        Preconditions.checkNotNullArgument(lexer, "delegate");
        advanceLexer(lexer);
    }

    /**
     * 前进
     * @param lexer
     */
    protected final void advanceLexer(@NonNull Lexer lexer) {
        Preconditions.checkNotNullArgument(lexer, "delegate");
        advanceAs(lexer, lexer.getTokenType());
    }

    protected final void advanceAs(@NonNull Lexer lexer, TokenType type) {
        Preconditions.checkNotNullArgument(lexer, "lexer");
        addToken(type);
        lexer.next();
    }

    protected void addToken(TokenType type) {
        addToken(this.delegate.getTokenEnd(), type);
    }

    protected void addToken(int endOffset, TokenType type) {
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

    public TokenType replaceCachedType(int index, TokenType token) {
        return this.myTypeCache.set(index, token);
    }

    protected final TokenType getCachedType(int index) {
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

    public TokenType getTokenType() {
        return this.myTypeCache.peekFirst();
    }

    public void start(@NonNull CharSequence buf, int startOffset, int endOffset, int initialState) {
        Preconditions.checkNotNullArgument(buf, "buffer");
        this.delegate.startInternal(buf, startOffset, endOffset, initialState & 0xFFFF);
        this.myTokenStart = startOffset;
        this.myTypeCache.clear();
        this.myEndOffsetCache.clear();
        doLookAhead();
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


}
