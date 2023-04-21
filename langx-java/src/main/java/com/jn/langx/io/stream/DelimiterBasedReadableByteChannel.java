package com.jn.langx.io.stream;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.io.Charsets;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;

public class DelimiterBasedReadableByteChannel implements ReadableByteChannel, Iterable<byte[]> {
    private ByteBuffer delimiter;
    private ReadableByteChannel channel;
    private ByteBuffer buf;
    private boolean eof = false;

    public DelimiterBasedReadableByteChannel(ReadableByteChannel channel, String delimiter) {
        Preconditions.checkNotNull(channel, "channel is null");
        this.channel = channel;
        setDelimiter(delimiter);
    }

    public int read(ByteBuffer dst, String delimiter) throws IOException {
        setDelimiter(delimiter);
        return read(dst);
    }

    public void setDelimiter(String delimiter) {
        Preconditions.checkTrue(Strings.isNotEmpty(delimiter), "delimiter is null or empty");
        this.delimiter = ByteBuffer.wrap(delimiter.getBytes(Charsets.UTF_8));
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return channel.read(dst);
    }

    @Override
    public Iterator<byte[]> iterator() {
        return new Iterator<byte[]>() {
            @Override
            public boolean hasNext() {
                try {
                    return hasNextSegment();
                } catch (IOException ex) {
                    return false;
                }
            }

            @Override
            public byte[] next() {
                if(hasNext()) {
                    try {
                        ByteBuffer byteBuffer = nextSegment();
                        byte[] bytes = new byte[byteBuffer.remaining()];
                        byteBuffer.get(bytes);
                        return bytes;
                    } catch (IOException ex) {
                        return Emptys.EMPTY_BYTES;
                    }
                }
                return Emptys.EMPTY_BYTES;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private int fill() throws IOException {
        if (buf == null) {
            buf = ByteBuffer.allocate(256);
        } else {
            if (!buf.hasRemaining()) {
                // Buffer 的 limit 后面也没有任何容量了
                if (buf.capacity() == buf.limit()) {
                    // 如果当前segment的起始位置 超过了容量的 80% , 就要move 到开头，否则就扩容
                    int newBufCapacity;
                    if(buf.arrayOffset() >  buf.array().length * 0.8){
                        // 前移
                        newBufCapacity = buf.array().length;
                    }else{
                        // 扩容
                        newBufCapacity = buf.array().length * 2;
                    }
                    ByteBuffer buf2 = ByteBuffer.allocate(newBufCapacity);
                    buf2.put(buf.array(), buf.arrayOffset(), buf.limit());
                    buf = buf2;
                }
            }
        }
        buf.mark();
        int length = channel.read(buf);
        if (length == -1) {
            eof = true;
        }
        buf.limit(buf.position());
        buf.reset();
        return length;
    }

    public boolean hasNextSegment() throws IOException {
        if (buf == null || !buf.hasRemaining()) {
            if (!eof) {
                fill();
            }
        }
        return buf != null && (buf.hasRemaining() || !eof);

    }

    public ByteBuffer nextSegment() throws IOException {
        if (buf == null && eof) {
            return null;
        }
        if (buf == null || !buf.hasRemaining()) {
            fill();
            if (!buf.hasRemaining() && eof) {
                int start = buf.arrayOffset();
                ByteBuffer ret = ByteBuffer.wrap(buf.array(), start, buf.position());
                return ret;
            }
        }

        delimiter.clear();
        byte firstByteOfDelimiter = delimiter.get();
        A:
        while (buf.hasRemaining() && buf.remaining() >= delimiter.limit()) {
            if (buf.get() == firstByteOfDelimiter) {
                while (delimiter.hasRemaining() && buf.remaining() >= delimiter.remaining()) {
                    if (delimiter.get() != buf.get()) {
                        delimiter.clear();
                        firstByteOfDelimiter = delimiter.get();
                        continue A;
                    }
                }

                // found
                delimiter.clear();
                int current = buf.position();
                int length = current - delimiter.limit();
                int start = buf.arrayOffset();
                ByteBuffer ret = ByteBuffer.wrap(buf.array(), start, length);
                // 每次找到后，进行切分，只要剩余部分继续作为 buf
                this.buf = this.buf.slice();
                return ret;
            }
        }
        fill();
        return nextSegment();
    }

    @Override
    public boolean isOpen() {
        return channel.isOpen();
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }

}
