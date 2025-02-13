package com.jn.langx.util.collection.sequence;

import com.jn.langx.util.collection.Listable;

import java.util.*;
/**
 * Sequence接口继承了List和Listable接口，旨在表示一个有序的元素序列。
 * 它添加了特定的方法来操作序列中的第一个和最后一个元素。
 * 该接口适用于需要有序集合操作的场景，提供了丰富的操作方法来管理序列中的元素。
 *
 * @param <E> 序列中元素的类型。
 */
public interface Sequence<E> extends List<E>, Listable<E> {

    /**
     * 返回序列中的第一个元素。
     *
     * @return 序列中的第一个元素，如果序列为空，则返回null。
     */
    E first();

    /**
     * 返回序列中的最后一个元素。
     *
     * @return 序列中的最后一个元素，如果序列为空，则返回null。
     */
    E last();

    /**
     * 判断序列是否为空。
     *
     * @return 如果序列为空，则返回true；否则返回false。
     */
    @Override
    boolean isNull();

    /**
     * 返回序列中元素的数量。
     *
     * @return 序列中元素的数量。
     */
    @Override
    int size();

    /**
     * 判断序列是否为空。
     *
     * @return 如果序列为空，则返回true；否则返回false。
     */
    @Override
    boolean isEmpty();

    /**
     * 判断序列是否包含指定的元素。
     *
     * @param o 要检查的元素。
     * @return 如果序列包含指定的元素，则返回true；否则返回false。
     */
    @Override
    boolean contains(Object o);

    /**
     * 返回一个迭代器，用于遍历序列中的元素。
     *
     * @return 遍历序列元素的迭代器。
     */
    @Override
    Iterator<E> iterator();

    /**
     * 将序列中的元素转换为数组。
     *
     * @return 包含序列元素的数组。
     */
    @Override
    Object[] toArray();

    /**
     * 将序列中的元素转换为指定类型的数组。
     *
     * @param a 转换后的数组类型。
     * @return 包含序列元素的指定类型数组。
     */
    @Override
    <T> T[] toArray(T[] a);

    /**
     * 向序列中添加一个元素。
     *
     * @param e 要添加的元素。
     * @return 如果添加成功，则返回true；否则返回false。
     */
    @Override
    boolean add(E e);

    /**
     * 从序列中移除一个元素。
     *
     * @param o 要移除的元素。
     * @return 如果移除成功，则返回true；否则返回false。
     */
    @Override
    boolean remove(Object o);

    /**
     * 判断序列是否包含指定集合中的所有元素。
     *
     * @param c 指定的集合。
     * @return 如果序列包含指定集合中的所有元素，则返回true；否则返回false。
     */
    @Override
    boolean containsAll(Collection<?> c);

    /**
     * 将指定集合中的所有元素添加到序列中。
     *
     * @param c 指定的集合。
     * @return 如果添加成功，则返回true；否则返回false。
     */
    @Override
    boolean addAll(Collection<? extends E> c);

    /**
     * 在序列的指定位置前添加指定集合中的所有元素。
     *
     * @param index 指定的位置。
     * @param c 指定的集合。
     * @return 如果添加成功，则返回true；否则返回false。
     */
    @Override
    boolean addAll(int index, Collection<? extends E> c);

    /**
     * 从序列中移除指定集合中包含的所有元素。
     *
     * @param c 指定的集合。
     * @return 如果移除成功，则返回true；否则返回false。
     */
    @Override
    boolean removeAll(Collection<?> c);

    /**
     * 仅保留序列中包含在指定集合中的元素。
     *
     * @param c 指定的集合。
     * @return 如果操作成功，则返回true；否则返回false。
     */
    @Override
    boolean retainAll(Collection<?> c);

    /**
     * 清空序列中的所有元素。
     */
    @Override
    void clear();

    /**
     * 判断序列是否与指定对象相等。
     *
     * @param o 指定的对象。
     * @return 如果序列与指定对象相等，则返回true；否则返回false。
     */
    @Override
    boolean equals(Object o);

    /**
     * 返回序列的哈希码。
     *
     * @return 序列的哈希码。
     */
    @Override
    int hashCode();

    /**
     * 获取序列中指定位置的元素。
     *
     * @param index 指定的位置。
     * @return 序列中指定位置的元素。
     */
    @Override
    E get(int index);

    /**
     * 设置序列中指定位置的元素。
     *
     * @param index 指定的位置。
     * @param element 新的元素。
     * @return 原来指定位置的元素。
     */
    @Override
    E set(int index, E element);

    /**
     * 在序列的指定位置插入一个元素。
     *
     * @param index 指定的位置。
     * @param element 要插入的元素。
     */
    @Override
    void add(int index, E element);

    /**
     * 从序列中移除指定位置的元素。
     *
     * @param index 指定的位置。
     * @return 被移除的元素。
     */
    @Override
    E remove(int index);

    /**
     * 返回序列中指定元素的第一个匹配项的索引。
     *
     * @param o 指定的元素。
     * @return 指定元素的第一个匹配项的索引，如果未找到，则返回-1。
     */
    @Override
    int indexOf(Object o);

    /**
     * 返回序列中指定元素的最后一个匹配项的索引。
     *
     * @param o 指定的元素。
     * @return 指定元素的最后一个匹配项的索引，如果未找到，则返回-1。
     */
    @Override
    int lastIndexOf(Object o);

    /**
     * 返回序列的列表迭代器。
     *
     * @return 序列的列表迭代器。
     */
    @Override
    ListIterator<E> listIterator();

    /**
     * 返回序列的列表迭代器，从指定位置开始。
     *
     * @param index 指定的起始位置。
     * @return 从指定位置开始的序列的列表迭代器。
     */
    @Override
    ListIterator<E> listIterator(int index);

    /**
     * 返回序列的一个子序列，从指定的起始位置到结束位置。
     *
     * @param fromIndex 子序列的起始位置（包含）。
     * @param toIndex 子序列的结束位置（不包含）。
     * @return 序列的子序列。
     */
    <S extends Sequence<E>> S subSequence(int fromIndex, int toIndex);

    /**
     * 返回序列的一个子列表，从指定的起始位置到结束位置。
     *
     * @param fromIndex 子列表的起始位置（包含）。
     * @param toIndex 子列表的结束位置（不包含）。
     * @return 序列的子列表。
     */
    @Override
    List<E> subList(int fromIndex, int toIndex);

    /**
     * 将序列视为List接口的实现，返回List视图。
     *
     * @return 序列的List视图。
     */
    List<E> asList();

    /**
     * 返回序列的字符串表示。
     *
     * @return 序列的字符串表示。
     */
    @Override
    String toString();
}
