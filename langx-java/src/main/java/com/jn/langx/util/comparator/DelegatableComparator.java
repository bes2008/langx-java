package com.jn.langx.util.comparator;

import com.jn.langx.Delegatable;

import java.util.Comparator;

/**
 * DelegatableComparator接口扩展了Comparator接口和Delegatable<Comparator<V>>接口，
 * 定义了一个可以委托比较逻辑的比较器。这种设计允许比较器的比较行为被动态地委托给另一个比较器，
 * 从而提高比较逻辑的灵活性和可重用性。
 *
 * @param <V> 比较器处理的对象类型。
 */
public interface DelegatableComparator<V> extends Comparator<V>, Delegatable<Comparator<V>> {

    /**
     * 获取当前比较器委托的比较器。
     *
     * @return 当前比较器委托的Comparator实例。
     */
    @Override
    Comparator<V> getDelegate();

    /**
     * 设置当前比较器委托的比较器。
     *
     * @param delegate 要委托的Comparator实例。
     */
    @Override
    void setDelegate(final Comparator<V> delegate);

    /**
     * 比较两个指定对象的顺序。这个比较器通过其委托的比较器来执行比较逻辑。
     *
     * @param o1 要比较的第一个对象。
     * @param o2 要比较的第二个对象。
     * @return 如果第一个对象小于、等于或大于第二个对象，则分别返回负数、零或正数。
     * @see Comparator#compare(Object, Object)
     */
    @Override
    int compare(V o1, V o2);
}
