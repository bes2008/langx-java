package com.jn.langx.algorithm.loadbalance;

public interface InvocationKeyGetter<NODE extends Node, INVOCATION> {
    String get(NODE node, INVOCATION invocation);
}
