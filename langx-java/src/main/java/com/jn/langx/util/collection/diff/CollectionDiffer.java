package com.jn.langx.util.collection.diff;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Differ;
import com.jn.langx.util.comparator.EqualsComparator;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Mapper;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.struct.Pair;

import java.util.*;

/**
 * @param <E>
 * @author jinuo.fang
 */
public class CollectionDiffer<E> implements Differ<Collection<E>, CollectionDiffResult<E>> {
    private Comparator<E> comparator;
    private KeyBuilder<String, E> keyBuilder;

    public void diffUsingMap(@Nullable KeyBuilder<String, E> keyBuilder) {
        this.keyBuilder = keyBuilder;
    }

    public void setComparator(@Nullable Comparator<E> comparator) {
        this.comparator = comparator;
    }

    @Override
    public CollectionDiffResult<E> diff(@Nullable Collection<E> oldCollection, @Nullable Collection<E> newCollection) {
        CollectionDiffResult<E> result = new CollectionDiffResult<E>();

        if (Objs.isEmpty(oldCollection) && Objs.isEmpty(newCollection)) {
            return result;
        }

        if (oldCollection == null) {
            result.setAdds(newCollection);
            return result;
        }

        if (newCollection == null) {
            result.setRemoves(oldCollection);
            return result;
        }

        if (isDiffUsingMapDiffer()) {
            Map<String, E> oldMap = Collects.map(oldCollection, new Mapper<E, Pair<String, E>>() {
                @Override
                public Pair<String, E> apply(E element) {
                    return new com.jn.langx.util.struct.Entry(keyBuilder.getKey(element), element);
                }
            });

            Map<String, E> newMap = Collects.map(newCollection, new Mapper<E, Pair<String, E>>() {
                @Override
                public Pair<String, E> apply(E element) {
                    return new com.jn.langx.util.struct.Entry(keyBuilder.getKey(element), element);
                }
            });

            MapDiffer<String, E> mapDiffer = new MapDiffer<String, E>();
            mapDiffer.setValueComparator(comparator);
            mapDiffer.setKeyComparator(new EqualsComparator<String>());
            MapDiffResult<String, E> dr = mapDiffer.diff(oldMap, newMap);
            result.setAdds(dr.getAdds().values());
            result.setRemoves(dr.getRemoves().values());
            result.setEquals(dr.getEquals().values());
            result.setUpdates(dr.getUpdates().values());
        } else {
            doDiff(oldCollection, newCollection, result);
        }
        return result;
    }

    private boolean isDiffUsingMapDiffer() {
        return this.keyBuilder != null;
    }


    private void doDiff(final Collection<E> oldCollection, final Collection<E> newCollection, CollectionDiffResult<E> result) {
        final List<E> adds = new ArrayList<E>();
        final List<E> removes = new ArrayList<E>();
        final List<E> equals = new ArrayList<E>();
        final Comparator<E> comp =  comparator == null ? new EqualsComparator<E>():comparator;
        Collects.forEach(newCollection, new Consumer<E>() {
            @Override
            public void accept(final E newValue) {
                if (Collects.anyMatch(oldCollection, new Predicate<E>() {
                    @Override
                    public boolean test(E oldValue) {
                        return comp.compare(oldValue, newValue) == 0;
                    }
                })) {
                    equals.add(newValue);
                } else {
                    adds.add(newValue);
                }
            }
        });

        Collects.forEach(oldCollection, new Consumer<E>() {
            @Override
            public void accept(final E oldValue) {
                if (Collects.noneMatch(newCollection, new Predicate<E>() {
                    @Override
                    public boolean test(E newValue) {
                        return comp.compare(oldValue, newValue) == 0;
                    }
                })) {
                    removes.add(oldValue);
                }
            }
        });

        result.setAdds(adds);
        result.setRemoves(removes);
        result.setEquals(equals);
    }

}
