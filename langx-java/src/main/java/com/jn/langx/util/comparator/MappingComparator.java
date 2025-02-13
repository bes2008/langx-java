package com.jn.langx.util.comparator;

import com.jn.langx.util.function.Function;

import java.util.Comparator;

public class MappingComparator<S,T> implements Comparator<S>{
    private Comparator<T> delegate;
    private Function<S,T> mapper;
    public MappingComparator(Comparator<T> delegate, Function<S,T> mapper){
        this.delegate= delegate;
        this.mapper = mapper;
    }

    @Override
    public int compare(S s1, S s2) {
        T t1 = mapper.apply(s1);
        T t2 = mapper.apply(s2);
        return delegate.compare(t1,t2);
    }
}
