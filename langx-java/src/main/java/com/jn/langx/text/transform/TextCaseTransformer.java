package com.jn.langx.text.transform;

import com.jn.langx.Transformer;

/**
 * TextCaseTransformer接口继承自Transformer接口，专门用于字符串到字符串的转换操作
 * 它定义了一个转换字符串的方法，允许实现者提供自定义的字符串转换逻辑
 */
public interface TextCaseTransformer extends Transformer<String, String> {
    /**
     * 转换输入的字符串
     *
     * @param text 待转换的原始字符串
     * @return 转换后的字符串
     */
    @Override
    String transform(String text);
}
