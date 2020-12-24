package com.jn.langx.algorithm.loadbalance;

import com.jn.langx.Named;
import com.jn.langx.annotation.Nullable;

import java.util.List;

public interface LoadBalanceStrategy<NODE extends Node, INVOCATION> extends Named {
    void addNode(NODE node);

    void removeNode(NODE node);

    NODE select(List<NODE> reachableNodes, @Nullable INVOCATION invocation);
}
