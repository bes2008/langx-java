package com.jn.langx.text.transform.caseconversion;


/**
 * 将文本转换为驼峰命名法的转换器类
 * 继承自AbstractTextCaseTransformer，并根据驼峰命名法的规则调整文本.
 *
 */
public class CamelCaseTransformer extends AbstractTokenCaseTransformer {
    /**
     * 构造函数
     * 初始化父类AbstractTextCaseTransformer时，设置单词首字母大写、其余字母小写，以及不使用下划线连接单词
     */
    public CamelCaseTransformer(){
        super("", LetterCase.LOWER, LetterCase.UPPER, LetterCase.LOWER);
    }
}
