package com.jn.langx.text.tokenizer;

import com.jn.langx.io.buffer.CharSequenceBuffer;
import com.jn.langx.util.Preconditions;

public abstract class CommonTokenizer extends AbstractTokenizer<String> {
    private boolean returnDelimiter;
    private CharSequenceBuffer buffer;

    protected CommonTokenizer(String text, boolean returnDelimiter) {
        Preconditions.checkNotNull(text);
        this.buffer = new CharSequenceBuffer(text);
        this.returnDelimiter = returnDelimiter;
    }

    protected final CharSequenceBuffer getBuffer() {
        return this.buffer;
    }

    @Override
    protected final String getNext() {

        boolean hasRemaining = this.buffer.hasRemaining();
        if (hasRemaining) {
            long position = this.buffer.position();
            long[] delimiterPositions = findNextDelimiter();
            if (delimiterPositions == null) {
                // 直到结束还没找到分隔符
                long segmentEnd = this.buffer.limit();
                String segment = this.buffer.toString(position, segmentEnd);
                return segment;
            } else {
                // 找到了分隔符
                long segmentEnd = delimiterPositions[0];
                if (segmentEnd == position) {
                    // 刚一进来这个 getNext()方法，就遇到了分隔符
                    if (returnDelimiter) {
                        // 返回分隔符
                        String delimiter = this.buffer.toString(position, delimiterPositions[1]);
                        this.buffer.position(delimiterPositions[1]);
                        return delimiter;
                    } else {
                        // 不返回分隔符的情况下，要再一次进行查找
                        this.buffer.position(delimiterPositions[1]);
                        return getNext();
                    }
                } else if (segmentEnd > position) {
                    String segment = this.buffer.toString(position, segmentEnd);
                    this.buffer.position(segmentEnd);
                    return segment;
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
