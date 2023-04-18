package com.jn.langx.codec.base62;

import com.jn.langx.io.stream.BufferExposingByteArrayOutputStream;
import com.jn.langx.util.io.IOs;

public final class Base62 {
    private static final int STANDARD_BASE = 256;

    private static final int TARGET_BASE = 62;

    private static final byte[] lookup = new byte[256];

    private static final byte[] alphabet = new byte[]{
            48, 49, 50, 51, 52, 53, 54, 55, 56, 57,
            65, 66, 67, 68, 69, 70, 71, 72, 73, 74,
            75, 76, 77, 78, 79, 80, 81, 82, 83, 84,
            85, 86, 87, 88, 89, 90, 97, 98, 99, 100,
            101, 102, 103, 104, 105, 106, 107, 108, 109, 110,
            111, 112, 113, 114, 115, 116, 117, 118, 119, 120,
            121, 122};

    static {
        for (int i = 0; i < alphabet.length; i++)
            lookup[alphabet[i]] = (byte) (i & 0xFF);
    }

    public static byte[] encode(byte[] message) {
        byte[] indices = convert(message, 256, 62);
        return translate(indices, alphabet);
    }

    public static byte[] decode(byte[] encoded) {
        byte[] prepared = translate(encoded, lookup);
        return convert(prepared, 62, 256);
    }

    private static byte[] translate(byte[] indices, byte[] dictionary) {
        byte[] translation = new byte[indices.length];
        for (int i = 0; i < indices.length; i++)
            translation[i] = dictionary[indices[i]];
        return translation;
    }

    private static byte[] convert(byte[] message, int sourceBase, int targetBase) {
        int estimatedLength = estimateOutputLength(message.length, sourceBase, targetBase);
        BufferExposingByteArrayOutputStream out = null;
        int length = 0;
        byte[] array = null;
        try {
            out = new BufferExposingByteArrayOutputStream(estimatedLength);
            byte[] source = message;
            while (source.length > 0) {
                BufferExposingByteArrayOutputStream quotient = null;
                try {
                    quotient = new BufferExposingByteArrayOutputStream(source.length);
                    int remainder = 0;
                    for (byte b : source) {
                        int accumulator = (b & 0xFF) + remainder * sourceBase;
                        int digit = (accumulator - accumulator % targetBase) / targetBase;
                        remainder = accumulator % targetBase;
                        if (quotient.size() > 0 || digit > 0)
                            quotient.write(digit);
                    }
                    out.write(remainder);
                    source = quotient.toByteArray();
                } finally {
                    IOs.close(quotient);
                }

            }
            for (int i = 0; i < message.length - 1 && message[i] == 0; i++) {
                out.write(0);
            }
            length = out.size();
            array = out.getInternalBuffer();
        } finally {
            IOs.close(out);
        }

        int k = 0;
        int j = length - 1;
        while (j > k) {
            byte tmp = array[j];
            array[j] = array[k];
            array[k] = tmp;
            j--;
            k++;
        }
        return array;
    }

    private static int estimateOutputLength(int inputLength, int sourceBase, int targetBase) {
        return (int) Math.ceil(Math.log(sourceBase) / Math.log(targetBase) * inputLength);
    }
}