package com.jn.langx.text.transform.caseconversion;

/**
 * 将文本转换为KebabCase（短横线连接）的转换器类
 * 继承自TextToHyphenCaseTransformer，用于将输入的文本转换为KebabCase格式
 * KebabCase格式是一种单词之间用短横线连接的命名风格，例如："this-is-kebab-case"
 * 该类的构造函数调用了父类的构造函数，以确定转换的细节
 */
public class KebabCaseTransformer extends AbstractTokenCaseTransformer {
    /**
     * 构造函数
     * 调用父类的构造函数，设置是否转换为小写和是否使用下划线的标志
     * 这里设置为：true - 转换为小写
     */
    public KebabCaseTransformer() {
        super("-", LetterCase.LOWER, LetterCase.NOOP, LetterCase.NOOP);
    }
}

