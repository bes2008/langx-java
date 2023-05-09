package com.jn.langx.io.stream;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.LineDelimiter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class DelimiterBasedReadableByteChannel implements ReadableByteChannel, Iterable<byte[]> {
    private List<ByteBuffer> delimiters;
    private ReadableByteChannel channel;
    private ByteBuffer buf;
    private boolean eof = false;
    private int maxLengthOfDelimiters;
    private int minLengthOfDelimiters;


    public DelimiterBasedReadableByteChannel(ReadableByteChannel channel, String... delimiter) {
        Preconditions.checkNotNull(channel, "channel is null");
        this.channel = channel;
        setDelimiters(delimiter);
    }

    public int read(ByteBuffer dst, String delimiter) throws IOException {
        setDelimiters(delimiter);
        return read(dst);
    }

    private void setDelimiters(String... delimiters) {
        boolean hasLineDelimiter = Collects.anyMatch(new Predicate<String>() {
            @Override
            public boolean test(String delimiter) {
                return LineDelimiter.isLineDelimiter(delimiter);
            }
        }, delimiters);
        List<String> dels = Pipeline.of(delimiters).asList();
        if (hasLineDelimiter) {
            dels.addAll(LineDelimiter.supportedLineDelimiterStrings());
        }

        List<ByteBuffer> ds = Pipeline.<String>of(dels)
                .clearEmptys()
                .distinct()
                .sort(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        // 按照长度降序排序
                        return o2.length() - o1.length();
                    }
                })
                .map(new Function<String, ByteBuffer>() {
                    @Override
                    public ByteBuffer apply(String delimiter) {
                        return ByteBuffer.wrap(delimiter.getBytes(Charsets.UTF_8));
                    }
                })
                .asList();
        Preconditions.checkTrue(Objs.isNotEmpty(ds), "delimiters is null or empty");
        this.delimiters = ds;
        this.maxLengthOfDelimiters = this.delimiters.get(0).limit();
        this.minLengthOfDelimiters = this.delimiters.get(this.delimiters.size() - 1).limit();
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
                if (hasNext()) {
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
        if (eof) {
            return -1;
        }
        if (buf == null) {
            buf = ByteBuffer.allocate(256);
        } else {
            if (buf.remaining() < Maths.max(this.maxLengthOfDelimiters, 1)) {
                // Buffer 的 limit 后面也没有任何容量了
                if (buf.capacity() == buf.limit()) {
                    // 如果当前segment的起始位置 超过了容量的 80% , 就要move 到开头，否则就扩容
                    int newBufCapacity;
                    if (buf.arrayOffset() > buf.array().length * 0.8) {
                        // 前移
                        newBufCapacity = buf.array().length;
                    } else {
                        // 扩容
                        newBufCapacity = buf.array().length * 2;
                    }
                    ByteBuffer buf2 = ByteBuffer.allocate(newBufCapacity);
                    buf2.put(buf.array(), buf.arrayOffset(), buf.limit());
                    buf = buf2;
                } else {
                    /*
                    ByteBuffer buf2 = ByteBuffer.wrap(buf.array(), buf.arrayOffset(), buf.array().length);
                    buf2.position(buf2.position());
                    buf = buf2;
                     */
                    buf.limit(buf.capacity());
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

        if (delimiters.size() > 1) {
            while (buf.hasRemaining() && buf.remaining() >= this.minLengthOfDelimiters) {
                // 寻找匹配的 分隔符
                ByteBuffer delimiter = null;
                for (ByteBuffer del : delimiters) {
                    if (buf.remaining() < del.limit()) {
                        continue;
                    }
                    buf.mark();
                    byte[] tmp = new byte[del.limit()];
                    buf.get(tmp);
                    buf.reset();
                    if (Objs.deepEquals(tmp, del.array())) {
                        delimiter = del;
                        break;
                    }
                }

                if (delimiter == null) {
                    // not found
                    buf.position(buf.position() + this.minLengthOfDelimiters);
                } else {
                    // found
                    int length = buf.position();
                    int start = buf.arrayOffset();
                    ByteBuffer ret = ByteBuffer.wrap(buf.array(), start, length);
                    buf.position(buf.position() + delimiter.limit());
                    // 每次找到后，进行切分，只要剩余部分继续作为 buf
                    this.buf = this.buf.slice();
                    return ret;
                }
            }
        } else {
            ByteBuffer delimiter = delimiters.get(0);
            delimiter.clear();
            byte firstByteOfDelimiter = delimiter.get();
            B:
            while (buf.hasRemaining() && buf.remaining() >= delimiter.limit()) {
                if (buf.get() == firstByteOfDelimiter) {
                    while (delimiter.hasRemaining() && buf.remaining() >= delimiter.remaining()) {
                        if (delimiter.get() != buf.get()) {
                            delimiter.clear();
                            firstByteOfDelimiter = delimiter.get();
                            continue B;
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
