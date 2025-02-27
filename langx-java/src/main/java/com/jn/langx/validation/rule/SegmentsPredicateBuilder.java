package com.jn.langx.validation.rule;

import com.jn.langx.Builder;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.regexp.Regexps;
import com.jn.langx.util.struct.CharData;
import com.jn.langx.validation.TextValidatorBuilder;

import java.util.List;

public class SegmentsPredicateBuilder implements Builder<SegmentsPredicate> {

    private List<SegmentMetadata> segments = Lists.newArrayList();
    private StringBuilder patternBuilder = new StringBuilder();
    public SegmentsPredicateBuilder(){
    }

    public SegmentsPredicateBuilder addSegment(String prefix, String name, boolean required, String regexp, Rule... rules){
        SegmentMetadata segmentMetadata = new SegmentMetadata(required, regexp, rules);
        Preconditions.checkNotEmpty(name);
        segmentMetadata.setName(name);
        return this.addSegment(prefix, segmentMetadata);
    }

    public SegmentsPredicateBuilder addSegment(String prefix, SegmentMetadata segmentMetadata){
        segments.add(segmentMetadata);

        String name = segmentMetadata.getName();
        // 只能是大小写字母或者数字
        if(!new CharRule(CharData.ALPHABET_DIGITS).test(name).isValid()) {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("name must be only any letter in [0-9a-zA-Z], your name is: {}", name));
        }

        if(Strings.isNotEmpty(prefix) && !segmentMetadata.isRequired()){
            patternBuilder.append("(");
            patternBuilder.append(prefix);
        }
        patternBuilder.append("(?<");
        patternBuilder.append(segmentMetadata.getName());
        patternBuilder.append(">");
        patternBuilder.append(segmentMetadata.getRegexp());
        patternBuilder.append(")");
        if(!segmentMetadata.isRequired()){
            if(Strings.isNotEmpty(prefix)){
                patternBuilder.append(")");
            }
            patternBuilder.append("?");
        }
        return this;
    }

    public SegmentsPredicateBuilder addString(String fixedString){
        patternBuilder.append(fixedString);
        return this;
    }

    @Override
    public SegmentsPredicate build() {
        return new SegmentsPredicate(Regexps.createRegexp(patternBuilder.toString()), segments);
    }
}
