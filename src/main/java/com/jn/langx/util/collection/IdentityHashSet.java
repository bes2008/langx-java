package com.jn.langx.util.collection;


import java.util.AbstractSet;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * An {@link IdentityHashMap}-backed {@link Set}.
 *
 * @author The Apache Directory Project (mina-dev@directory.apache.org)
 * @version $Rev: 555855 $, $Date: 2007-07-13 12:19:00 +0900 (Fri, 13 Jul 2007) $
 */
public class IdentityHashSet<E> extends AbstractSet<E> {
    private final Map<E, Boolean> delegate = new IdentityHashMap<E, Boolean>();

    public IdentityHashSet() {
    }

    public IdentityHashSet(Collection<E> c) {
        addAll(c);
    }

    @Override
    public int size() {
        return delegate.size();
    }

    @Override
    public boolean contains(Object o) {
        //noinspection SuspiciousMethodCalls
        return delegate.containsKey(o);
    }

    @Override
    public Iterator<E> iterator() {
        return delegate.keySet().iterator();
    }

    @Override
    public boolean add(E arg0) {
        return delegate.put(arg0, Boolean.TRUE) == null;
    }

    @Override
    public boolean remove(Object o) {
        //noinspection SuspiciousMethodCalls
        return delegate.remove(o) != null;
    }

    @Override
    public void clear() {
        delegate.clear();
    }
}
