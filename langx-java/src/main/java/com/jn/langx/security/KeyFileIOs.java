package com.jn.langx.security;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.security.exception.KeyFileFormatException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.io.IOs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
