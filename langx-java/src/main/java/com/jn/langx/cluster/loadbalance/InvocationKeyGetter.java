package com.jn.langx.cluster.loadbalance;

public interface InvocationKeyGetter<NODE extends Node, INVOCATION> {
    String get(NODE node, INVOCATION invocation);
}
