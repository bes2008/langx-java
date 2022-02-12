package com.jn.langx.util.comparator;

import com.jn.langx.Ordered;
import com.jn.langx.annotation.Order;
import com.jn.langx.util.reflect.Reflects;

public class Orders {
    public static int getOrder(Object o) {
        return getOrder(o, Ordered.LOWEST_PRECEDENCE);
    }

    public static int getOrder(Object o, int defaultValue) {
        if (o != null) {
            if (o instanceof Ordered) {
                return ((Ordered) o).getOrder();
            }
            Order order = Reflects.getAnnotation(o.getClass(), Order.class);
            if (order != null) {
                return order.value();
            }
        }
        return defaultValue;
    }
}
