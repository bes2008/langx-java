package com.jn.langx.util;

import com.jn.langx.util.Emptys;

import java.lang.reflect.Array;

/**
 * Array tools
 */
public class Arrs {

    /**
     * get the length if argument is an array, else -1
     *
     * @param object any object
     * @return the length if argument is an array, else -1
     */
    public static int getLengthIfIsArray(Object object) {
        if (isArray(object)) {
            return Array.getLength(object);
        }
        return -1;
    }

    /**
     * judge whether an object is an Array
     */
    public static boolean isArray(Object o) {
        return Emptys.isNull(o) ? false : o.getClass().isArray();
    }

    /**
     * wrap a string using new String[]{string}
     */
    public static String[] wrapAsArray(String string) {
        if (Emptys.isNull(string)) {
            return new String[0];
        }
        return new String[]{string};
    }

    /**
     * wrap a number using new String[]{number}
     */
    public static Number[] wrapAsArray(Number number) {
        if (Emptys.isNull(number)) {
            return new Number[0];
        }
        return new Number[]{number};
    }

    /**
     * wrap a boolean using new String[]{bool}
     */
    public static Boolean[] wrapAsArray(Boolean bool) {
        if (Emptys.isNull(bool)) {
            return new Boolean[0];
        }
        return new Boolean[]{bool};
    }

    /**
     * Wrap any object using new Object[]{object};
     */
    public static Object[] wrapAsArray(Object o) {
        if (Emptys.isNull(o)) {
            return new Object[0];
        }
        return new Object[]{o};
    }

}
