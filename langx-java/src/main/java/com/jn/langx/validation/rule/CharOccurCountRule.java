package com.jn.langx.validation.rule;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.ranges.IntRange;
import com.jn.langx.util.struct.CharData;

/**
 * CharOccurCountRule类用于检查字符串中特定字符集的出现次数是否在指定范围内
 * 它实现了Rule接口，用于对字符串进行有效性校验
 */
public class CharOccurCountRule extends AbstractRule {
    // validChars存储了有效的字符集
    private CharData validChars;
    // countRange定义了有效字符出现次数的范围
    private IntRange countRange;

    /**
     * 构造函数，用于初始化CharOccurCountRule对象
     * 它允许通过指定有效的字符集和最小出现次数来创建规则
     *
     * @param validChars 有效的字符集
     * @param min 字符出现的最小次数
     */
    public CharOccurCountRule(CharData validChars, int min){
        this(validChars,min,Integer.MAX_VALUE);
    }

    /**
     * 构造函数，用于初始化CharOccurCountRule对象
     * 它允许通过指定有效的字符集、最小和最大出现次数来创建规则
     *
     * @param validChars 有效的字符集
     * @param min 字符出现的最小次数
     * @param max 字符出现的最大次数
     */
    public CharOccurCountRule(CharData validChars, int min, int max){
        super(null);
        this.validChars = validChars;
        this.countRange = new IntRange(min,max);
    }

    /**
     * 构造函数，用于初始化CharOccurCountRule对象
     * 它允许通过指定有效的字符集字符串、最小和最大出现次数来创建规则
     *
     * @param validChars 有效的字符集字符串
     * @param min 字符出现的最小次数
     * @param max 字符出现的最大次数
     */
    public CharOccurCountRule(String validChars, int min, int max){
        this(new CharData(validChars),min,max);
    }

    /**
     * 实现Rule接口的test方法，用于检查输入字符串是否符合规则
     * 它会统计字符串中有效字符的出现次数，并判断这个次数是否在指定范围内
     *
     * @param value 待检查的字符串
     * @return 如果字符串符合规则，则返回true；否则返回false
     */
    @Override
    public ValidationResult doTest(String value) {
        // 初始化计数器，用于统计有效字符的出现次数
        int count = 0;
        // 遍历字符串中的每个字符
        for(char c: value.toCharArray()){
            // 如果字符是有效字符集的一部分，则增加计数器
            if(validChars.getChars().contains(""+c)){
                count++;
            }
        }
        // 判断有效字符的出现次数是否在指定范围内
        if( countRange.contains(count)){
            return ValidationResult.ofValid();
        }
        return ValidationResult.ofInvalid(StringTemplates.formatWithPlaceholder("the count of valid chars in '{}' is out of range: {}",value,countRange.getRangeString()));
    }
}
