package com.jn.langx.algorithm.loadbalance;

import com.jn.langx.util.random.ThreadLocalRandom;

import java.util.List;

public abstract class RandomLoadBalanceStrategy extends AbstractLBStrategy {

    @Override
    protected Node doSelect(List<Node> aliveNodes, Object any) {
        int length = aliveNodes.size();
        // Every invoker has the same weight?
        boolean sameWeight = true;
        // the maxWeight of every invokers, the minWeight = 0 or the maxWeight of the last invoker
        int[] weights = new int[length];
        // The sum of weights
        int totalWeight = 0;
        for (int i = 0; i < length; i++) {
            int weight = getWeight(aliveNodes.get(i), any);
            totalWeight += weight;
            // save for later use
            weights[i] = totalWeight;
            if (sameWeight && totalWeight != weight * (i + 1)) {
                sameWeight = false;
            }
        }
        if (totalWeight > 0 && !sameWeight) {
            // If (not every node has the same weight & at least one node's weight>0), select randomly based on totalWeight.
            int offset = ThreadLocalRandom.current().nextInt(totalWeight);
            // Return a node based on the random value.
            for (int i = 0; i < length; i++) {
                if (offset < weights[i]) {
                    return aliveNodes.get(i);
                }
            }
        }
        // If all invokers have the same weight value or totalWeight=0, return evenly.
        return aliveNodes.get(ThreadLocalRandom.current().nextInt(length));
    }
}
