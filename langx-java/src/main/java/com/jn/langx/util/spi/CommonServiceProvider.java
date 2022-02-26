package com.jn.langx.util.spi;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.comparator.OrderedComparator;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

import java.util.*;

public class CommonServiceProvider<T> implements ServiceProvider<T> {

    private Predicate<T> predicate = Functions.<T>truePredicate();
    private Comparator<T> comparator = new OrderedComparator<T>();

    public void setPredicate(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public void setComparator(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Iterator<T> get(Class<T> serviceClass) {
        ServiceLoader<T> loader = ServiceLoader.load(serviceClass);
        Iterator<T> iter = loader.iterator();
        Collection<T> ret = Collects.emptyArrayList();
        while (iter.hasNext()) {
            T t = iter.next();
            if (predicate.test(t)) {
                ret.add(t);
            }
        }
        if (comparator != null) {
            ret = Collects.sort(ret, comparator);
        }
        return ret.iterator();
    }
}
