package com.jn.langx.util;

import com.jn.langx.util.collection.Arrs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Emptys {


    public static final int[] EMPTY_INTS = {};
    public static final byte[] EMPTY_BYTES = {};
    public static final char[] EMPTY_CHARS = {};
    public static final Object[] EMPTY_OBJECTS = {};
    public static final Class<?>[] EMPTY_CLASSES = {};
    public static final String[] EMPTY_STRINGS = {};
    public static final StackTraceElement[] EMPTY_STACK_TRACE = {};
    public static final ByteBuffer[] EMPTY_BYTE_BUFFERS = {};
    public static final Certificate[] EMPTY_CERTIFICATES = {};
    public static final X509Certificate[] EMPTY_X509_CERTIFICATES = {};
    public static final javax.security.cert.X509Certificate[] EMPTY_JAVAX_X509_CERTIFICATES = {};

    private static boolean isZero(Number number) {
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
            return number.floatValue() == 0F;
        }
        if (number instanceof Double) {
            return number.doubleValue() == 0D;
        }
        if (number instanceof AtomicInteger) {
            return ((AtomicInteger) number).get() == 0;
        }
        if (number instanceof BigInteger) {
            return ((BigInteger) number).intValue() == 0;
        }
        if (number instanceof AtomicLong) {
            return number.longValue() == 0L;
        }
        if (number instanceof BigDecimal) {
            return new BigDecimal(0).equals(number);
        }
        return false;
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return Strings.isEmpty((String) object);
        }

        if (object instanceof CharSequence) {
            CharSequence cs = (CharSequence) object;
            return cs.length() == 0;
        }

        if (object instanceof Number) {
            return isZero((Number) object);
        }

        if (object instanceof Buffer) {
            Buffer buff = (Buffer) object;
            return buff.hasRemaining();
        }

        if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        }

        if (object instanceof Map) {
            return ((Map) object).isEmpty();
        }
        if (object.getClass().isArray()) {
            return Arrs.getLength(object) <= 0;
        }

        if (object.getClass().isEnum()) {
            return false;
        }

        return false;
    }

    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isNotNull(Object o) {
        return o != null;
    }

}
