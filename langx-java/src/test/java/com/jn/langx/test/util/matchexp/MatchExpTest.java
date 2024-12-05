package com.jn.langx.test.util.matchexp;

import com.jn.langx.util.matchexp.MatchExp;
import com.jn.langx.util.regexp.Regexps;
import org.junit.Test;

public class MatchExpTest {
    @Test
    public void testType(){
        String str = "hello";

        new MatchExp().addTypePattern(Integer.class, new Runnable() {
            @Override
            public void run() {
                System.out.println("type is Integer");
            }
        }).addRegexpPattern("\\d+hello\\d+", new Runnable() {
            @Override
            public void run() {
                System.out.println("match the regex");
            }
        }).addTypePattern(String.class, new Runnable() {
            @Override
            public void run() {
                System.out.println("type is String");
            }
        }).match(str);

    }
}
