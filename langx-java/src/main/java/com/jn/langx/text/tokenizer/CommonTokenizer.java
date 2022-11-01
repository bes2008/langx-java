package com.jn.langx.text.tokenizer;

import com.jn.langx.io.buffer.CharSequenceBuffer;
import com.jn.langx.util.Preconditions;

public abstract class CommonTokenizer<Token> extends AbstractTokenizer<Token> {
    private boolean returnDelimiter;
    /**
     * buffer#mark()用于标记一个 content segment的开始
     */
    private CharSequenceBuffer buffer;
    protected TokenFactory<Token> tokenFactory;

    protected CommonTokenizer(String text, boolean returnDelimiter) {
        Preconditions.checkNotNull(text);
        this.buffer = new CharSequenceBuffer(text);
        this.returnDelimiter = returnDelimiter;
    }

    protected final CharSequenceBuffer getBuffer() {
        return this.buffer;
    }

    @Override
    protected final Token getNext() {
        Preconditions.checkNotNull(tokenFactory, "the token factory is null");
        boolean hasRemaining = this.buffer.hasRemaining();
        if (hasRemaining) {
            long position = this.buffer.position();
            long[] delimiterPositions = findNextDelimiter();
            if (delimiterPositions == null) {
                // 直到结束还没找到分隔符
                long segmentEnd = this.buffer.limit();
                String segment = this.buffer.substring(position, segmentEnd);
                Token token = tokenFactory.get(segment, false);
                Preconditions.checkNotNull(token, "the token is null");
                return token;
            } else {
                // 找到了分隔符
                long segmentEnd = delimiterPositions[0];
                if (segmentEnd == position) {
                    // 刚一进来这个 getNext()方法，就遇到了分隔符
                    if (returnDelimiter) {
                        // 返回分隔符
                        String delimiter = this.buffer.substring(position, delimiterPositions[1]);
                        this.buffer.position(delimiterPositions[1]);
                        this.buffer.mark();
                        Token token = tokenFactory.get(delimiter, true);
                        Preconditions.checkNotNull(token, "the delimiter token is null");
                        return token;
                    } else {
                        // 不返回分隔符的情况下，要再一次进行查找
                        this.buffer.position(delimiterPositions[1]);
                        this.buffer.mark();
                        return getNext();
                    }
                } else if (segmentEnd > position) {
                    String segment = this.buffer.substring(position, segmentEnd);
                    this.buffer.position(segmentEnd);
                    this.buffer.mark();
                    Token token = tokenFactory.get(segment, false);
                    Preconditions.checkNotNull(token, "the token is null");
                    return token;
                } else {
                    throw new TokenizationException("error");
                }
            }
        } else {
            return null;
        }

    }

    /**
     * @return 返回下一个delimiter的开始结束位置（包含开始，不包含结束） [0,1)
     */
    private long[] findNextDelimiter() {
        while (this.buffer.hasRemaining()) {
            long position = this.buffer.position();
            char c = this.buffer.get();
            String delimiter = getIfDelimiterStart(position, c);
            if (delimiter != null) {
                return new long[]{position, position + delimiter.length()};
            }
        }
        return null;
    }


    /**
     * 如果接下来是 delimiter，就返回，如果不是范围null
     *
     * @param position
     * @param c
     * @return
     */
    protected abstract String getIfDelimiterStart(long position, char c);

}
