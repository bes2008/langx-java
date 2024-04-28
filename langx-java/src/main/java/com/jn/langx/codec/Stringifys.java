package com.jn.langx.codec;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.codec.hex.Hex;
import com.jn.langx.util.Objs;

/**
 * @since 5.3.9
 */
public class Stringifys {
    public static String stringify(byte[] bytes, StringifyFormat format){
        format= Objs.useValueIfEmpty(format, StringifyFormat.BASE64);
        String str;
        switch (format){
            case HEX:
                str= Hex.encodeHexString(bytes);
                break;
            case BASE64:
            default:
                str= Base64.encodeBase64String(bytes);
                break;
        }
        return str;
    }
}
