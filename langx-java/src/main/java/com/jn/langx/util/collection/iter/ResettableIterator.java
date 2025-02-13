package com.jn.langx.util.collection.iter;

import java.util.Iterator;

/**
 * ResettableIterator接口继承自Iterator接口，并添加了reset方法
 * 它允许迭代器在遍历集合时，回到初始状态，从而可以重新开始迭代
 *
 * @param <E> 迭代器中元素的类型
 */
public interface ResettableIterator<E> extends Iterator<E> {
    /**
     * 重置迭代器的状态
     * 调用此方法后，迭代器应该回到它最初的起点，如果迭代器尚未初始化，此方法应创建一个新的迭代状态
     * 重置后，迭代器应该为下一次迭代做好准备，就像第一次使用一样
     */
    void reset();
}
