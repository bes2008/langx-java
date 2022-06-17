package com.jn.langx.accessor;

import com.jn.langx.Accessor;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.reflect.Reflects;

import java.util.*;

public class Accessors {

    private Accessors() {
    }

    private static final Map<Class,AccessorFactory> accessorFactoryRegistry =new LinkedHashMap<Class, AccessorFactory>();
    static {
        Collects.forEach(ServiceLoader.load(AccessorFactory.class), new Consumer<AccessorFactory>() {
            @Override
            public void accept(AccessorFactory accessorFactory) {
                register(accessorFactory);
            }
        });
    }

    public static void register(final AccessorFactory accessorFactory) {
        final List<Class> classes=accessorFactory.applyTo();
        if(Objs.isNotEmpty(classes)){
            Collects.forEach(classes, new Consumer<Class>() {
                @Override
                public void accept(Class aClass) {
                    accessorFactoryRegistry.put(aClass, accessorFactory);
                }
            });

        }
    }

    public static <T> AccessorFactory findFactory(@NonNull final Class klass) {
        Preconditions.checkNotNull(klass);

        AccessorFactory factory = accessorFactoryRegistry.get(klass);
        if(factory==null){
            Set<Class> classes =accessorFactoryRegistry.keySet();
            Class matched =Collects.findFirst(classes, new Predicate<Class>() {
                @Override
                public boolean test(Class expected) {
                    return Reflects.isSubClassOrEquals(expected, klass);
                }
            });
            if(matched!=null){
                factory = accessorFactoryRegistry.get(matched);
            }
        }
        return factory;
    }

    public static <T> Accessor<String, T> of(@NonNull final Class klass) {
        AccessorFactory<T> factory = findFactory(klass);
        if(factory==null){
            return null;
        }
        return factory.get(klass);
    }

    public static <T> Accessor<String, T> of(@NonNull T object) {
        Preconditions.checkNotNull(object);
        Accessor<String, T> accessor = of(object.getClass());
        accessor.setTarget(object);
        return accessor;
    }

}
