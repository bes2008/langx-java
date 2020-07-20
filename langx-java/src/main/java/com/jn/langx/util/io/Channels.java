package com.jn.langx.util.io;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.io.stream.DelimiterBasedReadableByteChannel;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.struct.Holder;

import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

public class Channels {
    public static void readUsingDelimiter(@NonNull InputStream inputStream, @NonNull String delimiter, @NonNull final Consumer2<Integer, byte[]> consumer) {
        readUsingDelimiter(inputStream, delimiter, null, consumer, null);
    }

    public static void readUsingDelimiter(@NonNull InputStream inputStream, @NonNull String delimiter, @NonNull final Consumer<byte[]> consumer) {
        readUsingDelimiter(inputStream, delimiter, null, new Consumer2<Integer, byte[]>() {
            @Override
            public void accept(Integer key, byte[] value) {
                consumer.accept(value);
            }
        }, null);
    }

    public static void readUsingDelimiter(@NonNull InputStream inputStream, @NonNull String delimiter, @Nullable Predicate2<Integer, byte[]> consumePredicate, @NonNull Consumer2<Integer, byte[]> consumer, @Nullable Predicate2<Integer, byte[]> breakPredicate) {
        readUsingDelimiter(java.nio.channels.Channels.newChannel(inputStream), delimiter, consumePredicate, consumer, breakPredicate);
    }

    public static void readUsingDelimiter(@NonNull ReadableByteChannel channel, @NonNull String delimiter, @NonNull final Consumer2<Integer, byte[]> consumer) {
        readUsingDelimiter(channel, delimiter, null, consumer, null);
    }

    public static void readUsingDelimiter(@NonNull ReadableByteChannel channel, @NonNull String delimiter, @NonNull final Consumer<byte[]> consumer) {
        readUsingDelimiter(channel, delimiter, null, new Consumer2<Integer, byte[]>() {
            @Override
            public void accept(Integer key, byte[] value) {
                consumer.accept(value);
            }
        }, null);
    }

    /**
     * 所有方法最终都是走这个
     */
    public static void readUsingDelimiter(@NonNull ReadableByteChannel channel, @NonNull String delimiter, @Nullable Predicate2<Integer, byte[]> consumePredicate, @NonNull Consumer2<Integer, byte[]> consumer, @Nullable Predicate2<Integer, byte[]> breakPredicate) {
        Preconditions.checkNotNull(delimiter);
        Preconditions.checkNotNull(consumer);
        Collects.forEach(new DelimiterBasedReadableByteChannel(channel, delimiter), consumePredicate, consumer, breakPredicate);
    }

    public static void readUsingDelimiter(@NonNull InputStream inputStream, @NonNull String delimiter, @NonNull Charset charset, Consumer<String> consumer) {
        readUsingDelimiter(java.nio.channels.Channels.newChannel(inputStream), delimiter, charset, consumer);
    }

    public static void readUsingDelimiter(@NonNull InputStream inputStream, @NonNull String delimiter, @NonNull Charset charset, Consumer2<Integer, String> consumer) {
        readUsingDelimiter(java.nio.channels.Channels.newChannel(inputStream), delimiter, charset, consumer);
    }

    public static void readUsingDelimiter(@NonNull InputStream inputStream, @NonNull String delimiter, @NonNull Charset charset, Predicate2<Integer, String> consumePredicate, Consumer2<Integer, String> consumer, Predicate2<Integer, String> breakConsumer) {
        readUsingDelimiter(java.nio.channels.Channels.newChannel(inputStream), delimiter, charset, consumePredicate, consumer, breakConsumer);
    }

    public static void readUsingDelimiter(@NonNull ReadableByteChannel channel, @NonNull final String delimiter, @NonNull final Charset charset, @NonNull final Consumer<String> consumer) {
        readUsingDelimiter(channel, delimiter, charset, null, new Consumer2<Integer, String>() {
            @Override
            public void accept(Integer index, String value) {
                consumer.accept(value);
            }
        }, null);
    }

    public static void readUsingDelimiter(@NonNull ReadableByteChannel channel, @NonNull final String delimiter, @NonNull final Charset charset, @NonNull final Consumer2<Integer, String> consumer) {
        Preconditions.checkNotNull(charset);
        readUsingDelimiter(channel, delimiter, Charsets.getDefault(), null, consumer, null);
    }

    /**
     * 在消费之前，把 byte[] 转为 String
     */
    public static void readUsingDelimiter(@NonNull ReadableByteChannel channel, @NonNull final String delimiter, @NonNull final Charset charset, @Nullable final Predicate2<Integer, String> consumePredicate, @NonNull final Consumer2<Integer, String> consumer, @Nullable final Predicate2<Integer, String> breakPredicate) {
        Preconditions.checkNotNull(charset);
        final Holder<String> lineValueHolder = new Holder<String>();
        readUsingDelimiter(channel, delimiter, null, new Consumer2<Integer, byte[]>() {
            @Override
            public void accept(Integer index, byte[] bytes) {
                String str = new String(bytes, charset);
                // in a windows or an unix os read \n or \r\n file
                if (delimiter.equals("\n") && str.endsWith("\r")) {
                    str = str.substring(0, str.length() - 1);
                }
                // in a mac os ( <9 version ) read \r or \r\n file
                if (delimiter.equals("\r") && str.startsWith("\n")) {
                    str = str.substring(1);
                }

                if (consumePredicate == null || consumePredicate.test(index, str)) {
                    consumer.accept(index, str);
                }
                lineValueHolder.set(str);
            }
        }, new Predicate2<Integer, byte[]>() {
            @Override
            public boolean test(Integer index, byte[] value) {
                return breakPredicate == null || breakPredicate.test(index, lineValueHolder.get());
            }
        });
    }


    public static void readLines(@NonNull InputStream inputStream, @NonNull Consumer<String> consumer) {
        readLines(inputStream, Charsets.getDefault(), consumer);
    }

    public static void readLines(@NonNull InputStream inputStream, @NonNull Consumer2<Integer, String> consumer) {
        readLines(inputStream, Charsets.getDefault(), consumer);
    }

    public static void readLines(@NonNull InputStream inputStream, @Nullable Predicate2<Integer, String> consumePredicate, @NonNull Consumer2<Integer, String> consumer, @Nullable Predicate2<Integer, String> breakPredicate) {
        readLines(inputStream, Charsets.getDefault(), consumePredicate, consumer, breakPredicate);
    }

    public static void readLines(@NonNull InputStream inputStream, @NonNull final Charset charset, @NonNull final Consumer<String> consumer) {
        readLines(java.nio.channels.Channels.newChannel(inputStream), charset, consumer);
    }

    public static void readLines(@NonNull InputStream inputStream, @NonNull final Charset charset, @NonNull final Consumer2<Integer, String> consumer) {
        readLines(java.nio.channels.Channels.newChannel(inputStream), charset, consumer);
    }

    public static void readLines(@NonNull InputStream inputStream, @NonNull final Charset charset, @Nullable Predicate2<Integer, String> consumePredicate, @NonNull Consumer2<Integer, String> consumer, @Nullable Predicate2<Integer, String> breakPredicate) {
        readLines(java.nio.channels.Channels.newChannel(inputStream), charset, consumePredicate, consumer, breakPredicate);
    }

    public static void readLines(@NonNull ReadableByteChannel channel, @NonNull Consumer<String> consumer) {
        readLines(channel, Charsets.getDefault(), consumer);
    }

    public static void readLines(@NonNull ReadableByteChannel channel, @NonNull final Charset charset, @NonNull final Consumer<String> consumer) {
        Preconditions.checkNotNull(charset);
        Preconditions.checkNotNull(consumer);
        readUsingDelimiter(channel, "\n", charset, consumer);
    }

    public static void readLines(@NonNull ReadableByteChannel channel, @NonNull final Charset charset, @NonNull final Consumer2<Integer, String> consumer) {
        Preconditions.checkNotNull(charset);
        Preconditions.checkNotNull(consumer);
        readUsingDelimiter(channel, "\n", charset, consumer);
    }

    public static void readLines(@NonNull ReadableByteChannel channel, @NonNull final Charset charset, @Nullable Predicate2<Integer, String> consumePredicate, @NonNull Consumer2<Integer, String> consumer, @Nullable Predicate2<Integer, String> breakPredicate) {
        Preconditions.checkNotNull(charset);
        Preconditions.checkNotNull(consumer);
        readUsingDelimiter(channel, "\n", charset, consumePredicate, consumer, breakPredicate);
    }
}
