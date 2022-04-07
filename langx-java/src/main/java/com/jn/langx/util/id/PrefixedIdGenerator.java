package com.jn.langx.util.id;

import com.jn.langx.Delegatable;
import com.jn.langx.IdGenerator;
import com.jn.langx.util.function.supplier.PrefixSupplier;

/**
 * @since 4.4.7
 */
public class PrefixedIdGenerator<E> implements IdGenerator<E>, Delegatable<IdGenerator<E>> {
    private IdGenerator<E> delegate;

    private PrefixSupplier prefixSupplier;

    private String separator;

    public PrefixedIdGenerator(IdGenerator<E> delegate, PrefixSupplier supplier) {
        this(delegate, supplier, "_");
    }

    public PrefixedIdGenerator(IdGenerator<E> delegate, PrefixSupplier supplier, String separator) {
        this.prefixSupplier = supplier;
        this.separator = separator;
        setDelegate(delegate);
    }

    @Override
    public String get(E o) {
        String id = delegate.get(o);
        String prefix = prefixSupplier.get(o);
        return prefix + separator + id;
    }

    @Override
    public String get() {
        return get(null);
    }

    @Override
    public IdGenerator<E> getDelegate() {
        return delegate;
    }

    @Override
    public void setDelegate(IdGenerator<E> delegate) {
        this.delegate = delegate;
    }
}
