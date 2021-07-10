package com.jn.langx.util.pattern.patternset;

import com.jn.langx.Nameable;

public class StringPatternEntry implements Nameable {
    private String name;

    public StringPatternEntry(){

    }

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


    public static class Factory implements com.jn.langx.factory.Factory<String, StringPatternEntry> {
        @Override
        public StringPatternEntry get(String pattern) {
            return new StringPatternEntry(pattern);
        }
    }
}
