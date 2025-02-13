package com.jn.langx.util.function.supplier;

import com.jn.langx.util.function.Supplier;

/**
 * PrefixSupplier 接口扩展了 Supplier 接口，用于定义一个可以提供前缀字符串的功能。
 * 它的主要作用是根据提供的补充信息（supplement）生成一个带有特定前缀的字符串。
 *
 * @since 4.4.7 表示该接口从版本 4.4.7 开始引入。
 */
public interface PrefixSupplier extends Supplier<Object,String> {
    /**
     * 获取根据补充信息生成的带有特定前缀的字符串。
     *
     * @param supplement 提供给实现类的补充信息，用于生成特定前缀的字符串。
     *                   这个参数的具体类型和用途由实现类决定。
     * @return 返回一个根据补充信息生成的带有特定前缀的字符串。
     */
    @Override
    String get(Object supplement);
}
