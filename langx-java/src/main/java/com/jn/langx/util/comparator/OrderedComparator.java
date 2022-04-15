package com.jn.langx.util.comparator;


import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.function.Supplier;

import java.util.Comparator;

/**
 * 按照优先级 从高 到 底 排序
 *
 * @param <T>
 */
public class OrderedComparator<T> implements Comparator<T> {
    @Nullable
    private Supplier<T, Integer> defaultOrderSupplier;

    public OrderedComparator() {

    }

    public OrderedComparator(Supplier<T, Integer> defaultOrderSupplier) {
        this.defaultOrderSupplier = defaultOrderSupplier;
    }

    public Supplier<T, Integer> getDefaultOrderSupplier() {
        return defaultOrderSupplier;
    }

    public void setDefaultOrderSupplier(Supplier<T, Integer> defaultOrderSupplier) {
        this.defaultOrderSupplier = defaultOrderSupplier;
    }

    @Override
    public int compare(T o1, T o2) {
        if (o1 == o2) {
            return 0;
        }
        Supplier defaultOrderSupplier = this.defaultOrderSupplier;
        int order1 = Orders.getOrder(o1, defaultOrderSupplier);
        int order2 = Orders.getOrder(o2, defaultOrderSupplier);
        // 优先级 高 -> 低，对应的值是：从小到大， 也就是需要的是升序排序
        return order1 - order2;
    }
}
