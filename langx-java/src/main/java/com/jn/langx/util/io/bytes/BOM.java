package com.jn.langx.util.io.bytes;


/**
 * Byte Order Mark (BOM)
 * <p>
 * <p>
 * 保存一个以UTF-8编码的文件时，会在文件开始的地方插入三个不可见的字符（0xEF 0xBB 0xBF，即BOM）。它是一串隐藏的字符，用于让记事本等编辑器识别这个文件是否以UTF-8编码
 */
public enum BOM {
    UTF8("UTF-8", new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF}, "EFBBBF"),
    /**
     * 大端
     */
    UTF16_BE("UTF-16-BE", new byte[]{(byte) 0xFE, (byte) 0xFF},"FEFF"),
    /**
     * 小端
     */
    UTF16_LE("UTF-16-LE", new byte[]{(byte) 0xFF, (byte) 0xFE},"FFFE"),
    /**
     * 大端
     */
    UTF32_BE("UTF-32-BE", new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF}, "0000FEFF"),
    /**
     * 小端
     */
    UTF32_LE("UTF-32-LE", new byte[]{(byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFE},  "0000FFFE"),
    ;

    private BOM(String name, byte[] bytes, String checkValue) {
        this.name = name;
        this.bytes = bytes;
        this.checkValue = checkValue;
    }

    private String name;
    private byte[] bytes;
    private String checkValue;

    public String getName() {
        return name;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public String getCheckValue() {
        return checkValue;
    }
}

