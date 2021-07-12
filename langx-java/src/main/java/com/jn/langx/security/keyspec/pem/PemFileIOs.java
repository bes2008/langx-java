package com.jn.langx.security.keyspec.pem;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.hex.Hex;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.io.resource.ByteArrayResource;
import com.jn.langx.io.resource.InputStreamResource;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.io.stream.StringBuilderWriter;
import com.jn.langx.security.exception.KeyFileFormatException;
import com.jn.langx.security.keyspec.KeyEncode;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.*;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.LineDelimiter;

import java.io.*;
import java.security.Key;

public class PemFileIOs {
    public static byte[] readKey(File file) {
        return readKey(Resources.loadFileResource(file));
    }

    public static byte[] readKey(File file, KeyEncode keyFormat) {
        return readKey(Resources.loadFileResource(file), keyFormat);
    }

    public static byte[] readKey(Resource resource) {
        return readKey(resource, KeyEncode.BASE64);
    }

    public static byte[] readKey(Resource resource, KeyEncode keyFormat) {
        try {
            String content = readKeyAsString(resource);
            if (Emptys.isEmpty(content)) {
                throw new NullPointerException();
            }
            keyFormat = Objects.useValueIfNull(keyFormat, KeyEncode.BASE64);
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

    public static String readKeyAsString(byte[] bytes){
        return readKeyAsString(new ByteArrayResource(bytes));
    }

    public static String readKeyAsString(InputStream inputStream){
        return readKeyAsString(new InputStreamResource(inputStream));
    }

    public static String readKeyAsString(Resource resource){
        Preconditions.checkNotNull(resource);
        String filepath = resource.toString();
        BufferedReader bufferedReader = null;
        StringBuilder builder = new StringBuilder(2048);
        try {
            InputStream inputStream = resource.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charsets.UTF_8));
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

    @Deprecated
    public static String readKeyFile(Resource resource) {
        return readKeyAsString(resource);
    }

    public static void writeKey(@NonNull Key key, @NonNull File file) throws IOException {
        writeKey(key.getEncoded(), file, null);
    }

    public static void writeKey(@NonNull Key key, @NonNull File file, KeyEncode keyFormat) throws IOException {
        writeKey(key.getEncoded(), file, keyFormat);
    }

    public static void writeKey(@NonNull Key key, @NonNull File file, KeyEncode keyFormat, @Nullable String headerLine, @Nullable String footerLine) throws IOException {
        writeKey(key.getEncoded(), file, keyFormat, headerLine, footerLine);
    }

    public static void writeKey(byte[] keyBytes, File file) throws IOException {
        writeKey(keyBytes, file, null, null, null);
    }

    public static void writeKey(byte[] keyBytes, File file, KeyEncode keyFormat) throws IOException {
        writeKey(keyBytes, file, keyFormat, null, null);
    }

    public static void writeKey(byte[] keyBytes, File file, KeyEncode keyFormat, String headerLine, String footerLine) throws IOException {
        writeKey(keyBytes, new FileOutputStream(file, true), keyFormat, headerLine, footerLine);
    }

    public static void writeKey(byte[] keyBytes, OutputStream outputStream, KeyEncode keyFormat, String headerLine, String footerLine) throws IOException {
        Preconditions.checkNotNull(keyBytes);
        Preconditions.checkNotNull(outputStream);
        writeKey(keyBytes, new OutputStreamWriter(outputStream, Charsets.UTF_8), keyFormat, headerLine, footerLine);
    }

    public static void writeKey(byte[] keyBytes, Writer writer, KeyEncode keyFormat, String headLine, String footerLine) throws IOException {
        Preconditions.checkNotNull(keyBytes);
        Preconditions.checkNotNull(writer);
        keyFormat = Objects.useValueIfNull(keyFormat, KeyEncode.BASE64);

        writer.write(LineDelimiter.DEFAULT.getValue());
        if (Strings.isNotBlank(headLine)) {
            writer.write(headLine.trim());
            writer.write(LineDelimiter.DEFAULT.getValue());
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
            writer.write(encodedKeyString.substring(offset, toIndex));
            writer.write(LineDelimiter.DEFAULT.getValue());
            offset = toIndex;
            writer.flush();
        }

        if (Strings.isNotBlank(footerLine)) {
            writer.write(footerLine.trim());
            writer.write(LineDelimiter.DEFAULT.getValue());
        }
        writer.flush();
    }

    public static void writeKey(byte[] keyBytes, StringBuilder stringBuilder, KeyEncode keyFormat, String headerLine, String footerLine) throws IOException{
        StringBuilderWriter writer = new StringBuilderWriter(stringBuilder);
        writeKey(keyBytes, writer, keyFormat, headerLine, footerLine);
    }


}
