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
            case ISO_8859_1:
                return Strings.newString(bytes, Charsets.ISO_8859_1);
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
                bytes = Base64.decodeBase64(text);
                break;
            default:
                bytes = Strings.getBytesUtf8(text);
                break;
        }
        return bytes;
    }
}
