package com.jn.langx.util.collect;

/**
 * Primitive array wrapper
 */
public class PrimitiveArrays {
    /**
     * convert a boolean[] to a Boolean[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Boolean[] wrap(boolean[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Boolean[0];
        }
        Boolean[] ret = new Boolean[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Boolean[] to a boolean[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static boolean[] unwrap(Boolean[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new boolean[0];
        }
        boolean[] ret = new boolean[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a char[] to a Character[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Character[] wrap(char[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Character[0];
        }
        Character[] ret = new Character[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Character[] to a char[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static char[] unwrap(Character[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new char[0];
        }
        char[] ret = new char[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a byte[] to a Byte[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Byte[] wrap(byte[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Byte[0];
        }
        Byte[] ret = new Byte[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Byte[] to a byte[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static byte[] unwrap(Byte[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new byte[0];
        }
        byte[] ret = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a short[] to a Short[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static Short[] wrap(short[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new Short[0];
        }
        Short[] ret = new Short[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a Short[] to a short[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
    public static short[] unwrap(Short[] values, boolean resultNullable) {
        if (values == null) {
            return resultNullable ? null : new short[0];
        }
        short[] ret = new short[values.length];
        for (int i = 0; i < values.length; i++) {
            ret[i] = values[i];
        }
        return ret;
    }

    /**
     * convert a int[] to a Integer[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
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

    /**
     * convert a Integer[] to a int[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
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

    /**
     * convert a float[] to a Float[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
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

    /**
     * convert a Float[] to a float[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
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

    /**
     * convert a long[] to a Long[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
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

    /**
     * convert a Long[] to a long[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
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

    /**
     * convert a double[] to a Double[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
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

    /**
     * convert a Double[] to a double[]
     * @param values source
     * @param resultNullable whether return null when source is null or not
     * @return result
     */
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
