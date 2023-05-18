package com.jn.langx.util.spi;

import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.comparator.OrderedComparator;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.util.*;

public class CommonServiceProvider<T> implements ServiceProvider<T> {
    private static Logger logger = Loggers.getLogger(CommonServiceProvider.class);
    private Predicate<T> predicate = Functions.truePredicate();
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
            try {
                T t = iter.next();
                if (predicate.test(t)) {
                    ret.add(t);
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }

        }
        if (comparator != null) {
            ret = Collects.sort(ret, comparator);
        }
        return ret.iterator();
    }
}
