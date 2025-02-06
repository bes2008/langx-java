package com.jn.langx.configuration;

import com.jn.langx.Parser;

/**
 * ConfigurationParser接口继承自Parser接口，用于解析配置信息
 * 它定义了一个泛型方法parse，用于将输入的I类型数据解析成T类型的Configuration对象
 * 此接口的存在是为了提供一个专门用于配置解析的契约，使得配置解析过程更加清晰和标准化
 *
 * @param <I> 输入数据的类型，可以是任何支持解析的输入类型
 * @param <T> 解析输出的配置类型，必须是Configuration的子类
 */
public interface ConfigurationParser<I, T extends Configuration> extends Parser<I, T> {
    /**
     * 解析输入的I类型数据，返回解析后的T类型配置对象
     * 该方法覆盖了父接口Parser中的parse方法，确保了其返回类型与父接口保持一致
     *
     * @param input 输入数据，其类型为I，是解析的源头
     * @return 解析后的配置对象，其类型为T，是Configuration的子类
     */
    @Override
    T parse(I input);
}
