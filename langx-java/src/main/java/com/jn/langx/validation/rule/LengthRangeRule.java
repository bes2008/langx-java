package com.jn.langx.validation.rule;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.ranges.IntRange;

/**
 * 长度规则类，用于检查输入字符串是否符合指定长度范围
 * 该规则用于验证字符串的长度是否在给定的最小值和最大值范围内
 */
public class LengthRangeRule extends AbstractRule {
    // 定义一个整数范围对象，用于存储长度的最小值和最大值
    private IntRange range;

    /**
     * 构造函数，用于创建指定最小长度的长度规则对象
     * 最大长度默认为Integer.MAX_VALUE，即不做上限限制
     *
     * @param min 最小长度
     */
    public LengthRangeRule(int min) {
        this(min, Integer.MAX_VALUE);
    }

    /**
     * 构造函数，用于创建指定最小长度和最大长度的长度规则对象
     *
     * @param min 最小长度
     * @param max 最大长度
     */
    public LengthRangeRule(int min, int max) {
        super(null);
        // 初始化长度范围
        this.range = new IntRange(min, max);
    }

    /**
     * 检测给定字符串是否符合长度规则
     *
     * @param value 待检测的字符串
     * @return 如果字符串长度在指定范围内，则返回true，否则返回false
     */
    @Override
    public ValidationResult doTest(String value) {
        // 检测字符串长度是否在范围内
        boolean isValid = range.contains(value.length());
        if(isValid){
            return ValidationResult.ofValid();
        }else{
            return ValidationResult.ofInvalid(StringTemplates.formatWithPlaceholder("the length of '{}' is out of range: {}",value,range.getRangeString()));
        }
    }
}
