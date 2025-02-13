package com.jn.langx.configuration;

/**
 * 配置序列化接口，用于将配置对象序列化为其他形式的对象
 * 此接口的存在是为了提供一个统一的序列化方法，使得不同的配置类型可以被转换为各种形式的输出
 * 例如，可以将内存中的配置对象序列化为JSON、XML格式的字符串，或者特定的传输对象
 *
 * @param <T> 配置类型，必须是Configuration的子类这允许接口处理各种具体的配置实现
 * @param <O> 输出类型，表示序列化后的对象类型这可以是任何类型，如String、byte[]等
 */
public interface ConfigurationSerializer<T extends Configuration, O> {
    /**
     * 序列化方法，将输入的配置对象转换为指定类型的输出对象
     *
     * @param configuration 要序列化的配置对象，它必须是Configuration的子类实例
     * @return 序列化后的对象，类型为O这可以是调用者指定的任何类型
     */
    O serialize(T configuration) ;
}

