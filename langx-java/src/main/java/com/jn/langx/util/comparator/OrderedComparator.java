package com.jn.langx.util.comparator;

import java.util.Comparator;

/**
 * 按照优先级 从高 到 底 排序
 *
 * @param <T>
 */
public class OrderedComparator<T> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        if(o1==o2){
            return 0;
        }
        int order1 = Orders.getOrder(o1);
        int order2 = Orders.getOrder(o2);
        // 优先级 高 -> 低，对应的值是：从小到大， 也就是需要的是升序排序
        return order1 - order2;
    }
}
