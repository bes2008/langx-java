package com.jn.langx.util.collection.diff;

import com.jn.langx.util.collection.DiffResult;

import java.util.ArrayList;
import java.util.Collection;

public class CollectionDiffResult<E> implements DiffResult<Collection<E>> {

    private Collection<E> adds = new ArrayList<E>();
    private Collection<E> removes = new ArrayList<E>();
    private Collection<E> updates = new ArrayList<E>();
    private Collection<E> equals = new ArrayList<E>();

    @Override
    public Collection<E> getAdds() {
        return adds;
    }

    public void setAdds(Collection<E> adds) {
        this.adds = adds;
    }

    @Override
    public Collection<E> getRemoves() {
        return removes;
    }

    public void setRemoves(Collection<E> removes) {
        this.removes = removes;
    }

    @Override
    public Collection<E> getUpdates() {
        return updates;
    }

    public void setUpdates(Collection<E> updates) {
        this.updates = updates;
    }

    @Override
    public Collection<E> getEquals() {
        return equals;
    }

    public void setEquals(Collection<E> equals) {
        this.equals = equals;
    }

}
