package com.jn.langx.plugin;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.registry.Registry;
import com.jn.langx.util.function.Predicate;

import java.util.List;

/**
 * PluginRegistry接口扩展了Registry接口，专门用于插件的注册和查询
 * 它提供了获取插件列表和根据条件查找特定插件的方法
 */
public interface PluginRegistry extends Registry<String, Plugin> {

    /**
     * 获取当前注册的所有插件的列表
     *
     * @return 插件列表
     */
    List<Plugin> plugins();

    /**
     * 根据插件接口类型查找所有实现该接口的插件
     *
     * @param itfc 插件接口类型
     * @param <P>  插件类型参数
     * @return 实现指定接口的插件列表
     */
    <P extends Plugin> List<P> find(@NonNull Class<P> itfc);

    /**
     * 使用Predicate条件查找所有符合条件的插件
     *
     * @param predicate 查找条件
     * @param <P>       插件类型参数
     * @return 符合条件的插件列表
     */
    <P extends Plugin> List<P> find(@NonNull Predicate<P> predicate);

    /**
     * 结合插件接口类型和Predicate条件查找所有符合条件的插件
     *
     * @param itfc      插件接口类型
     * @param predicate 查找条件
     * @param <P>       插件类型参数
     * @return 实现指定接口且符合条件的插件列表
     */
    <P extends Plugin> List<P> find(@NonNull Class<P> itfc, @NonNull Predicate<P> predicate);

    /**
     * 根据插件接口类型和Predicate条件查找一个符合条件的插件
     *
     * @param itfc      插件接口类型
     * @param predicate 查找条件
     * @param <P>       插件类型参数
     * @return 实现指定接口且符合条件的一个插件，如果不存在则返回null
     */
    <P extends Plugin> P findOne(@NonNull Class<P> itfc, @NonNull Predicate<P> predicate);
}
