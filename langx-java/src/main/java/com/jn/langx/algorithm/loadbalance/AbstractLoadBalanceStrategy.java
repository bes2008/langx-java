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

public abstract class AbstractLoadBalanceStrategy implements LoadBalanceStrategy {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected final ConcurrentHashMap<String, Node> nodeMap = new ConcurrentHashMap<String, Node>();
    @Nullable
    private Weighter weighter;

    @Override
    public void addNode(Node node) {
        Preconditions.checkNotNull(node, "the node is null");
        nodeMap.put(node.getId(), node);
    }

    @Override
    public void removeNode(Node node) {
        Preconditions.checkNotNull(node, "the node is null");
    }


    public void setWeighter(Weighter weighter) {
        this.weighter = weighter;
    }

    /**
     * 获取node的权重
     */
    public int getWeight(Node node, Object any) {
        if (weighter != null) {
            return weighter.getWeight(node, any);
        }
        return 0;
    }

    protected abstract Node doSelect(List<Node> reachableNodes, Object any);

    @Override
    public Node select(List<Node> reachableNodes, Object any) {
        // 过滤掉没有注册的 node
        reachableNodes = Pipeline.of(reachableNodes).filter(new Predicate<Node>() {
            @Override
            public boolean test(Node node) {
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
        return doSelect(reachableNodes, any);
    }
}
