package com.jn.langx.cluster.loadbalance;

public interface LoadBalancerAware {
    LoadBalancer getLoadBalancer();

    void setLoadBalancer(LoadBalancer loadBalancer);
}
