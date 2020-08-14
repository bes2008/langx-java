package com.jn.langx.security;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.Hex;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.security.exception.KeyFileFormatException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.*;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.LineDelimiter;

import java.io.*;
import java.security.Key;

public class KeyFileIOs {
    public static byte[] readKey(File file) {
        return readKey(Resources.loadFileResource(file));
    }

    public static byte[] readKey(File file, KeyFormat keyFormat) {
        return readKey(Resources.loadFileResource(file), keyFormat);
    }

    public static byte[] readKey(Resource resource) {
        return readKey(resource, KeyFormat.BASE64);
    }

    public static byte[] readKey(Resource resource, KeyFormat keyFormat) {
        try {
            String content = readKeyFile(resource);
            if (Emptys.isEmpty(content)) {
                throw new NullPointerException();
            }
            keyFormat = Objects.useValueIfNull(keyFormat, KeyFormat.BASE64);
            byte[] bytes = null;
            switch (keyFormat) {
                case HEX:
                    bytes = content.getBytes(Charsets.UTF_8);
                    break;
                case BASE64:
                    bytes = Base64.decodeBase64(content);
                    break;
                default:
                    bytes = content.getBytes(Charsets.UTF_8);
                    break;
            }
            return bytes;
        } catch (Throwable ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        }
    }

    public static String readKeyFile(Resource resource) {
        Preconditions.checkNotNull(resource);
        String filepath = resource.toString();
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder(2048);
        try {
            InputStream inputStream = resource.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            boolean beginLineFound = false;
            boolean endLineFound = false;
            while ((line = bufferedReader.readLine()) != null && !endLineFound) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.startsWith("-----") && line.endsWith("-----")) {
                        if (line.startsWith("-----BEGIN")) {
                            if (!beginLineFound) {
                                beginLineFound = true;
                            } else {
                                throw new KeyFileFormatException(StringTemplates.formatWithPlaceholder("error format key file : {}", filepath));
                            }
                        } else if (line.startsWith("-----END")) {
                            if (!endLineFound) {
                                endLineFound = true;
                            }
                        } else {
                            throw new KeyFileFormatException(StringTemplates.formatWithPlaceholder("error format key file : {}", filepath));
                        }
                    } else {
                        builder.append(line);
                        System.out.println(line);
                    }
                }
            }
        } catch (IOException ex) {
            throw Throwables.wrapAsRuntimeException(ex);
        } finally {
            IOs.close(bufferedReader);
        }
        if (builder.length() < 1) {
            throw new KeyFileFormatException(StringTemplates.formatWithPlaceholder("error format key file : {}", filepath));
        }
        return builder.toString();
    }

    public static void writeKey(@NonNull Key key, @NonNull File file) throws IOException {
        writeKey(key.getEncoded(), file, null);
    }

    public static void writeKey(@NonNull Key key, @NonNull File file, KeyFormat keyFormat) throws IOException {
        writeKey(key.getEncoded(), file, keyFormat);
    }

    public static void writeKey(@NonNull Key key, @NonNull File file, KeyFormat keyFormat, @Nullable String startLine, @Nullable String endLine) throws IOException {
        writeKey(key.getEncoded(), file, keyFormat, startLine, endLine);
    }

    public static void writeKey(byte[] keyBytes, File file) throws IOException {
        writeKey(keyBytes, file, null, null, null);
    }

    public static void writeKey(byte[] keyBytes, File file, KeyFormat keyFormat) throws IOException {
        writeKey(keyBytes, file, keyFormat, null, null);
    }

    public static void writeKey(byte[] keyBytes, File file, KeyFormat keyFormat, String startLine, String endLine) throws IOException {
        writeKey(keyBytes, new FileOutputStream(file, true), keyFormat, startLine, endLine);
    }

    public static void writeKey(byte[] keyBytes, OutputStream outputStream, KeyFormat keyFormat, String startLine, String endLine) throws IOException {
        Preconditions.checkNotNull(keyBytes);
        Preconditions.checkNotNull(outputStream);
        keyFormat = Objects.useValueIfNull(keyFormat, KeyFormat.BASE64);


        outputStream.write(LineDelimiter.DEFAULT.getValue().getBytes());
        if (Strings.isNotBlank(startLine)) {
            outputStream.write(startLine.trim().getBytes(Charsets.UTF_8));
            outputStream.write(LineDelimiter.DEFAULT.getValue().getBytes());
        }

        String encodedKeyString = null;
        switch (keyFormat) {
            case BASE64:
                encodedKeyString = Base64.encodeBase64String(keyBytes);
                break;
            case HEX:
                encodedKeyString = Hex.encodeHexString(keyBytes);
                break;
            default:
                encodedKeyString = new String(keyBytes, Charsets.UTF_8);
                break;
        }

        int offset = 0;
        while (offset < encodedKeyString.length()) {
            int toIndex = offset + 64;
            if (toIndex > encodedKeyString.length()) {
                toIndex = encodedKeyString.length();
            }
            outputStream.write(encodedKeyString.substring(offset, toIndex).getBytes(Charsets.UTF_8));
            outputStream.write(LineDelimiter.DEFAULT.getValue().getBytes());
            offset = toIndex;
        }

        if (Strings.isNotBlank(endLine)) {
            outputStream.write(endLine.trim().getBytes(Charsets.UTF_8));
            outputStream.write(LineDelimiter.DEFAULT.getValue().getBytes());
        }

    }

    public static enum KeyFormat {
        HEX,
        BASE64,
        UTF8
    }


}
