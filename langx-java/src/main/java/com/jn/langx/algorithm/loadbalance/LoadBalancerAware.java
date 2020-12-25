package com.jn.langx.algorithm.loadbalance;

public interface LoadBalancerAware {
    LoadBalancer getLoadBalancer();

    void setLoadBalancer(LoadBalancer loadBalancer);
}
