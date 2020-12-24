package com.jn.langx.algorithm.loadbalance;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractLoadBalanceStrategy<NODE extends Node,INVOCATION> implements LoadBalanceStrategy<NODE,INVOCATION> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected final ConcurrentHashMap<String, NODE> nodeMap = new ConcurrentHashMap<String, NODE>();
    @Nullable
    private Weighter weighter;

    @Override
    public void addNode(NODE node) {
        Preconditions.checkNotNull(node, "the node is null");
        nodeMap.put(node.getId(), node);
    }

    @Override
    public void removeNode(NODE node) {
        Preconditions.checkNotNull(node, "the node is null");
    }


    public void setWeighter(Weighter weighter) {
        this.weighter = weighter;
    }

    /**
     * 获取node的权重
     */
    public int getWeight(NODE node, INVOCATION invocation) {
        if (weighter != null) {
            return weighter.getWeight(node, invocation);
        }
        return 0;
    }

    protected abstract NODE doSelect(List<NODE> reachableNodes, INVOCATION invocation);

    @Override
    public NODE select(List<NODE> reachableNodes, INVOCATION invocation) {
        // 过滤掉没有注册的 node
        reachableNodes = Pipeline.of(reachableNodes).filter(new Predicate<NODE>() {
            @Override
            public boolean test(NODE node) {
                return nodeMap.containsKey(node.getId());
            }
        }).asList();

        if (Emptys.isEmpty(reachableNodes) || nodeMap.isEmpty()) {
            logger.warn("Can't find any reachable nodes");
            return null;
        }

        if (reachableNodes.size() == 1) {
            return reachableNodes.get(0);
        }
        return doSelect(reachableNodes, invocation);
    }
}
