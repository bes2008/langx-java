package com.jn.langx.util.comparator;

import com.jn.langx.Ordered;
import com.jn.langx.annotation.Order;
import com.jn.langx.util.function.Supplier;
import com.jn.langx.util.reflect.Reflects;

public class Orders {
    public static int getOrder(Object o) {
        return getOrder(o, Ordered.LOWEST_PRECEDENCE);
    }
    public static int getOrder(Object o, final int defaultValue) {
        return getOrder(o, new Supplier<Object, Integer>() {
            @Override
            public Integer get(Object input) {
                return defaultValue;
            }
        });
    }
    public static int getOrder(Object o, Supplier<Object,Integer> defaultOrderSupplier) {
        if (o != null) {
            if (o instanceof Integer) {
                return (Integer) o;
            }
            if (o instanceof Ordered) {
                return ((Ordered) o).getOrder();
            }
            Order order = Reflects.getAnnotation(o.getClass(), Order.class);
            if (order != null) {
                return order.value();
            }
        }
        return defaultOrderSupplier.get(o);
    }
}
