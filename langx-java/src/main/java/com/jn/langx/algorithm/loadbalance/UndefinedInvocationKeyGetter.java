package com.jn.langx.algorithm.loadbalance;

public class UndefinedInvocationKeyGetter implements InvocationKeyGetter {
    @Override
    public String get(Node node, Object any) {
        return "undefined";
    }
}
