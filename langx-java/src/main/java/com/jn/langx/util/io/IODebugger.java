package com.jn.langx.util.io;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;

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
        try {
            return IOs.readAsString(inputStream, charset);
        }catch (IOException e){
            return "IO ERROR";
        }
    }

    private IODebugger() {

    }
}
