package com.jn.langx.validation.rule;

import com.jn.langx.Builder;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Lists;

import java.util.List;

public class SegmentsPredicateBuilder implements Builder<SegmentsPredicate> {

    private List<SegmentMetadata> segments = Lists.newArrayList();
    private StringBuilder patternBuilder = new StringBuilder();
    public SegmentsPredicateBuilder(){
    }

    public SegmentsPredicateBuilder addSegment(String name, boolean required, String regexp, Rule... rules){
        SegmentMetadata segmentMetadata = new SegmentMetadata(required, regexp, rules);
        Preconditions.checkNotEmpty(name);
        segmentMetadata.setName(name);
        return this.addSegment(segmentMetadata);
    }

    public SegmentsPredicateBuilder addSegment(SegmentMetadata segmentMetadata){
        segments.add(segmentMetadata);
        patternBuilder.append("(<");
        patternBuilder.append(segmentMetadata.getName());
        patternBuilder.append(">");
        patternBuilder.append(segmentMetadata.getRegexp());
        patternBuilder.append(")");
        if(!segmentMetadata.isRequired()){
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
        return null;
    }
}
