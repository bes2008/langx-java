package com.jn.langx.algorithm.loadbalance;

import com.jn.langx.util.function.Predicate;

import java.util.List;

public interface LoadBalancer<NODE extends Node, INVOCATION> {
    void addNode(NODE node);

    void removeNode(NODE node);

    boolean hasNode(String nodeId);

    NODE getNode(String nodeId);

    void markDown(String nodeId);

    List<NODE> getNodes();

    List<NODE> getNodes(Predicate<NODE> predicate);

    NODE chooseNode(INVOCATION invocation);

    boolean isEmpty();
}
