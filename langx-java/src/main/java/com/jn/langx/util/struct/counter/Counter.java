package com.jn.langx.util.struct.counter;

public interface Counter<E extends Number> {
    /**
     * +1
     * @return 返回增加后的值
     */
    E increment();

    /**
     * +n
     * @return 返回增加后的值
     */
    E increment(E delta);


    /**
     * -1
     * @return 返回减少后的值
     */
    E decrement();

    /**
     * -n
     * @return 返回减少后的值
     */
    E decrement(E delta);

    /**
     * +1
     * @return 返回增加之前的值
     */
    E getAndIncrement();

    /**
     * +n
     * @return 返回增加之前的值
     */
    E getAndIncrement(E delta);

    /**
     * @return 返回当前值
     */
    E get();

    /**
     * 设置值
     */
    void set(E value);

    void reset();
}
