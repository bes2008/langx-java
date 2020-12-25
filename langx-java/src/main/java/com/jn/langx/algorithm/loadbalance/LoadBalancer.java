package com.jn.langx.algorithm.loadbalance;

import com.jn.langx.util.function.Predicate;

import java.util.List;

public interface LoadBalancer<NODE extends Node, INVOCATION> {
    void addNode(NODE node);

    void removeNode(NODE node);

    boolean hasNode(NODE node);

    NODE getNode(NODE node);

    void markDown(NODE node);

    List<NODE> getNodes();

    List<NODE> getNodes(Predicate<NODE> predicate);

    NODE chooseNode(INVOCATION invocation);

    boolean isEmpty();
}
