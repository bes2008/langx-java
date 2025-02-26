package com.jn.langx.validation.rule;


import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.struct.CharSequenceData;

/**
 * 检测字符串中是否存在连续的序列，当连续的序列超出指定的最大长度，就认为是不合法的。
 *
 * @author jinuo.fang
 */
public class CharSequenceRule implements Rule{

    private CharSequenceData charSequences;

    private int maxSequenceLength;

    public CharSequenceRule(CharSequenceData charSequences, int maxSequenceLength){
        this.charSequences = charSequences;
        Preconditions.checkArgument(maxSequenceLength>=1);
        this.maxSequenceLength = maxSequenceLength;
    }

    @Override
    public ValidationResult test(String value) {
        int maxSequenceLength = 1;
        int sequenceLength = 1;
        // 上一次匹配到的序列
        String lastMatchedSequence = null;
        // 上一次匹配到的序列中的索引
        int lastMatchedSequenceIndex = -1;

        for (int i = 0; i < value.length(); i++){
            final char c = value.charAt(i);
            String sequence = Pipeline.of(charSequences.getSequences()).findFirst( new Predicate<String>() {
                @Override
                public boolean test(String charSequence) {
                    return Strings.contains(charSequence, ""+c);
                }
            });

            if(sequence == null){
                lastMatchedSequence = null;
                lastMatchedSequenceIndex = -1;
                sequenceLength=1;
                continue;
            }
            int indexInSequence = sequence.indexOf(c);
            if(lastMatchedSequence==null || !Objs.equals(lastMatchedSequence, sequence)){
                lastMatchedSequence = sequence;
                lastMatchedSequenceIndex=indexInSequence;
                sequenceLength=1;
                continue;
            }

            // 接下来就是要处理两次的sequence 是同一个的情况
            if(indexInSequence-lastMatchedSequenceIndex==1){
                // 出现了连续的数据了
                sequenceLength++;
            }

            maxSequenceLength = Maths.max(maxSequenceLength, sequenceLength);
            if(maxSequenceLength > this.maxSequenceLength){
                return ValidationResult.ofInvalid(StringTemplates.formatWithPlaceholder("the max sequence length is {}", this.maxSequenceLength));
            }
        }

        return ValidationResult.ofValid();
    }
}
