package com.jn.langx.distributed.cluster.loadbalance;

public interface LoadBalancerAware {
    LoadBalancer getLoadBalancer();

    void setLoadBalancer(LoadBalancer loadBalancer);
}
