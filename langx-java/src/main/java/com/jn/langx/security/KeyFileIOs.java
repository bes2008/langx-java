package com.jn.langx.security;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.codec.base64.Base64;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.security.exception.KeyFileFormatException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.io.LineDelimiter;

import java.io.*;
import java.security.Key;

public class KeyFileIOs {

    public static byte[] readKeyFileAndBase64Decode(Resource resource) {
        String content = readKeyFile(resource);
        return Base64.decodeBase64(content);
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
        writeKey(key.getEncoded(), file);
    }

    public static void writeKey(@NonNull Key key, @NonNull File file, @Nullable String startLine, @Nullable String endLine) throws IOException {
        writeKey(key.getEncoded(), file, startLine, endLine);
    }


    public static void writeKey(byte[] keyBytes, File file) throws IOException {
        writeKey(keyBytes, file, null, null);
    }

    public static void writeKey(byte[] keyBytes, File file, String startLine, String endLine) throws IOException {
        writeKey(keyBytes, new FileOutputStream(file, true), startLine, endLine);
    }

    public static void writeKey(byte[] keyBytes, OutputStream outputStream, String startLine, String endLine) throws IOException {
        Preconditions.checkNotNull(keyBytes);
        Preconditions.checkNotNull(outputStream);
        outputStream.write(LineDelimiter.DEFAULT.getValue().getBytes());
        if (Strings.isNotBlank(startLine)) {
            outputStream.write(startLine.trim().getBytes(Charsets.UTF_8));
            outputStream.write(LineDelimiter.DEFAULT.getValue().getBytes());
        }

        String base64Key = Base64.encodeBase64String(keyBytes);
        int offset = 0;
        while (offset < base64Key.length()) {
            int toIndex = offset + 64;
            if (toIndex > base64Key.length()) {
                toIndex = base64Key.length();
            }
            outputStream.write(base64Key.substring(offset, toIndex).getBytes(Charsets.UTF_8));
            outputStream.write(LineDelimiter.DEFAULT.getValue().getBytes());
            offset = toIndex;
        }

        if (Strings.isNotBlank(endLine)) {
            outputStream.write(endLine.trim().getBytes(Charsets.UTF_8));
            outputStream.write(LineDelimiter.DEFAULT.getValue().getBytes());
        }

    }
}
