package com.jn.langx.util;

import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.struct.Reference;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.Collection;
import java.util.Map;

public class Emptys {
    private Emptys() {

    }

    public static final int[] EMPTY_INTS = {};
    public static final byte[] EMPTY_BYTES = {};
    public static final char[] EMPTY_CHARS = {};
    public static final Object[] EMPTY_OBJECTS = {};
    public static final Class<?>[] EMPTY_CLASSES = {};
    public static final String[] EMPTY_STRINGS = {};
    public static final String EMPTY_STRING = "";

    public static boolean isAnyEmpty(Object... args) {
        return Collects.anyMatch(Functions.emptyPredicate(), args);
    }

    public static boolean isNoneEmpty(Object... args) {
        return Collects.noneMatch(Functions.emptyPredicate(), args);
    }


    public static boolean isAllEmpty(Object... args) {
        return Collects.allMatch(Functions.emptyPredicate(), args);
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }

        if (object instanceof Collection) {
            return ((Collection) object).isEmpty();
        }

        if (object instanceof Map) {
            return ((Map) object).isEmpty();
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

        if (object.getClass().isEnum()) {
            return false;
        }
        if (object.getClass().isArray()) {
            return Array.getLength(object) <= 0;
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

        if (object instanceof com.jn.langx.util.collection.buffer.Buffer) {
            return ((com.jn.langx.util.collection.buffer.Buffer) object).hasRemaining();
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
