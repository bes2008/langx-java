package com.jn.langx.algorithm.loadbalance;

public interface Weighter<NODE extends Node, INVOCATION> {
    int getWeight(NODE node, INVOCATION invocation);
}
