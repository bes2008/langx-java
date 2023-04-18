package com.jn.langx.util.io.close;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.Closer;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;

import java.io.Closeable;
import java.util.Map;
import java.util.ServiceLoader;

public class ObjectCloser {
    private static Map<Class, Closer> closerMap = Collects.emptyHashMap(true);
    private static ForceCloser forceCloser = new ForceCloser();

    static {
        Collects.forEach(ServiceLoader.load(Closer.class), new Consumer<Closer>() {
            @Override
            public void accept(final Closer closer) {
                register(closer);
            }
        });
    }

    private ObjectCloser(){

    }
    public static void register(final Closer closer) {
        if (closer != null) {
            Collects.forEach(closer.applyTo(), new Consumer<Class>() {
                @Override
                public void accept(Class aClass) {
                    closerMap.put(aClass, closer);
                }
            });
        }
    }

    public static void close(Object obj) {
        if (obj == null) {
            return;
        }

        if(obj instanceof Closeable){
            try {
                ((Closeable) obj).close();
            }catch (Exception e){
                Loggers.getLogger(ObjectCloser.class).warn("close fail: {}", obj);
            }
        }
        Class type = obj.getClass();
        findCloser(type).close(obj);
    }

    private static Closer findCloser(final Class type) {
        Preconditions.checkNotNull(type);
        Closer closer = closerMap.get(type);
        if (closer == null) {
            Class t = Collects.findFirst(closerMap.keySet(), new Predicate<Class>() {
                @Override
                public boolean test(Class expectClass) {
                    return Reflects.isSubClassOrEquals(expectClass, type);
                }
            });

            if (t != null) {
                closer = closerMap.get(t);
            }
        }

        if (closer == null) {
            closer = forceCloser;
        }

        return closer;
    }


}
