package com.jn.langx.codec;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.codec.hex.Hex;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.io.Charsets;

/**
 * @since 5.3.9
 */
public class Stringifys {
    public static String stringify(byte[] bytes, StringifyFormat format) {
        format = Objs.useValueIfEmpty(format, StringifyFormat.UTF8);
        switch (format) {
            case HEX:
                return Hex.encodeHexString(bytes);
            case BASE64:
                return Base64.encodeBase64ToString(bytes);
            case BASE64URL:
                return Base64.encodeBase64URLSafeString(bytes);
            case ISO_8859_1:
                return Strings.newString(bytes, Charsets.ISO_8859_1);
            case UTF_16BE:
                return Strings.newString(bytes, Charsets.UTF_16BE);
            case UTF_16LE:
                return Strings.newString(bytes, Charsets.UTF_16LE);
            default:
                return Strings.newStringUtf8(bytes);
        }
    }

    public static byte[] toBytes(String text, StringifyFormat format ){
        format = Objs.useValueIfEmpty(format, StringifyFormat.UTF8);
        byte[] bytes;
        switch (format) {
            case HEX:
                bytes = Hex.decodeHex(text);
                break;
            case BASE64:
            case BASE64URL:
                bytes = Base64.decodeBase64(text);
                break;
            case ISO_8859_1:
                bytes= text.getBytes(Charsets.ISO_8859_1);
                break;
            case UTF_16BE:
                bytes= text.getBytes(Charsets.UTF_16BE);
                break;
            case UTF_16LE:
                bytes= text.getBytes(Charsets.UTF_16LE);
                break;
            default:
                bytes = Strings.getBytesUtf8(text);
                break;
        }
        return bytes;
    }
}
