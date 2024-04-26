package com.jn.langx.test.text.lexer.json;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.Regexps;

class JsonSymbol {
    @NonNull
    String name;
    @Nullable
    String value;

    int tokenType;

    // 用于判定 一个token 是否是这个symbol
    Predicate<String> predicate;



    JsonSymbol(String name, final String value, int tokenType){
        this(name, value, tokenType, (Predicate<String>) null );
    }

    JsonSymbol(String name, final String value, int tokenType, final Regexp regexp){
        this(name, value, tokenType, new Predicate<String>() {
            @Override
            public boolean test(String token) {
                return Regexps.match(regexp, token);
            }
        });
    }

    JsonSymbol(String name, final String value, int tokenType, Predicate<String> predicate){
        this.name = name;
        this.value=value;
        this.tokenType=tokenType;
        this.predicate= predicate==null?new Predicate<String>() {
            @Override
            public boolean test(String token) {
                if(value != null){
                    return Objs.equals(token, value);
                }
                return true;
            }
        }:predicate;
    }

}
