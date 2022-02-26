package com.jn.langx.util.bean;

public interface ModelMapper<A, B> {
    B map(A a);
}
