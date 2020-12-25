package com.jn.langx.algorithm.loadbalance;

public interface LoadBalancerAware<NODE extends Node, INVOCATION> {
    LoadBalancer<NODE, INVOCATION> getLoadBalancer();

    void setLoadBalancer(LoadBalancer<NODE, INVOCATION> loadBalancer);
}
