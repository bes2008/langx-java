package com.jn.langx.validation.rule;

import com.jn.langx.text.StringTemplates;

/**
 * 用于检查字符串是否全是有效字符
 */
public class CharRule extends AbstractRule {
    // 保存有效的字符数据
    private CharData validCharData;

    /**
     * 构造函数，使用CharData对象初始化CharRule
     * @param charData 包含有效字符的CharData对象
     */
    public CharRule(CharData charData){
        super(null);
        this.validCharData=charData;
    }

    /**
     * 构造函数，使用字符串初始化CharRule
     * 这个字符串表示一组有效的字符
     * @param chars 包含有效字符的字符串
     */
    public CharRule(String chars){
        // 通过字符串创建CharData对象
        this(new CharData(chars));
    }

    /**
     * 检查输入字符串是否只包含有效字符
     * @param value 待检查的字符串
     * @return 如果字符串只包含有效字符，则返回true；否则返回false
     */
    @Override
    public ValidationResult doTest(String value) {
        // 遍历输入字符串的每一个字符
        for (char c : value.toCharArray()){
            // 如果字符不在有效字符列表中，则返回false
            if(!validCharData.getChars().contains(String.valueOf(c))){
                return ValidationResult.ofInvalid(StringTemplates.formatWithPlaceholder("the char '{}' is invalid",c));
            }
        }
        // 所有字符都是有效的，返回true
        return ValidationResult.ofValid();
    }
}
