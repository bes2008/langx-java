package com.jn.langx.text.transform.caseconversion;


/**
 * 将文本转换为Train Case格式的类
 * Train Case是一种字符串格式，其中每个单词的首字母大写，且单词之间使用短划线连接
 * 例如：convertThisTextToTrainCase 会被转换为 Convert-This-Text-To-Train-Case
 * 继承自TextToSnakeCaseTransformer类，利用其文本转换能力，但改为输出Train Case格式
 */
public class TrainCaseTransformer extends AbstractTokenCaseTransformer {
    /**
     * 构造函数
     * 初始化TextToSnakeCaseTransformer的实例，设置其参数为true，以确保转换时保留空格
     * 重写构造函数以适应Train Case转换的特定需求
     */
    public TrainCaseTransformer() {
        super("-", LetterCase.LOWER, LetterCase.UPPER, LetterCase.NOOP);
    }
}
