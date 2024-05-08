package com.jn.langx.util;


import com.jn.langx.annotation.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Numbers {
    private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);

    private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

    private Numbers() {

    }

    /**
     * Parses the string argument as an unsigned decimal integer. The
     * characters in the string must all be decimal digits, except
     * that the first character may be an an ASCII plus sign {@code
     * '+'} ({@code '\u005Cu002B'}). The resulting integer value
     * is returned, exactly as if the argument and the radix 10 were
     * given as arguments to the {@link
     * #parseUnsignedInt(java.lang.String, int)} method.
     *
     * @param s a {@code String} containing the unsigned {@code int}
     *          representation to be parsed
     * @return the unsigned integer value represented by the argument in decimal.
     * @throws NumberFormatException if the string does not contain a
     *                               parsable unsigned integer.
     * @since 4.2.0
     */
    public static int parseUnsignedInt(String s) throws NumberFormatException {
        return parseUnsignedInt(s, 10);
    }

    public static int parseUnsignedInt(String s, int radix)
            throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }

        int len = s.length();
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar == '-') {
                throw new
                        NumberFormatException(String.format("Illegal leading minus sign " +
                        "on unsigned string %s.", s));
            } else {
                if (len <= 5 || // Integer.MAX_VALUE in Character.MAX_RADIX is 6 digits
                        (radix == 10 && len <= 9)) { // Integer.MAX_VALUE in base 10 is 10 digits
                    return Integer.parseInt(s, radix);
                } else {
                    long ell = Long.parseLong(s, radix);
                    if ((ell & 0xffffffff00000000L) == 0) {
                        return (int) ell;
                    } else {
                        throw new
                                NumberFormatException(String.format("String value %s exceeds " +
                                "range of unsigned int.", s));
                    }
                }
            }
        } else {
            throw new NumberFormatException(s);
        }
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Float</code>.</p>
     * <p>
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     *
     * @param str a <code>String</code> to convert, may be null
     * @return converted <code>Float</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Float createFloat(String str) {
        if (str == null) {
            return null;
        }
        return Float.parseFloat(str);
    }

    public static Float createFloat(Object d) {
        if (d == null) {
            return null;
        }
        if (d instanceof Number) {
            return ((Number) d).floatValue();
        }
        if (d instanceof String) {
            return createFloat((String) d);
        }
        return null;
    }


    /**
     * <p>Convert a <code>String</code> to a <code>Double</code>.</p>
     * <p>
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     *
     * @param str a <code>String</code> to convert, may be null
     * @return converted <code>Double</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Double createDouble(String str) {
        if (str == null) {
            return null;
        }
        return Double.parseDouble(str);
    }

    public static Double createDouble(Object d) {
        if (d == null) {
            return null;
        }
        if (d instanceof Number) {
            return ((Number) d).doubleValue();
        }
        if (d instanceof String) {
            return createDouble((String) d);
        }
        return null;
    }

    public static Integer createInteger(String str) {
        if (str == null) {
            return null;
        }
        // decode() handles 0xAABD and 0777 (hex and octal) as well.
        return Integer.parseInt(str);
    }

    public static Integer createInteger(Object d) {
        if (d == null) {
            return null;
        }
        if (d instanceof Number) {
            return ((Number) d).intValue();
        }
        if (d instanceof String) {
            return createInteger((String) d);
        }
        return null;
    }


    public static Short createShort(String str) {
        Integer integer = createInteger(str);
        if (integer == null) {
            return null;
        }
        return integer.shortValue();
    }

    public static Byte createByte(String str) {
        Integer integer = createInteger(str);
        if (integer == null) {
            return null;
        }
        return integer.byteValue();
    }

    /**
     * <p>Convert a <code>String</code> to a <code>Long</code>.</p>
     * <p>
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     *
     * @param str a <code>String</code> to convert, may be null
     * @return converted <code>Long</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static Long createLong(String str) {
        if (str == null) {
            return null;
        }
        return Long.parseLong(str);
    }

    public static Long createLong(Object d) {
        if (d == null) {
            return null;
        }
        if (d instanceof Number) {
            return ((Number) d).longValue();
        }
        if (d instanceof String) {
            return createLong((String) d);
        }
        return null;
    }

    /**
     * <p>Convert a <code>String</code> to a <code>BigInteger</code>.</p>
     * <p>
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     *
     * @param str a <code>String</code> to convert, may be null
     * @return converted <code>BigInteger</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static BigInteger createBigInteger(String str) {
        if (str == null) {
            return null;
        }
        return new BigInteger(str);
    }

    /**
     * <p>Convert a <code>String</code> to a <code>BigDecimal</code>.</p>
     * <p>
     * <p>Returns <code>null</code> if the string is <code>null</code>.</p>
     *
     * @param str a <code>String</code> to convert, may be null
     * @return converted <code>BigDecimal</code>
     * @throws NumberFormatException if the value cannot be converted
     */
    public static BigDecimal createBigDecimal(String str) {
        if (str == null) {
            return null;
        }
        // handle JDK1.3.1 bug where "" throws IndexOutOfBoundsException
        if (Strings.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        return new BigDecimal(str);
    }

    /**
     * 数字转{@link BigDecimal}<br>
     * Float、Double等有精度问题，转换为字符串后再转换<br>
     * null转换为0
     *
     * @param number 数字
     * @return {@link BigDecimal}
     * @since 4.6.13
     */
    public static BigDecimal toBigDecimal(Number number) {
        if (null == number) {
            return BigDecimal.ZERO;
        }

        if (number instanceof BigDecimal) {
            return (BigDecimal) number;
        } else if (number instanceof Long) {
            return new BigDecimal((Long) number);
        } else if (number instanceof Integer) {
            return new BigDecimal((Integer) number);
        } else if (number instanceof BigInteger) {
            return new BigDecimal((BigInteger) number);
        }

        // Float、Double等有精度问题，转换为字符串后再转换
        return createBigDecimal(number.toString());
    }


    /**
     * 判断是否为0
     *
     * @return 如为 0， 则返回true
     */
    public static boolean isZero(Number number) {
        if (number == null) {
            return true;
        }
        if (number instanceof Byte) {
            return number.equals(Byte.valueOf("0"));
        }
        if (number instanceof Short) {
            return number.equals(Short.valueOf("0"));
        }
        if (number instanceof Integer) {
            return number.intValue() == 0;
        }
        if (number instanceof Long) {
            return number.longValue() == 0L;
        }
        if (number instanceof Float) {
            return ((Float)number).floatValue() == 0F;
        }
        if (number instanceof Double) {
            return ((Double)number).doubleValue() == 0D;
        }
        if (number instanceof AtomicInteger) {
            return ((AtomicInteger) number).get() == 0;
        }
        if (number instanceof BigInteger) {
            return number.intValue() == 0;
        }
        if (number instanceof AtomicLong) {
            return number.longValue() == 0L;
        }
        if (number instanceof BigDecimal) {
            return new BigDecimal(0).equals(number);
        }
        return false;
    }

    private static boolean isAllZeros(String str) {
        if (str == null) {
            return true;
        }
        for (int i = str.length() - 1; i >= 0; i--) {
            if (str.charAt(i) != '0') {
                return false;
            }
        }
        return str.length() > 0;
    }

    /**
     * <p>Checks whether the <code>String</code> contains only
     * digit characters.</p>
     * <p>
     * <p><code>Null</code> and empty String will return
     * <code>false</code>.</p>
     *
     * @param str the <code>String</code> to check
     * @return <code>true</code> if str contains only unicode numeric
     */
    public static boolean isDigits(String str) {
        if (Strings.isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks whether the String a valid Java number.</p>
     * <p>
     * <p>Valid numbers include hexadecimal marked with the <code>0x</code>
     * qualifier, scientific notation and numbers marked with a type
     * qualifier (e.g. 123L).</p>
     * <p>
     * <p><code>Null</code> and empty String will return
     * <code>false</code>.</p>
     *
     * @param str the <code>String</code> to check
     * @return <code>true</code> if the string is a correctly formatted number
     */
    public static boolean isNumber(String str) {
        if (Strings.isEmpty(str)) {
            return false;
        }
        char[] chars = str.toCharArray();
        int sz = chars.length;
        boolean hasExp = false;
        boolean hasDecPoint = false;
        boolean allowSigns = false;
        boolean foundDigit = false;
        // deal with any possible sign up front
        int start = (chars[0] == '-') ? 1 : 0;
        if (sz > start + 1 && chars[start] == '0' && chars[start + 1] == 'x') {
            int i = start + 2;
            if (i == sz) {
                return false; // str == "0x"
            }
            // checking hex (it can't be anything else)
            for (; i < chars.length; i++) {
                if ((chars[i] < '0' || chars[i] > '9')
                        && (chars[i] < 'a' || chars[i] > 'f')
                        && (chars[i] < 'A' || chars[i] > 'F')) {
                    return false;
                }
            }
            return true;
        }
        sz--; // don't want to loop to the last char, check it afterwords
        // for type qualifiers
        int i = start;
        // loop to the next to last char or to the last char if we need another digit to
        // make a valid number (e.g. chars[0..5] = "1234E")
        while (i < sz || (i < sz + 1 && allowSigns && !foundDigit)) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                foundDigit = true;
                allowSigns = false;

            } else if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                hasDecPoint = true;
            } else if (chars[i] == 'e' || chars[i] == 'E') {
                // we've already taken care of hex.
                if (hasExp) {
                    // two E's
                    return false;
                }
                if (!foundDigit) {
                    return false;
                }
                hasExp = true;
                allowSigns = true;
            } else if (chars[i] == '+' || chars[i] == '-') {
                if (!allowSigns) {
                    return false;
                }
                allowSigns = false;
                foundDigit = false; // we need a digit after the E
            } else {
                return false;
            }
            i++;
        }
        if (i < chars.length) {
            if (chars[i] >= '0' && chars[i] <= '9') {
                // no type qualifier, OK
                return true;
            }
            if (chars[i] == 'e' || chars[i] == 'E') {
                // can't have an E at the last byte
                return false;
            }
            if (chars[i] == '.') {
                if (hasDecPoint || hasExp) {
                    // two decimal points or dec in exponent
                    return false;
                }
                // single trailing decimal point after non-exponent is ok
                return foundDigit;
            }
            if (!allowSigns
                    && (chars[i] == 'd'
                    || chars[i] == 'D'
                    || chars[i] == 'f'
                    || chars[i] == 'F')) {
                return foundDigit;
            }
            if (chars[i] == 'l'
                    || chars[i] == 'L') {
                // not allowing L with an exponent
                return foundDigit && !hasExp;
            }
            // last character is illegal
            return false;
        }
        // allowSigns is true iff the val ends in 'E'
        // found digit it to make sure weird stuff like '.' and '1E-' doesn't pass
        return !allowSigns && foundDigit;
    }

    public static Number createNumber(String str) throws NumberFormatException {
        if (str == null) {
            return null;
        }
        if (Strings.isBlank(str)) {
            throw new NumberFormatException("A blank string is not a valid number");
        }
        if (str.startsWith("--")) {
            // this is protection for poorness in java.lang.BigDecimal.
            // it accepts this as a legal value, but it does not appear
            // to be in specification of class. OS X Java parses it to
            // a wrong value.
            return null;
        }
        if (str.startsWith("0x") || str.startsWith("-0x")) {
            return createInteger(str);
        }
        char lastChar = str.charAt(str.length() - 1);
        String mant;
        String dec;
        String exp;
        int decPos = str.indexOf('.');
        int expPos = str.indexOf('e') + str.indexOf('E') + 1;

        if (decPos > -1) {

            if (expPos > -1) {
                if (expPos < decPos) {
                    throw new NumberFormatException(str + " is not a valid number.");
                }
                dec = str.substring(decPos + 1, expPos);
            } else {
                dec = str.substring(decPos + 1);
            }
            mant = str.substring(0, decPos);
        } else {
            if (expPos > -1) {
                mant = str.substring(0, expPos);
            } else {
                mant = str;
            }
            dec = null;
        }
        if (!Character.isDigit(lastChar) && lastChar != '.') {
            if (expPos > -1 && expPos < str.length() - 1) {
                exp = str.substring(expPos + 1, str.length() - 1);
            } else {
                exp = null;
            }
            //Requesting a specific type..
            String numeric = str.substring(0, str.length() - 1);
            if (Strings.isEmpty(numeric)) {
                return 0;
            }
            boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
            Number number = null;
            switch (lastChar) {
                case 'l':
                case 'L':
                    if (dec == null
                            && exp == null
                            && (numeric.charAt(0) == '-' && isDigits(numeric.substring(1)) || isDigits(numeric))) {
                        try {
                            number = createLong(numeric);
                            break;
                        } catch (NumberFormatException nfe) {
                            //Too big for a long
                        }
                        number = createBigInteger(numeric);
                    }
                    break;
                case 'f':
                case 'F':
                    try {
                        Float f = createFloat(numeric);
                        if (!(f.isInfinite() || (f == 0.0F && !allZeros))) {
                            number = f;
                        }
                    } catch (NumberFormatException nfe) {
                        // ignore the bad number
                    }
                    break;
                case 'd':
                case 'D':
                    try {
                        Double d = createDouble(numeric);
                        if (!(d.isInfinite() || (d.floatValue() == 0.0D && !allZeros))) {
                            number = d;
                            break;
                        }
                    } catch (NumberFormatException nfe) {
                        // ignore it
                    }
                    try {
                        number = createBigDecimal(numeric);
                    } catch (NumberFormatException e) {
                        // ignore it
                    }
                    break;
                default:
                    break;
            }
            if (number == null) {
                throw new NumberFormatException(str + " is not a valid number.");
            } else {
                return number;
            }

        } else {
            //User doesn't have a preference on the return type, so let's start
            //small and go from there...
            if (expPos > -1 && expPos < str.length() - 1) {
                exp = str.substring(expPos + 1);
            } else {
                exp = null;
            }
            if (dec == null && exp == null) {
                //Must be an int,long,bigint
                try {
                    return createInteger(str);
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                try {
                    return createLong(str);
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                return createBigInteger(str);

            } else {
                //Must be a float,double,BigDec
                boolean allZeros = isAllZeros(mant) && isAllZeros(exp);
                try {
                    Float f = createFloat(str);
                    if (!(f.isInfinite() || (f == 0.0F && !allZeros))) {
                        return f;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }
                try {
                    Double d = createDouble(str);
                    if (!(d.isInfinite() || (d == 0.0D && !allZeros))) {
                        return d;
                    }
                } catch (NumberFormatException nfe) {
                    // ignore the bad number
                }

                return createBigDecimal(str);

            }
        }
    }

    /**
     * Parse the given {@code text} into a {@link Number} instance of the given
     * target class, using the corresponding {@code decode} / {@code valueOf} method.
     * <p>Trims all whitespace (leading, trailing, and in between characters) from
     * the input {@code String} before attempting to parse the number.
     * <p>Supports numbers in hex format (with leading "0x", "0X", or "#") as well.
     *
     * @param text        the text to convert
     * @param targetClass the target class to parse into
     * @return the parsed number
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see Byte#decode
     * @see Short#decode
     * @see Integer#decode
     * @see Long#decode
     * @see #decodeBigInteger(String)
     * @see #createFloat(String)
     * @see #createDouble(String)
     * @see java.math.BigDecimal#BigDecimal(String)
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
        Preconditions.checkNotNull(text, "Text must not be null");
        Preconditions.checkNotNull(targetClass, "Target class must not be null");
        String trimmed = Strings.deleteWhitespace(text);

        if (Byte.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Byte.decode(trimmed) : createByte(trimmed));
        } else if (Short.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Short.decode(trimmed) : (T) createShort(trimmed));
        } else if (Integer.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Integer.decode(trimmed) : createInteger(trimmed));
        } else if (Long.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Long.decode(trimmed) : createLong(trimmed));
        } else if (BigInteger.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? decodeBigInteger(trimmed) : createBigInteger(trimmed));
        } else if (Float.class == targetClass) {
            return (T) createFloat(trimmed);
        } else if (Double.class == targetClass) {
            return (T) createDouble(trimmed);
        } else if (BigDecimal.class == targetClass || Number.class == targetClass) {
            return (T) createBigDecimal(trimmed);
        } else {
            throw new IllegalArgumentException(
                    "Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
        }
    }

    /**
     * Parse the given {@code text} into a {@link Number} instance of the
     * given target class, using the supplied {@link NumberFormat}.
     * <p>Trims the input {@code String} before attempting to parse the number.
     *
     * @param text         the text to convert
     * @param targetClass  the target class to parse into
     * @param numberFormat the {@code NumberFormat} to use for parsing (if
     *                     {@code null}, this method falls back to {@link #parseNumber(String, Class)})
     * @return the parsed number
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see java.text.NumberFormat#parse
     * @see #convertNumberToTargetClass
     * @see #parseNumber(String, Class)
     */
    public static <T extends Number> T parseNumber(
            String text, Class<T> targetClass, @Nullable NumberFormat numberFormat) {

        if (numberFormat != null) {
            Preconditions.checkNotNull(text, "Text must not be null");
            Preconditions.checkNotNull(targetClass, "Target class must not be null");
            DecimalFormat decimalFormat = null;
            boolean resetBigDecimal = false;
            if (numberFormat instanceof DecimalFormat) {
                decimalFormat = (DecimalFormat) numberFormat;
                if (BigDecimal.class == targetClass && !decimalFormat.isParseBigDecimal()) {
                    decimalFormat.setParseBigDecimal(true);
                    resetBigDecimal = true;
                }
            }
            try {
                Number number = numberFormat.parse(Strings.deleteWhitespace(text));
                return convertNumberToTargetClass(number, targetClass);
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Could not parse number: " + ex.getMessage());
            } finally {
                if (resetBigDecimal && decimalFormat!=null) {
                    decimalFormat.setParseBigDecimal(false);
                }
            }
        } else {
            return parseNumber(text, targetClass);
        }
    }

    /**
     * Convert the given number into an instance of the given target class.
     *
     * @param number      the number to convert
     * @param targetClass the target class to convert to
     * @return the converted number
     * @throws IllegalArgumentException if the target class is not supported
     *                                  (i.e. not a standard Number subclass as included in the JDK)
     * @see java.lang.Byte
     * @see java.lang.Short
     * @see java.lang.Integer
     * @see java.lang.Long
     * @see java.math.BigInteger
     * @see java.lang.Float
     * @see java.lang.Double
     * @see java.math.BigDecimal
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertNumberToTargetClass(Number number, Class<T> targetClass)
            throws IllegalArgumentException {

        Preconditions.checkNotNull(number, "Number must not be null");
        Preconditions.checkNotNull(targetClass, "Target class must not be null");

        if (targetClass.isInstance(number)) {
            return (T) number;
        } else if (Byte.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) Byte.valueOf(number.byteValue());
        } else if (Short.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Short.MIN_VALUE || value > Short.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) Short.valueOf(number.shortValue());
        } else if (Integer.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            if (value < Integer.MIN_VALUE || value > Integer.MAX_VALUE) {
                raiseOverflowException(number, targetClass);
            }
            return (T) Integer.valueOf(number.intValue());
        } else if (Long.class == targetClass) {
            long value = checkedLongValue(number, targetClass);
            return (T) Long.valueOf(value);
        } else if (BigInteger.class == targetClass) {
            if (number instanceof BigDecimal) {
                // do not lose precision - use BigDecimal's own conversion
                return (T) ((BigDecimal) number).toBigInteger();
            } else {
                // original value is not a Big* number - use standard long conversion
                return (T) BigInteger.valueOf(number.longValue());
            }
        } else if (Float.class == targetClass) {
            return (T) Float.valueOf(number.floatValue());
        } else if (Double.class == targetClass) {
            return (T) Double.valueOf(number.doubleValue());
        } else if (BigDecimal.class == targetClass) {
            // always use BigDecimal(String) here to avoid unpredictability of BigDecimal(double)
            // (see BigDecimal javadoc for details)
            return (T) new BigDecimal(number.toString());
        } else {
            throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                    number.getClass().getName() + "] to unsupported target class [" + targetClass.getName() + "]");
        }
    }

    /**
     * Raise an <em>overflow</em> exception for the given number and target class.
     *
     * @param number      the number we tried to convert
     * @param targetClass the target class we tried to convert to
     * @throws IllegalArgumentException if there is an overflow
     */
    private static void raiseOverflowException(Number number, Class<?> targetClass) {
        throw new IllegalArgumentException("Could not convert number [" + number + "] of type [" +
                number.getClass().getName() + "] to target class [" + targetClass.getName() + "]: overflow");
    }

    /**
     * Check for a {@code BigInteger}/{@code BigDecimal} long overflow
     * before returning the given number as a long value.
     *
     * @param number      the number to convert
     * @param targetClass the target class to convert to
     * @return the long value, if convertible without overflow
     * @throws IllegalArgumentException if there is an overflow
     * @see #raiseOverflowException
     */
    private static long checkedLongValue(Number number, Class<? extends Number> targetClass) {
        BigInteger bigInt = null;
        if (number instanceof BigInteger) {
            bigInt = (BigInteger) number;
        } else if (number instanceof BigDecimal) {
            bigInt = ((BigDecimal) number).toBigInteger();
        }
        // Effectively analogous to JDK 8's BigInteger.longValueExact()
        if (bigInt != null && (bigInt.compareTo(LONG_MIN) < 0 || bigInt.compareTo(LONG_MAX) > 0)) {
            raiseOverflowException(number, targetClass);
        }
        return number.longValue();
    }


    /**
     * Determine whether the given {@code value} String indicates a hex number,
     * i.e. needs to be passed into {@code Integer.decode} instead of
     * {@code Integer.valueOf}, etc.
     */
    private static boolean isHexNumber(String value) {
        int index = (value.startsWith("-") ? 1 : 0);
        return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
    }

    /**
     * Decode a {@link java.math.BigInteger} from the supplied {@link String} value.
     * <p>Supports decimal, hex, and octal notation.
     *
     * @see BigInteger#BigInteger(String, int)
     */
    private static BigInteger decodeBigInteger(String value) {
        int radix = 10;
        int index = 0;
        boolean negative = false;

        // Handle minus sign, if present.
        if (value.startsWith("-")) {
            negative = true;
            index++;
        }

        // Handle radix specifier, if present.
        if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        } else if (value.startsWith("#", index)) {
            index++;
            radix = 16;
        } else if (value.startsWith("0", index) && value.length() > 1 + index) {
            index++;
            radix = 8;
        }

        BigInteger result = new BigInteger(value.substring(index), radix);
        return (negative ? result.negate() : result);
    }

    public static int toInt(Number number) {
        return number.intValue();
    }

    public static long toLong(Number number) {
        return number.longValue();
    }

    public static double toDouble(Number number) {
        return number.doubleValue();
    }

    public static float toFloat(Number number) {
        return number.floatValue();
    }

    public static short toShort(Number number) {
        return number.shortValue();
    }

    public static byte toByte(Number number) {
        return number.byteValue();
    }

    public static Number mod(Number left, Number right) {
        if (isDouble(left) || isDouble(right)) {
            return left.doubleValue() % right.doubleValue();
        }
        return left.longValue() % right.longValue();
    }

    public static Number add(Number left, Number right) {
        if (isDouble(left) || isDouble(right)) {
            return left.doubleValue() + right.doubleValue();
        }
        return left.longValue() + right.longValue();
    }

    public static Number sub(Number left, Number right) {
        if (isDouble(left) || isDouble(right)) {
            return left.doubleValue() - right.doubleValue();
        }
        return left.longValue() - right.longValue();
    }

    public static Number mul(Number left, Number right) {
        if (isDouble(left) || isDouble(right)) {
            return left.doubleValue() * right.doubleValue();
        }
        return left.longValue() * right.longValue();
    }

    public static Number div(Number left, Number right) {
        if (isDouble(left) || isDouble(right)) {
            return left.doubleValue() / right.doubleValue();
        }
        return left.longValue() / right.longValue();
    }

    public static boolean isInteger(Number number) {
        if (number instanceof Integer) {
            return true;
        }
        return number.longValue() == number.intValue();
    }

    public static boolean isDouble(Number number) {
        if (number instanceof Double) {
            return true;
        }
        return Strings.containsAny(number.toString(), ".");
    }

    public static boolean isLong(Number number) {
        if (number instanceof Long) {
            return true;
        }
        return !isDouble(number) && !isInteger(number);
    }


    /**
     * Compares two {@code long} values numerically.
     * The value returned is identical to what would be returned by:
     * <pre>
     *    Long.valueOf(x).compareTo(Long.valueOf(y))
     * </pre>
     *
     * @param x the first {@code long} to compare
     * @param y the second {@code long} to compare
     * @return the value {@code 0} if {@code x == y};
     * a value less than {@code 0} if {@code x < y}; and
     * a value greater than {@code 0} if {@code x > y}
     * @since 4.4.7
     */
    public static int compare(long x, long y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }


    /**
     * Compares two {@code int} values numerically.
     * The value returned is identical to what would be returned by:
     * <pre>
     *    Integer.valueOf(x).compareTo(Integer.valueOf(y))
     * </pre>
     *
     * @param x the first {@code int} to compare
     * @param y the second {@code int} to compare
     * @return the value {@code 0} if {@code x == y};
     * a value less than {@code 0} if {@code x < y}; and
     * a value greater than {@code 0} if {@code x > y}
     * @since 4.4.7
     */
    public static int compare(int x, int y) {
        return (x < y) ? -1 : ((x == y) ? 0 : 1);
    }


    /**
     * Compares two {@code byte} values numerically.
     * The value returned is identical to what would be returned by:
     * <pre>
     *    Byte.valueOf(x).compareTo(Byte.valueOf(y))
     * </pre>
     *
     * @param x the first {@code byte} to compare
     * @param y the second {@code byte} to compare
     * @return the value {@code 0} if {@code x == y};
     * a value less than {@code 0} if {@code x < y}; and
     * a value greater than {@code 0} if {@code x > y}
     * @since 4.4.7
     */
    public static int compare(byte x, byte y) {
        return x - y;
    }


    /**
     * Compares two {@code char} values numerically.
     * The value returned is identical to what would be returned by:
     * <pre>
     *    Character.valueOf(x).compareTo(Character.valueOf(y))
     * </pre>
     *
     * @param x the first {@code char} to compare
     * @param y the second {@code char} to compare
     * @return the value {@code 0} if {@code x == y};
     * a value less than {@code 0} if {@code x < y}; and
     * a value greater than {@code 0} if {@code x > y}
     * @since 4.4.7
     */
    public static int compare(char x, char y) {
        return x - y;
    }


    /**
     * Compares two {@code short} values numerically.
     * The value returned is identical to what would be returned by:
     * <pre>
     *    Short.valueOf(x).compareTo(Short.valueOf(y))
     * </pre>
     *
     * @param x the first {@code short} to compare
     * @param y the second {@code short} to compare
     * @return the value {@code 0} if {@code x == y};
     * a value less than {@code 0} if {@code x < y}; and
     * a value greater than {@code 0} if {@code x > y}
     * @since 4.4.7
     */
    public static int compare(short x, short y) {
        return x - y;
    }

    /**
     * Compares the two specified {@code float} values. The sign
     * of the integer value returned is the same as that of the
     * integer that would be returned by the call:
     * <pre>
     *    new Float(f1).compareTo(new Float(f2))
     * </pre>
     *
     * @param f1 the first {@code float} to compare.
     * @param f2 the second {@code float} to compare.
     * @return the value {@code 0} if {@code f1} is
     * numerically equal to {@code f2}; a value less than
     * {@code 0} if {@code f1} is numerically less than
     * {@code f2}; and a value greater than {@code 0}
     * if {@code f1} is numerically greater than
     * {@code f2}.
     * @since 4.4.7
     */
    public static int compare(float f1, float f2) {
        return Float.compare(f1, f2);
    }

    /**
     * Compares the two specified {@code double} values. The sign
     * of the integer value returned is the same as that of the
     * integer that would be returned by the call:
     * <pre>
     *    new Double(d1).compareTo(new Double(d2))
     * </pre>
     *
     * @param d1 the first {@code double} to compare
     * @param d2 the second {@code double} to compare
     * @return the value {@code 0} if {@code d1} is
     * numerically equal to {@code d2}; a value less than
     * {@code 0} if {@code d1} is numerically less than
     * {@code d2}; and a value greater than {@code 0}
     * if {@code d1} is numerically greater than
     * {@code d2}.
     * @since 4.4.7
     */
    public static int compare(double d1, double d2) {
        return Double.compare(d1, d2);
    }

}
