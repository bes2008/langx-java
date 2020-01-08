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
        Preconditions.checkNotNull(delimiter);
        Preconditions.checkNotNull(consumer);
        Collects.forEach(new DelimiterBasedReadableByteChannel(channel, delimiter), consumer);
    }

    public static void readUsingDelimiter(@NonNull InputStream inputStream, @NonNull String delimiter, @NonNull Charset charset, Consumer<String> consumer) {
        readUsingDelimiter(java.nio.channels.Channels.newChannel(inputStream), delimiter, charset, consumer);
    }

    public static void readUsingDelimiter(@NonNull ReadableByteChannel channel, @NonNull final String delimiter, @NonNull final Charset charset, @NonNull final Consumer<String> consumer) {
        readUsingDelimiter(channel, delimiter, new Consumer<byte[]>() {
            @Override
            public void accept(byte[] bytes) {
                String str = new String(bytes, charset);
                // in a windows or an unix os read \n or \r\n file
                if (delimiter.equals("\n") && str.endsWith("\r")) {
                    str = str.substring(0, str.length() - 1);
                }
                // in a mac os ( <9 version ) read \r or \r\n file
                if (delimiter.equals("\r") && str.startsWith("\n")) {
                    str = str.substring(1);
                }
                consumer.accept(str);
            }
        });
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
        readUsingDelimiter(channel, "\n", charset, consumer);
    }
}
