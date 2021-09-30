package com.jn.langx.util.io;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class IODebugger {
    public static String showBytes(byte[] bytes) {
        return showBytes(bytes, null);
    }

    public static String showBytes(@NonNull byte[] bytes, @Nullable Charset charset) {
        Preconditions.checkNotNull(bytes);
        return charset == null ? new String(bytes, Charsets.UTF_8) : new String(bytes, charset);
    }


    public static String showBytes(ByteBuffer byteBuffer) {
        return showBytes(byteBuffer, null);
    }

    public static String showBytes(ByteBuffer byteBuffer, Charset charset) {
        if (byteBuffer.remaining() > 0) {
            int position = byteBuffer.position();
            byte[] bytes = new byte[byteBuffer.remaining()];
            byteBuffer.get(bytes);
            byteBuffer.position(position);
            return showBytes(bytes, charset);
        }
        return "";
    }

    public static String showBytes(@NonNull InputStream inputStream) {
        return showBytes(inputStream, null);
    }

    public static String showBytes(@NonNull InputStream inputStream, @NonNull Charset charset) {
        Preconditions.checkNotNull(inputStream);
        int remaining = IOs.getRemaining(inputStream);
        if (remaining > 0 && inputStream.markSupported()) {
            inputStream.mark(remaining + 10);
            try {
                byte[] bytes = new byte[remaining];
                inputStream.read(bytes);

                return showBytes(bytes, charset);
            } catch (IOException ex) {
                throw Throwables.wrapAsRuntimeIOException(ex);
            } finally {
                try {
                    inputStream.reset();
                } catch (IOException ex) {
                    throw Throwables.wrapAsRuntimeIOException(ex);
                }
            }
        }
        return "";
    }
}
