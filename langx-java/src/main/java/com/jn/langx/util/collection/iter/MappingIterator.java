package com.jn.langx.util.collection.iter;

import com.jn.langx.util.function.Function;

import java.util.Iterator;

public class MappingIterator<W, E> extends UnmodifiableIterator<E> {
    private Iterator<W> delegate;
    private Function<W, E> mapper;

    public MappingIterator(Iterable<W> wrapperIterator, Function<W, E> mapper) {
        this.delegate = wrapperIterator.iterator();
        this.mapper = mapper;
    }

    public MappingIterator(Iterator<W> wrapperIterator, Function<W, E> mapper) {
        this.delegate = wrapperIterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    @Override
    public E next() {
        return mapper.apply(delegate.next());
    }
}
