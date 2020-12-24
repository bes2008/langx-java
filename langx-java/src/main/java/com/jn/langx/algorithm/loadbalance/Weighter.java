package com.jn.langx.algorithm.loadbalance;

public interface Weighter {
    int getWeight(Node node, Object any);
}
