package com.jn.langx.validation.rule;


import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

import java.util.List;
import java.util.Map;


public class SegmentsPredicate implements Predicate<String> {
    private final List<SegmentMetadata> segmentMetadatas;
    private Regexp regexp;

    public SegmentsPredicate(Regexp regexp, List<SegmentMetadata> segmentMetadatas) {
        this.regexp = regexp;
        this.segmentMetadatas = segmentMetadatas;
    }

    @Override
    public boolean test(String text) {
        if(Regexps.match(regexp, text)){
            Map<String,String> groups = Regexps.findNamedGroup(regexp, text);
            for (SegmentMetadata segmentMetadata : segmentMetadatas) {
                String segmentValue = groups.get(segmentMetadata.getName());
                if(Strings.isEmpty(segmentValue)){
                    if(segmentMetadata.isRequired()){
                        return false;
                    }
                }else{
                    for (Rule rule : segmentMetadata.getRules()) {
                        if(!rule.test(segmentValue).isValid()){
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
