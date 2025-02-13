package com.jn.langx.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.Collection;

/**
 * MemberAttributes接口定义了一系列用于获取成员属性信息的方法
 * 它提供了一种方式来访问成员的名称、注解、修饰符等信息
 * @param <M> 表示成员类型，必须是Member的子类型
 */
public interface MemberAttributes<M extends Member> {

    /**
     * 获取成员的名称
     * @return 成员的名称
     */
    String getName();

    /**
     * 获取指定类型的注解
     * @param <T> 注解类型，必须是Annotation的子类型
     * @param annotation 注解类型的Class对象，用于指定想要获取的注解类型
     * @return 如果成员上有该类型的注解，则返回注解对象；否则返回null
     */
    <T extends Annotation> T getAnnotation(Class<T> annotation);

    /**
     * 获取成员上所有的注解
     * @return 包含所有注解的Collection集合
     */
    Collection<Annotation> getAnnotations();

    /**
     * 检查成员是否具有指定的修饰符
     * @param modifier 需要检查的修饰符的整数值表示
     * @return 如果成员具有该修饰符，则返回true；否则返回false
     */
    boolean hasModifier(int modifier);

    /**
     * 获取声明该成员的类
     * @return 声明该成员的类的Class对象
     */
    Class getDeclaringClass();

    /**
     * 获取成员的修饰符
     * @return 成员的修饰符的整数值表示
     */
    int getModifier();

    /**
     * 获取成员实例
     * @return 成员实例，具体类型由M指定
     */
    M get();
}
