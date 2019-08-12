package com.jn.langx.util;

import java.util.Collection;
import java.util.Map;

public class Emptys {
    public static boolean isEmpty(String str) {
        return Strings.isEmpty(str);
    }

    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.isEmpty();
    }

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
            return number.floatValue() == 0F;
        }
        if (number instanceof Double) {
            return number.doubleValue() == 0D;
        }
        return false;
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        if (object instanceof String) {
            return isEmpty((String) object);
        }

        if (object instanceof Number) {
            return isZero((Number) object);
        }

        if (object instanceof Collection) {
            return isEmpty((Collection) object);
        }

        if (object instanceof Map) {
            return isEmpty((Map) object);
        }
        if (object.getClass().isArray()) {
            return isEmpty((Object[]) object);
        }

        if (object.getClass().isEnum()) {
            return false;
        }

        return false;
    }
}
