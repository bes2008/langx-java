package com.jn.langx.text.transform.caseconversion;


/**
 * 将文本转换为连字符案例的转换器类
 * 该类继承自AbstractTextCaseTransformer，用于将输入的文本转换为连字符分隔的格式
 * 连字符案例是指单词之间使用连字符进行连接的一种命名风格
 */
public class HyphenCaseTransformer extends AbstractTokenCaseTransformer {

    /**
     * 构造函数
     * 初始化父类AbstractTextCaseTransformer的属性
     * 设置分隔符为连字符("-")，并设置文本转换的标志为默认值（均为false）
     */
    public HyphenCaseTransformer(){
        super("-", LetterCase.NOOP, LetterCase.NOOP, LetterCase.NOOP);
    }

}
