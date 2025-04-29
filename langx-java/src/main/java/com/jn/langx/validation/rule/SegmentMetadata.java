package com.jn.langx.validation.rule;

import com.jn.langx.AbstractNameable;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Lists;

import java.util.List;

public class SegmentMetadata extends AbstractNameable {
    /**
     * 用于提取、验证
     */
    private boolean required;
    /**
     * 用于提取
     */
    private String regexp;

    /**
     * 用于验证
     */
    private List<Rule> rules;
    public SegmentMetadata(String regexp, Rule... rules){
        this(true, regexp, rules);
    }
    public SegmentMetadata(boolean required, String regexp, Rule... rules){
        Preconditions.checkNotEmpty(regexp);
        this.required = required;
        this.regexp = regexp;
        this.rules = Lists.newArrayList(rules);
    }


    public boolean isRequired() {
        return required;
    }

    public String getRegexp() {
        return regexp;
    }

    public List<Rule> getRules() {
        return rules;
    }
}
