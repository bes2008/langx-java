package com.jn.langx.util.io.unicode;

public final class Utf16s {
    public static final int CODEPOINT_MIN_VALUE = 0x0;
    public static final int CODEPOINT_MAX_VALUE = 0x10FFFF;
    public static final int SUPPLEMENTARY_MIN_VALUE = 65536;
    public static final int LEAD_SURROGATE_MIN_VALUE = 55296;
    public static final int TRAIL_SURROGATE_MIN_VALUE = 56320;
    public static final int LEAD_SURROGATE_MAX_VALUE = 56319;
    public static final int TRAIL_SURROGATE_MAX_VALUE = 57343;
    public static final int SURROGATE_MIN_VALUE = 55296;
    private static final int LEAD_SURROGATE_SHIFT_ = 10;
    private static final int TRAIL_SURROGATE_MASK_ = 1023;
    private static final int LEAD_SURROGATE_OFFSET_ = 55232;

    private Utf16s() {
    }

    public static int charAt(String string, int index) {
        char c = string.charAt(index);
        return c < '\ud800' ? c : _charAt(string, index, c);
    }

    private static int _charAt(String string, int index, char c) {
        if (c > '\udfff') {
            return c;
        } else {
            char var3;
            if (c <= '\udbff') {
                ++index;
                if (string.length() != index) {
                    var3 = string.charAt(index);
                    if (var3 >= '\udc00' && var3 <= '\udfff') {
                        return getRawSupplementary(c, var3);
                    }
                }
            } else {
                --index;
                if (index >= 0) {
                    var3 = string.charAt(index);
                    if (var3 >= '\ud800' && var3 <= '\udbff') {
                        return getRawSupplementary(var3, c);
                    }
                }
            }

            return c;
        }
    }

    public static int charAt(char[] chars, int beginIndex, int endIndex, int index) {
        index += beginIndex;
        if (index >= beginIndex && index < endIndex) {
            char c = chars[index];
            if (!isSurrogate(c)) {
                return c;
            } else {
                char c1;
                if (c <= '\udbff') {
                    ++index;
                    if (index >= endIndex) {
                        return c;
                    }

                    c1 = chars[index];
                    if (isTrailSurrogate(c1)) {
                        return getRawSupplementary(c, c1);
                    }
                } else {
                    if (index == beginIndex) {
                        return c;
                    }

                    --index;
                    c1 = chars[index];
                    if (isLeadSurrogate(c1)) {
                        return getRawSupplementary(c1, c);
                    }
                }

                return c;
            }
        } else {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    public static int getCharCount(int codepoint) {
        return codepoint < 65536 ? 1 : 2;
    }

    public static boolean isSurrogate(char c) {
        return '\ud800' <= c && c <= '\udfff';
    }

    public static boolean isTrailSurrogate(char c) {
        return '\udc00' <= c && c <= '\udfff';
    }

    public static boolean isLeadSurrogate(char c) {
        return '\ud800' <= c && c <= '\udbff';
    }

    public static char getLeadSurrogate(int c) {
        return c >= 65536 ? (char)('ퟀ' + (c >> 10)) : '\u0000';
    }

    public static char getTrailSurrogate(int c) {
        return c >= 65536 ? (char)('\udc00' + (c & 1023)) : (char)c;
    }

    public static String valueOf(int c) {
        if (c >= 0 && c <= 1114111) {
            return toString(c);
        } else {
            throw new IllegalArgumentException("Illegal codepoint");
        }
    }

    public static StringBuilder append(StringBuilder buffer, int c) {
        if (c >= 0 && c <= 1114111) {
            if (c >= 65536) {
                buffer.append(getLeadSurrogate(c));
                buffer.append(getTrailSurrogate(c));
            } else {
                buffer.append((char)c);
            }

            return buffer;
        } else {
            throw new IllegalArgumentException("Illegal codepoint: " + Integer.toHexString(c));
        }
    }

    public static int moveCodePointOffset(char[] chars, int var1, int var2, int var3, int var4) {
        int length = chars.length;
        int index = var3 + var1;
        if (var1 >= 0 && var2 >= var1) {
            if (var2 > length) {
                throw new StringIndexOutOfBoundsException(var2);
            } else if (var3 >= 0 && index <= var2) {
                int var6;
                char var7;
                if (var4 > 0) {
                    if (var4 + index > length) {
                        throw new StringIndexOutOfBoundsException(index);
                    }

                    for(var6 = var4; index < var2 && var6 > 0; ++index) {
                        var7 = chars[index];
                        if (isLeadSurrogate(var7) && index + 1 < var2 && isTrailSurrogate(chars[index + 1])) {
                            ++index;
                        }

                        --var6;
                    }
                } else {
                    if (index + var4 < var1) {
                        throw new StringIndexOutOfBoundsException(index);
                    }

                    for(var6 = -var4; var6 > 0; --var6) {
                        --index;
                        if (index < var1) {
                            break;
                        }

                        var7 = chars[index];
                        if (isTrailSurrogate(var7) && index > var1 && isLeadSurrogate(chars[index - 1])) {
                            --index;
                        }
                    }
                }

                if (var6 != 0) {
                    throw new StringIndexOutOfBoundsException(var4);
                } else {
                    index -= var1;
                    return index;
                }
            } else {
                throw new StringIndexOutOfBoundsException(var3);
            }
        } else {
            throw new StringIndexOutOfBoundsException(var1);
        }
    }

    private static String toString(int c) {
        if (c < 65536) {
            return String.valueOf((char)c);
        } else {
            StringBuilder buffer = new StringBuilder();
            buffer.append(getLeadSurrogate(c));
            buffer.append(getTrailSurrogate(c));
            return buffer.toString();
        }
    }

    public static int getRawSupplementary(char var0, char var1) {
        return (var0 << 10) + var1 + -56613888;
    }

}
