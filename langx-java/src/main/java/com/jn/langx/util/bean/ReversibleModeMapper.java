package com.jn.langx.util.bean;

/**
 * ReversibleModeMapper接口继承自ModelMapper接口，用于定义两个类A和B之间的可逆映射关系
 * 它不仅允许将类A的对象映射到类B的对象，还支持将类B的对象反向映射回类A的对象
 * 这种可逆映射在数据同步、对象转换等场景中非常有用
 *
 * @param <A> the type of the source object to map
 * @param <B> the type of the destination object to map
 */
public interface ReversibleModeMapper<A, B> extends ModelMapper<A, B> {
    /**
     * 将源对象A映射为目标对象B
     * 此方法继承自ModelMapper接口，用于执行从A到B的映射操作
     *
     * @param a 源对象，类型为A
     * @return 映射后的目标对象，类型为B
     */
    @Override
    B map(A a);

    /**
     * 将目标对象B反向映射为源对象A
     * 这是ReversibleModeMapper接口特有的方法，用于执行从B到A的反向映射操作
     *
     * @param b 目标对象，类型为B
     * @return 反向映射后的源对象，类型为A
     */
    A reverseMap(B b);
}
