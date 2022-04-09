package com.jn.langx.text.grok;

import com.jn.langx.util.regexp.RegexpMatcher;

import java.util.Map;

public class DefaultGrokTemplate implements GrokTemplate{
    private TemplatizedPattern pattern;

    @Override
    public Map<String, Object> extract(String text) {
        RegexpMatcher matcher = pattern.getRegexp().matcher(text);
        if(!matcher.matches()) {
            return null;
        }
        //matcher.group();
        return null;
    }


    @Override
    public String format(Map<String, Object> args) {
        return null;
    }
}
