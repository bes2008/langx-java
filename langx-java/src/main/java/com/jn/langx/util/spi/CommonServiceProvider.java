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
        Collection<T> ret = Collects.emptyArrayList();
        int serviceInstanceCount = loadServices(serviceClass, Thread.currentThread().getContextClassLoader(), ret);
        if(serviceInstanceCount==0 && Thread.currentThread().getContextClassLoader() != serviceClass.getClassLoader()){
            loadServices(serviceClass, serviceClass.getClassLoader(), ret);
        }
        if (comparator != null) {
            ret = Collects.sort(ret, comparator);
        }
        return ret.iterator();
    }

    private int loadServices(Class<T> serviceClass, ClassLoader classLoader, Collection<T> tmpStorage){
        ServiceLoader<T> loader = ServiceLoader.load(serviceClass, classLoader);
        Iterator<T> iter = loader.iterator();
        int loadedServiceInstanceCount=0;
        while (iter.hasNext()) {
            try {
                T t = iter.next();
                if (predicate.test(t)) {
                    tmpStorage.add(t);
                }
            }catch (Exception e){
                logger.error(e.getMessage(),e);
            }
            loadedServiceInstanceCount++;
        }
        return loadedServiceInstanceCount;
    }

    public static <T> Iterable<T> loadService(Class<T> serviceClass){
        return Collects.asIterable(new CommonServiceProvider<T>().get(serviceClass));
    }
}
