package com.jn.langx.util.io;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Iterator;

public class DelimiterBasedReadableByteChannel implements ReadableByteChannel, Iterable<byte[]> {
    private ByteBuffer delimiter;
    private ReadableByteChannel channel;
    private ByteBuffer buf;
    private int nextScanDelimiterStartPosition = 0;

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
        this.delimiter = ByteBuffer.wrap(delimiter.getBytes());
        if (buf != null) {
            this.nextScanDelimiterStartPosition = buf.position();
        }
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
            buf.mark();
        } else {
            if (!buf.hasRemaining() && buf.capacity()-buf.limit()==0) {
                buf.position(0);
                buf.limit(buf.capacity());
                buf.mark();
            } else {
                // the buff will full
                if (buf.position() == 0 && buf.limit() - nextScanDelimiterStartPosition <= delimiter.capacity() && buf.capacity() - buf.limit() < delimiter.capacity()) {
                    // expand capacity
                    ByteBuffer buf2 = ByteBuffer.allocate(buf.capacity() * 2);
                    ByteBuffer willCopy = buf.slice();
                    willCopy.limit(buf.remaining());
                    buf2.put(willCopy);
                    buf2.position(0);
                    buf.mark();
                    buf2.position(willCopy.limit());
                    buf2.limit(buf2.capacity());
                    buf = buf2;
                } else {
                    buf.mark();
                    buf.position(buf.limit());
                    buf.limit(buf.capacity());
                }
            }
        }
        int length = channel.read(buf);
        buf.limit(buf.position());
        if (length != -1) {
            buf.reset();
        }
        return length;
    }

    public boolean hasNextSegment() throws IOException {
        if (buf == null || !buf.hasRemaining() || buf.limit() - nextScanDelimiterStartPosition < 1) {
            fill();
        }
        return buf.hasRemaining();

    }

    public ByteBuffer nextSegment() throws IOException {
        if (buf == null || !buf.hasRemaining() || buf.limit() - nextScanDelimiterStartPosition < 1) {
            fill();
        }
        if (!buf.hasRemaining()) {
            return buf;
        }
        buf.position(nextScanDelimiterStartPosition);
        buf.mark();
        boolean found = findDelimiter(buf);
        int waterPosition = buf.position();
        if (found) {
            // skip the delimiter
            buf.reset();
            ByteBuffer ret = buf.slice();
            ret.limit(waterPosition - nextScanDelimiterStartPosition);
            buf.position(waterPosition + delimiter.capacity());
            nextScanDelimiterStartPosition = buf.position();
            return ret;
        } else {
            // copy to an new buffer
            ByteBuffer buf2 = ByteBuffer.allocate(buf.capacity());
            buf.reset();
            int delta = buf.limit() - waterPosition;
            ByteBuffer subBuffer = buf.slice();
            subBuffer.limit(buf.limit() - nextScanDelimiterStartPosition);
            buf2.put(subBuffer);
            buf2.position(0);
            buf2.limit(subBuffer.limit());
            buf = buf2;
            nextScanDelimiterStartPosition = buf.limit() - delta;
            return nextSegment();
        }
    }

    private boolean findDelimiter(ByteBuffer byteBuffer) {
        delimiter.clear();
        int delimiterLength = delimiter.limit();
        byte firstByteOfDelimiter = delimiter.get();

        // skip to scan position
        byteBuffer.position(nextScanDelimiterStartPosition);

        while (byteBuffer.hasRemaining()) {
            byte b = byteBuffer.get();
            if (b == firstByteOfDelimiter) {
                if (delimiterLength > 1) {
                    boolean found = true;
                    int i = 0;
                    while (delimiter.hasRemaining() && byteBuffer.hasRemaining()) {
                        if (delimiter.get() != byteBuffer.get()) {
                            found = false;
                            break;
                        } else {
                            i++;
                        }
                    }

                    if (found) {
                        int delimiterStartPosition = byteBuffer.position() - delimiterLength;
                        byteBuffer.position(delimiterStartPosition);
                        return true;
                    } else {
                        if (byteBuffer.hasRemaining()) {
                            // not found
                        } else {
                            byteBuffer.position(byteBuffer.position() - i);
                        }
                    }
                } else {
                    int delimiterStartPosition = byteBuffer.position() - delimiterLength;
                    byteBuffer.position(delimiterStartPosition);
                    return true;
                }
            }
        }
        return false;
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
