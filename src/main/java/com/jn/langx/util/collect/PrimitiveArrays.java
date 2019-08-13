package com.jn.langx.util.collect;

/**
 * Primitive array wrapper
 */
public class PrimitiveArrays {
    public static Boolean[] wrap(boolean[] chars, boolean resultNullable) {
        if (chars == null) {
            return resultNullable ? null : new Boolean[0];
        }
        Boolean[] ret = new Boolean[chars.length];
        for (int i = 0; i < chars.length; i++) {
            ret[i] = chars[i];
        }
        return ret;
    }

    public static boolean[] unwrap(Boolean[] chars, boolean resultNullable) {
        if (chars == null) {
            return resultNullable ? null : new boolean[0];
        }
        boolean[] ret = new boolean[chars.length];
        for (int i = 0; i < chars.length; i++) {
            ret[i] = chars[i];
        }
        return ret;
    }

    public static Character[] wrap(char[] chars, boolean resultNullable) {
        if (chars == null) {
            return resultNullable ? null : new Character[0];
        }
        Character[] ret = new Character[chars.length];
        for (int i = 0; i < chars.length; i++) {
            ret[i] = chars[i];
        }
        return ret;
    }

    public static char[] unwrap(Character[] chars, boolean resultNullable) {
        if (chars == null) {
            return resultNullable ? null : new char[0];
        }
        char[] ret = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            ret[i] = chars[i];
        }
        return ret;
    }

    public static Byte[] wrap(byte[] bytes, boolean resultNullable) {
        if (bytes == null) {
            return resultNullable ? null : new Byte[0];
        }
        Byte[] ret = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = bytes[i];
        }
        return ret;
    }

    public static byte[] unwrap(Byte[] bytes, boolean resultNullable) {
        if (bytes == null) {
            return resultNullable ? null : new byte[0];
        }
        byte[] ret = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = bytes[i];
        }
        return ret;
    }

    public static Short[] wrap(short[] bytes, boolean resultNullable) {
        if (bytes == null) {
            return resultNullable ? null : new Short[0];
        }
        Short[] ret = new Short[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = bytes[i];
        }
        return ret;
    }

    public static short[] unwrap(Short[] bytes, boolean resultNullable) {
        if (bytes == null) {
            return resultNullable ? null : new short[0];
        }
        short[] ret = new short[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            ret[i] = bytes[i];
        }
        return ret;
    }

    public static Integer[] wrap(int[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Integer[0];
        }
        Integer[] ret = new Integer[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static int[] unwrap(Integer[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new int[0];
        }
        int[] ret = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static Float[] wrap(float[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Float[0];
        }
        Float[] ret = new Float[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static float[] unwrap(Float[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new float[0];
        }
        float[] ret = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static Long[] wrap(long[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Long[0];
        }
        Long[] ret = new Long[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static long[] unwrap(Long[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new long[0];
        }
        long[] ret = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static Double[] wrap(double[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Double[0];
        }
        Double[] ret = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    public static double[] unwrap(Double[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new double[0];
        }
        double[] ret = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }
}
