package com.jn.langx.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class IOs {
    public static String readAsString(Reader reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(256);
        char[] chars = new char[1024];
        int length = -1;
        while ((length = reader.read(chars)) != -1) {
            stringBuilder.append(chars, 0, length);
        }
        return stringBuilder.toString();
    }

    public static String readAsString(InputStream reader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder(256);
        byte[] bytes = new byte[1024];
        int length = -1;
        while ((length = reader.read(bytes)) != -1) {
            stringBuilder.append(new String(bytes, 0, length));
        }
        return stringBuilder.toString();
    }

}
