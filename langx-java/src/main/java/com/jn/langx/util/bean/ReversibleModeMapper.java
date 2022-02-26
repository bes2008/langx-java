package com.jn.langx.util.bean;

public interface ReversibleModeMapper<A, B> extends ModelMapper<A, B> {
    @Override
    B map(A a);

    A reverseMap(B b);
}
