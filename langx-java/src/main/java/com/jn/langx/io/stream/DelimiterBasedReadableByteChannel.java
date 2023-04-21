package com.jn.langx.io.stream;

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
                try {
                    ByteBuffer byteBuffer = nextSegment();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    return bytes;
                } catch (IOException ex) {
                    return new byte[0];
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    private int fill() throws IOException {
        if (buf == null) {
            buf = ByteBuffer.allocate(8192);
        } else {
            if (!buf.hasRemaining()) {
                // 不足 5% 时 expand the buffer
                if ((buf.capacity() - buf.limit()) < buf.capacity() / 20) {
                    // expand capacity
                    ByteBuffer buf2 = ByteBuffer.allocate(buf.capacity() * 2);
                    int start = buf.arrayOffset();
                    buf2.put(buf.array(), start , buf.position()-start);
                    buf = buf2;
                }
            }
        }
        int length = channel.read(buf);
        if (length == -1) {
            eof = true;
        }
        buf.limit(buf.position());
        return length;
    }

    public boolean hasNextSegment() throws IOException {
        if (buf == null || !buf.hasRemaining()) {
            if (!eof) {
                fill();
            }
        }
        return buf != null && buf.hasRemaining();

    }

    public ByteBuffer nextSegment() throws IOException {
        if (buf == null && eof) {
            return null;
        }
        if (buf == null || !buf.hasRemaining()) {
            fill();
            if (!buf.hasRemaining()) {
                return null;
            }
        }

        int start = buf.position();
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

                buf.mark();
                int current = buf.position();
                int end = current - delimiter.limit();
                ByteBuffer ret = ByteBuffer.wrap(buf.array(), start, end-start);
                buf.slice();
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
