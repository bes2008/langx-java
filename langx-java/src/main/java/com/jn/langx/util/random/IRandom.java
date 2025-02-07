package com.jn.langx.util.random;

/**
 * IRandom接口定义了一组生成随机数的方法
 * 它提供了不同类型的随机数生成方法，包括整数、长整数、布尔值、浮点数和双精度浮点数
 * 以及将随机字节填充到给定的字节数组的方法
 */
public interface IRandom {
    /**
     * 设置随机数生成器的种子
     * 这个方法允许用户通过提供一个种子值来初始化随机数生成器
     * 相同的种子将产生相同的随机数序列
     *
     * @param seed 用于初始化随机数生成器的种子值
     */
    void setSeed(long seed);

    /**
     * 生成下一个随机整数
     * 这个方法返回一个随机的整数值
     *
     * @return 生成的随机整数
     */
    int nextInt();

    /**
     * 生成一个在指定范围内的随机整数
     * 这个方法返回一个在[0, bound)范围内的随机整数
     * 如果bound为0，则结果总是0
     *
     * @param bound 随机数的上限（不包含）
     * @return 在指定范围内的随机整数
     */
    int nextInt(int bound);

    /**
     * 生成下一个随机长整数
     * 这个方法返回一个随机的长整数值
     *
     * @return 生成的随机长整数
     */
    long nextLong();

    /**
     * 生成下一个随机布尔值
     * 这个方法返回一个随机的布尔值，true或false
     *
     * @return 生成的随机布尔值
     */
    boolean nextBoolean();

    /**
     * 生成下一个随机浮点数
     * 这个方法返回一个在[0.0f, 1.0f)范围内的随机浮点数
     *
     * @return 生成的随机浮点数
     */
    float nextFloat();

    /**
     * 生成下一个随机双精度浮点数
     * 这个方法返回一个在[0.0, 1.0)范围内的随机双精度浮点数
     *
     * @return 生成的随机双精度浮点数
     */
    double nextDouble();

    /**
     * 将随机字节填充到给定的字节数组
     * 这个方法将随机生成的字节连续地填充到给定的字节数组中
     * 直到字节数组满为止
     *
     * @param dest 要填充的字节数组
     */
    void nextBytes(byte[] dest);
}
