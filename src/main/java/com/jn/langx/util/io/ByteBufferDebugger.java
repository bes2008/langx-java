package com.jn.langx.util.io;

import java.nio.ByteBuffer;

public class ByteBufferDebugger {
    public static String showByteBuffer(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        byte[] bytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(bytes);
        byteBuffer.position(position);
        return new String(bytes);
    }
}
