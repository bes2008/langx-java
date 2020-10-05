package com.jn.langx.util;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.struct.Reference;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Map;

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
            return Numbers.isZero((Number) object);
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

        if (object instanceof EmptyEvalutible) {
            return ((EmptyEvalutible) object).isEmpty();
        }
        if (object instanceof Holder) {
            return ((Holder) object).isEmpty();
        }
        if (object instanceof Reference) {
            return ((Reference) object).isNull();
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

    public static <T> int getLength(T object) {
        if (isNull(object)) {
            return 0;
        }
        if (object instanceof String) {
            return ((String) object).length();
        }

        if (object instanceof CharSequence) {
            CharSequence cs = (CharSequence) object;
            return cs.length();
        }

        if (object instanceof Number) {
            return 1;
        }

        if (object instanceof Buffer) {
            Buffer buff = (Buffer) object;
            return buff.remaining();
        }

        if (object instanceof Collection) {
            return ((Collection) object).size();
        }

        if (object instanceof Map) {
            return ((Map) object).size();
        }
        if (object.getClass().isArray()) {
            return Arrs.getLength(object);
        }

        if (object.getClass().isEnum()) {
            return 1;
        }
        if (object instanceof Holder) {
            Holder holder = (Holder) object;
            return getLength(holder.get());
        }
        return Pipeline.of(object).asList().size();
    }

}
