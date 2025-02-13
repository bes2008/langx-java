package com.jn.langx.text.split;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Pipeline;

import java.util.List;

public class RegexpStringSplitter extends AbstractStringSplitter {
    private String regexp;

    public RegexpStringSplitter(String regexp) {
        this.regexp = regexp;
    }

    @Override
    protected String beforeSplit(String str) {
        Preconditions.checkNotEmpty(this.regexp,"the regexp is empty");
        return str;
    }

    @Override
    protected List<String> doSplit(String str) {
        return Pipeline.of(str.split(regexp)).asList();
    }
}
