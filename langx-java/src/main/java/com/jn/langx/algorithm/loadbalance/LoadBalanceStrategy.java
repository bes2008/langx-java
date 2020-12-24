package com.jn.langx.algorithm.loadbalance;

import com.jn.langx.annotation.Nullable;

import java.util.List;

public interface LoadBalanceStrategy {
    void addNode(Node node);

    void removeNode(Node node);

    Node select(List<Node> reachableNodes, @Nullable Object any);
}
