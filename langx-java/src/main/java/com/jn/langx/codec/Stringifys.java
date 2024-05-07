package com.jn.langx.codec;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.codec.hex.Hex;
import com.jn.langx.util.Objs;
import com.jn.langx.util.io.Charsets;

/**
 * @since 5.3.9
 */
public class Stringifys {
    public static String stringify(byte[] bytes, StringifyFormat format) {
        format = Objs.useValueIfEmpty(format, StringifyFormat.UTF8);
        String str;
        switch (format) {
            case HEX:
                str = Hex.encodeHexString(bytes);
                break;
            case BASE64:
                str = Base64.encodeBase64String(bytes);
                break;
            default:
                str = new String(bytes, Charsets.UTF_8);
                break;
        }
        return str;
    }
}
