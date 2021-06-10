package com.jn.langx.codec.z85;

import com.jn.langx.codec.CodecException;
import com.jn.langx.codec.Encoder;
import static com.jn.langx.codec.z85.Z85.encodeTable;

public class Z85Encoder implements Encoder<byte[], String> {
    @Override
    public String encode(byte[] bytes) throws CodecException {
        int length = bytes.length;
        int remainder = length % 4;
        int padding = 4 - (remainder == 0 ? 4 : remainder);
        StringBuilder result = new StringBuilder();
        long value = 0;
        for (int i = 0; i < length + padding; ++i) {
            boolean isPadding = i >= length;
            value = value * 256 + (isPadding ? 0 : bytes[i] & 0xFF);
            if ((i + 1) % 4 == 0) {
                int divisor = 85 * 85 * 85 * 85;
                for (int j = 5; j > 0; --j) {
                    if (!isPadding || j > padding) {
                        int code = (int) ((value / divisor) % 85);
                        result.append(encodeTable[code]);
                    }
                    divisor /= 85;
                }
                value = 0;
            }
        }
        return result.toString();
    }
}
