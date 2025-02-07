package com.jn.langx.util.struct;

/**
 * Ref接口定义了一个泛型引用类型，允许存储和获取不同类型的数据。
 * 它扩展了Reference接口，继承了Reference的功能，并添加了设置引用值的能力。
 * 该接口主要用于需要一个可以动态指向不同对象的引用的场景。
 *
 * @param <T> 表示引用对象的类型，可以是任何数据类型。
 */
public interface Ref<T> extends Reference<T> {
    /**
     * 设置引用指向的对象。
     * 这允许Ref对象在生命周期内改变其指向的对象。
     *
     * @param t 要设置为引用目标的对象，可以为空，表示清除当前引用。
     */
    void set(T t);

    /**
     * 判断当前引用指向的对象是否为空。
     * 这个方法与isNull不同，它用于检查引用对象内部是否为空，而不是引用本身是否为空。
     * 例如，如果引用对象是一个集合，那么这个方法将检查集合是否为空。
     *
     * @return 如果引用指向的对象为空，则返回true；否则返回false。
     */
    boolean isEmpty();
}
