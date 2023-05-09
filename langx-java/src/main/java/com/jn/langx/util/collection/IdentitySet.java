package com.jn.langx.util.collection;


import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Set implementation that use == instead of equals() as its comparison
 * mechanism.  This is achieved by internally using an IdentityHashMap.
 *
 * @author Emmanuel Bernard
 */
public class IdentitySet implements Set {
    private static final Object DUMP_VALUE = new Object();

    private final IdentityHashMap map;

    /**
     * Create an IdentitySet with default sizing.
     */
    public IdentitySet() {
        this.map = new IdentityHashMap();
    }

    /**
     * Create an IdentitySet with the given sizing.
     *
     * @param sizing The sizing of the set to create.
     */
    public IdentitySet(int sizing) {
        this.map = new IdentityHashMap(sizing);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean contains(Object o) {
        return map.get(o) == DUMP_VALUE;
    }

    public Iterator iterator() {
        return map.keySet().iterator();
    }

    public Object[] toArray() {
        return map.keySet().toArray();
    }

    public Object[] toArray(Object[] a) {
        return map.keySet().toArray(a);
    }

    public boolean add(Object o) {
        return map.put(o, DUMP_VALUE) == null;
    }

    public boolean remove(Object o) {
        return map.remove(o) == DUMP_VALUE;
    }

    public boolean containsAll(Collection c) {
        for (Object o : c) {
            if (!map.containsKey(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection c) {
        Iterator it = c.iterator();
        boolean changed = false;
        while (it.hasNext()) {
            if (this.add(it.next())) {
                changed = true;
            }
        }
        return changed;
    }

    public boolean retainAll(Collection c) {
        //doable if needed
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(Collection c) {
        Iterator it = c.iterator();
        boolean changed = false;
        while (it.hasNext()) {
            if (this.remove(it.next())) {
                changed = true;
            }
        }
        return changed;
    }

    public void clear() {
        map.clear();
    }
}
