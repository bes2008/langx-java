package com.jn.langx.navigation;

import java.util.List;

/**
 * Navigator接口定义了一组方法，用于在给定的上下文中，基于路径表达式对数据进行操作。
 * 它提供了获取、设置和查询数据类型的功能，以及解析路径表达式的工具方法。
 *
 * @param <Context> 一个标记类型参数，表示导航器操作所依赖的上下文类型。
 * @since 4.6.10
 */
public interface Navigator<Context> {
    /**
     * 根据路径表达式从指定上下文中获取一个对象。
     *
     * @param <E> 期望获取的对象类型。
     * @param context 导航器操作所依赖的上下文。
     * @param pathExpression 描述如何到达目标对象的路径表达式。
     * @return 返回根据路径表达式获取到的对象，类型为E。
     */
    <E> E get(Context context, String pathExpression);

    /**
     * 根据路径表达式从指定上下文中获取一个对象列表。
     *
     * @param <E> 列表中期望的对象类型。
     * @param context 导航器操作所依赖的上下文。
     * @param pathExpression 描述如何到达目标对象列表的路径表达式。
     * @return 返回一个包含根据路径表达式获取到的对象的列表，类型为E。
     */
    <E> List<E> getList(Context context, String pathExpression);

    /**
     * 在指定上下文中，根据路径表达式设置一个对象的值。
     *
     * @param <E> 要设置的值的类型。
     * @param context 导航器操作所依赖的上下文。
     * @param pathExpression 描述如何到达目标对象的路径表达式。
     * @param value 要设置的新值，类型为E。
     */
    <E> void set(Context context, String pathExpression, E value);

    /**
     * 根据路径表达式获取指定上下文中对象的类型。
     *
     * @param <E> 期望获取的类型。
     * @param context 导航器操作所依赖的上下文。
     * @param pathExpression 描述如何到达目标对象的路径表达式。
     * @return 返回根据路径表达式获取到的对象的类型，类型为E。
     */
    <E> Class<E> getType(Context context, String pathExpression);

    /**
     * 获取路径表达式的父路径。
     *
     * @param pathExpression 原始的路径表达式。
     * @return 返回路径表达式的父路径部分。
     */
    String getParentPath(String pathExpression);

    /**
     * 获取路径表达式的叶节点部分。
     *
     * @param pathExpression 原始的路径表达式。
     * @return 返回路径表达式的叶节点部分。
     */
    String getLeaf(String pathExpression);
}
