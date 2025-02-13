package com.jn.langx;

/**
 * 定义一个过滤器接口，用于确定某个元素是否满足特定条件
 * 这个接口的主要作用是定义一个接受或拒绝元素的标准
 *
 * @param <E> 元素的类型，这是一个泛型接口，可以用于任何类型的元素
 */
public interface Filter<E> {
    /**
     * 判断给定的元素是否符合过滤条件
     *
     * @param e 要进行判断的元素
     * @return 如果元素符合过滤条件，则返回true；否则返回false
     */
    boolean accept(E e);
}
