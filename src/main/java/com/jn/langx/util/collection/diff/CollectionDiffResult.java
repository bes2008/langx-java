package com.jn.langx.util.collection.diff;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Preconditions;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @param <E>
 * @author jinuo.fang
 */
public class CollectionDiffResult<E> implements CollectionDifferResult<Collection<E>> {

    private Collection<E> adds = new ArrayList<E>();
    private Collection<E> removes = new ArrayList<E>();
    private Collection<E> updates = new ArrayList<E>();
    private Collection<E> equals = new ArrayList<E>();

    @Override
    public Collection<E> getAdds() {
        return adds;
    }

    public void setAdds(@Nullable Collection<E> adds) {
        Preconditions.checkNotNull(adds);
        this.adds = adds;
    }

    @Override
    public Collection<E> getRemoves() {
        return removes;
    }

    public void setRemoves(@Nullable Collection<E> removes) {
        Preconditions.checkNotNull(removes);
        this.removes = removes;
    }

    @Override
    public Collection<E> getUpdates() {
        return updates;
    }

    public void setUpdates(@Nullable Collection<E> updates) {
        Preconditions.checkNotNull(updates);
        this.updates = updates;
    }

    @Override
    public Collection<E> getEquals() {
        return equals;
    }

    public void setEquals(@Nullable Collection<E> equals) {
        Preconditions.checkNotNull(equals);
        this.equals = equals;
    }

}
