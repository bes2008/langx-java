package com.jn.langx;

/**
 * Ordered接口用于定义对象的排序优先级。
 * 它通过提供两个极端优先级常量和一个获取优先级值的方法来实现
 */
public interface Ordered {
    // 最高优先级常量，表示最高的排序优先级
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    // 最低优先级常量，表示最低的排序优先级
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * 获取当前对象的排序优先级
     *
     * @return 当前对象的排序优先级值
     */
    int getOrder();
}
