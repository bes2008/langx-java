package com.jn.langx.algorithm.loadbalance;

public interface InvocationKeyGetter {
    String get(Node node, Object any);
}
