package com.jn.langx.util.collection;

import com.jn.langx.util.EmptyEvalutible;

import java.util.Collection;
import java.util.Iterator;

/**
 * Listable接口扩展了Iterable和EmptyEvalutible接口，为处理列表类型的集合提供了一组标准操作。
 * 它定义了如何添加、移除元素，以及评估集合是否为空或为null。
 * @param <E> 元素类型，表示此接口可以参数化，以适应不同类型的元素。
 */
public interface Listable<E> extends Iterable<E>, EmptyEvalutible {

    /**
     * 向列表中添加一个元素。
     * 如果列表不包含该元素且成功添加，则返回true。
     * @param e 要添加的元素，不能为null。
     * @return 如果元素成功添加到列表中，则返回true；否则返回false。
     */
    boolean add(E e);

    /**
     * 移除列表中第一次出现的指定元素。
     * 如果列表包含该元素且成功移除，则返回true。
     * @param e 要移除的元素，不能为null。
     * @return 如果列表中存在该元素并成功移除，则返回true；否则返回false。
     */
    boolean remove(Object e);

    /**
     * 从列表中移除所有在指定集合中包含的元素。
     * @param collection 包含要被移除元素的集合。
     * @return 如果列表因调用此方法而改变，则返回true；否则返回false。
     */
    boolean removeAll(Collection<?> collection);

    /**
     * 清空列表中的所有元素。
     * 此操作后，列表将不包含任何元素，但不会改变列表的初始容量。
     */
    void clear();

    /**
     * 将指定集合中的所有元素添加到列表中。
     * 添加的元素将位于列表的末尾。
     * @param elements 包含要添加到列表中的元素的集合。
     * @return 如果列表因调用此方法而改变，则返回true；否则返回false。
     */
    boolean addAll(Collection<? extends E> elements);

    /**
     * 返回列表的迭代器。
     * 使用此迭代器可以遍历列表中的所有元素。
     * @return 列表的迭代器。
     */
    @Override
    Iterator<E> iterator();

    /**
     * 判断列表是否为空。
     * @return 如果列表不包含任何元素，则返回true；否则返回false。
     */
    @Override
    boolean isEmpty();

    /**
     * 判断列表是否为null。
     * @return 如果列表为null，则返回true；否则返回false。
     */
    @Override
    boolean isNull();
}

