package com.jn.langx.util.io;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;

import java.io.InputStream;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;

public class Channels {
    public static void readUsingDelimiter(@NonNull InputStream inputStream, @NonNull String delimiter, @NonNull Consumer<byte[]> consumer) {
        readUsingDelimiter(java.nio.channels.Channels.newChannel(inputStream), delimiter, consumer);
    }

    public static void readUsingDelimiter(@NonNull ReadableByteChannel channel, @NonNull String delimiter, @NonNull Consumer<byte[]> consumer) {
        Collects.forEach(new DelimiterBasedReadableByteChannel(channel, delimiter), consumer);
    }

    public static void readLines(@NonNull InputStream inputStream, @NonNull Consumer<String> consumer) {
        readLines(inputStream, Charsets.getDefault(), consumer);
    }

    public static void readLines(@NonNull InputStream inputStream, @NonNull final Charset charset, @NonNull final Consumer<String> consumer) {
        readLines(java.nio.channels.Channels.newChannel(inputStream), charset, consumer);
    }

    public static void readLines(@NonNull ReadableByteChannel channel, @NonNull Consumer<String> consumer) {
        readLines(channel, Charsets.getDefault(), consumer);
    }

    public static void readLines(@NonNull ReadableByteChannel channel, @NonNull final Charset charset, @NonNull final Consumer<String> consumer) {
        Preconditions.checkNotNull(charset);
        Preconditions.checkNotNull(consumer);
        readUsingDelimiter(channel, "\n", new Consumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) {
                consumer.accept(new String(bytes, charset));
            }
        });
    }
}
