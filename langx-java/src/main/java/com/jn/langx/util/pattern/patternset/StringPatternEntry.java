package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Named;

public class StringPatternEntry implements Named {
    private String name;

    public StringPatternEntry(String name) {
        this.name = name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
