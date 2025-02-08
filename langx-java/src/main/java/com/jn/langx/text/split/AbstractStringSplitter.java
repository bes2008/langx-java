package com.jn.langx.text.split;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Lists;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;

import java.util.List;

public abstract class AbstractStringSplitter implements StringSplitter {

    private boolean trimToken= true;
    private boolean ignoreEmptyToken = true;

    public void setTrimToken(boolean trimToken) {
        this.trimToken = trimToken;
    }

    public void setIgnoreEmptyToken(boolean ignoreEmptyToken) {
        this.ignoreEmptyToken = ignoreEmptyToken;
    }

    @Override
    public final List<String> split(String str) {
        if(Objs.isEmpty(str)){
            return Lists.immutableList();
        }
        String text = beforeSplit(str);
        List<String> result = doSplit(text);
        result = afterSplit(result);
        return result;
    }

    protected String beforeSplit(String str){
        return str;
    }

    protected abstract List<String> doSplit(String str);

    protected List<String> afterSplit(List<String> tokens){
        return Pipeline.of(tokens).map(new Function<String, String>() {
            @Override
            public String apply(String input) {
                if (trimToken) {
                    return Strings.trim(input);
                } else {
                    return input;
                }
            }
        }).filter(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                if (ignoreEmptyToken) {
                    return Strings.isNotBlank(value);
                } else {
                    return true;
                }
            }
        }).asList();
    }
}
