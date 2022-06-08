package com.jn.langx.util;

public class Booleans {
    private Booleans() {
    }

    public static boolean truth(Boolean b) {
        return b != null && b;
    }

    public static boolean truth(String b){
        return BooleanEvaluator.simpleStringEvaluator.evalTrue(b);
    }
}
