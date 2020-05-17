package com.jn.langx.util.reflect;

import com.jn.langx.annotation.Name;
import com.jn.langx.annotation.Singleton;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.reflect.parameter.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

@Singleton
public class ParameterServiceRegistry {
    private static final ParameterServiceRegistry INSTANCE = new ParameterServiceRegistry();
    private static final MethodParameterSupplier methodParameterSupplier;
    private static final ConstructorParameterSupplier constructorParameterSupplier;
    private static final Map<String, MethodParameterSupplier> methodParameterSupplierRegistry = new HashMap<String, MethodParameterSupplier>();
    private static final Map<String, ConstructorParameterSupplier> constructorParameterSupplierRegistry = new HashMap<String, ConstructorParameterSupplier>();

    private ParameterServiceRegistry() {

    }

    static {
        boolean JDK_PARAMETER_FOUND = false;
        try {
            Class.forName("java.lang.reflect.Parameter");
            JDK_PARAMETER_FOUND = true;
        } catch (ClassNotFoundException ex) {
        }

        Map<Boolean, MethodParameterSupplier> _methodParameterSupplierMap = loadMethodParameterSuppliers();
        MethodParameterSupplier defaultMethodParameterSupplier = _methodParameterSupplierMap.get(JDK_PARAMETER_FOUND);
        if (defaultMethodParameterSupplier == null && JDK_PARAMETER_FOUND) {
            defaultMethodParameterSupplier = _methodParameterSupplierMap.get(false);
        }
        methodParameterSupplier = defaultMethodParameterSupplier;
        Collects.forEach(_methodParameterSupplierMap, new Consumer2<Boolean, MethodParameterSupplier>() {
            @Override
            public void accept(Boolean key, MethodParameterSupplier supplier) {
                Class<? extends MethodParameterSupplier> clazz = supplier.getClass();
                String name = clazz.getSimpleName();
                if (Reflects.hasAnnotation(clazz, Name.class)) {
                    Name nameAnno = Reflects.getAnnotation(clazz, Name.class);
                    name = nameAnno.value();
                }
                methodParameterSupplierRegistry.put(name, supplier);
            }
        });

        Map<Boolean, ConstructorParameterSupplier> _constructorParameterSupplierMap = loadConstructorParameterSuppliers();
        ConstructorParameterSupplier defaultConstructorParameterSupplier = _constructorParameterSupplierMap.get(JDK_PARAMETER_FOUND);
        if (defaultConstructorParameterSupplier == null && JDK_PARAMETER_FOUND) {
            defaultConstructorParameterSupplier = _constructorParameterSupplierMap.get(false);
        }
        constructorParameterSupplier = defaultConstructorParameterSupplier;
        Collects.forEach(_constructorParameterSupplierMap, new Consumer2<Boolean, ConstructorParameterSupplier>() {
            @Override
            public void accept(Boolean key, ConstructorParameterSupplier supplier) {
                Class<? extends ConstructorParameterSupplier> clazz = supplier.getClass();
                String name = clazz.getSimpleName();
                if (Reflects.hasAnnotation(clazz, Name.class)) {
                    Name nameAnno = Reflects.getAnnotation(clazz, Name.class);
                    name = nameAnno.value();
                }
                constructorParameterSupplierRegistry.put(name, supplier);
            }
        });

    }

    private static Map<Boolean, MethodParameterSupplier> loadMethodParameterSuppliers() {
        ServiceLoader<MethodParameterSupplier> loader = ServiceLoader.load(MethodParameterSupplier.class);
        final Map<Boolean, MethodParameterSupplier> methodParameterSupplierRegistry = new HashMap<Boolean, MethodParameterSupplier>();
        Collects.forEach(loader, new Consumer<MethodParameterSupplier>() {
            @Override
            public void accept(MethodParameterSupplier methodParameterSupplier) {
                if (methodParameterSupplier.getClass().getPackage().getName().startsWith("com.jn.langx.")) {
                    methodParameterSupplierRegistry.put(methodParameterSupplier.usingJdkApi(), methodParameterSupplier);
                }
            }
        });
        return methodParameterSupplierRegistry;
    }


    private static Map<Boolean, ConstructorParameterSupplier> loadConstructorParameterSuppliers() {
        ServiceLoader<ConstructorParameterSupplier> loader = ServiceLoader.load(ConstructorParameterSupplier.class);
        final Map<Boolean, ConstructorParameterSupplier> constructorParameterSupplierRegistry = new HashMap<Boolean, ConstructorParameterSupplier>();
        Collects.forEach(loader, new Consumer<ConstructorParameterSupplier>() {
            @Override
            public void accept(ConstructorParameterSupplier constructorParameterSupplier) {
                if (constructorParameterSupplier.getClass().getPackage().getName().startsWith("com.jn.langx.")) {
                    constructorParameterSupplierRegistry.put(constructorParameterSupplier.usingJdkApi(), constructorParameterSupplier);
                }
            }
        });
        return constructorParameterSupplierRegistry;
    }

    public static ParameterServiceRegistry getInstance() {
        return INSTANCE;
    }


    public List<MethodParameter> getMethodParameters(final Method method) {
        Preconditions.checkNotNull(method);
        int count = method.getParameterTypes().length;
        final List<MethodParameter> parameters = Collects.newArrayList();
        if (count > 0) {
            Collects.forEach(Arrs.range(count), new Consumer<Integer>() {
                @Override
                public void accept(Integer index) {
                    parameters.add(getMethodParameter(method, index));
                }
            });
        }
        return parameters;
    }

    public MethodParameter getMethodParameter(Method method, int index) {
        return getMethodParameter("arg" + index, 0, method, index);
    }

    public MethodParameter getMethodParameter(String name, int modifiers, Method method, int index) {
        return methodParameterSupplier.get(new ParameterMeta(name, modifiers, method, index));
    }

    public List<ConstructorParameter> getConstructorParameters(final Constructor constructor) {
        Preconditions.checkNotNull(constructor);
        int count = constructor.getParameterTypes().length;
        final List<ConstructorParameter> parameters = Collects.newArrayList();
        if (count > 0) {
            Collects.forEach(Arrs.range(count), new Consumer<Integer>() {
                @Override
                public void accept(Integer index) {
                    parameters.add(getConstructorParameter(constructor, index));
                }
            });
        }
        return parameters;
    }

    public ConstructorParameter getConstructorParameter(Constructor constructor, int index) {
        return getConstructorParameter("arg" + index, 0, constructor, index);
    }

    public ConstructorParameter getConstructorParameter(String name, int modifiers, Constructor constructor, int index) {
        return constructorParameterSupplier.get(new ParameterMeta(name, modifiers, constructor, index));
    }

    public MethodParameterSupplier getDefaultMethodParameterSupplier() {
        return methodParameterSupplier;
    }

    public ConstructorParameterSupplier getDefaultConstructorParameterSupplier() {
        return constructorParameterSupplier;
    }

    public MethodParameterSupplier findMethodParameterSupplier(String name) {
        return methodParameterSupplierRegistry.get(name);
    }

    public ConstructorParameterSupplier findConstructorParameterSupplier(String name) {
        return constructorParameterSupplierRegistry.get(name);
    }

    public void register(String name, MethodParameterSupplier supplier) {
        if (methodParameterSupplierRegistry.get(name) == null) {
            methodParameterSupplierRegistry.put(name, supplier);
        }
    }

    public void register(String name, ConstructorParameterSupplier supplier) {
        if (constructorParameterSupplierRegistry.get(name) == null) {
            constructorParameterSupplierRegistry.put(name, supplier);
        }
    }

}
