package com.jn.langx.util.reflect;

import com.jn.langx.annotation.Name;
import com.jn.langx.annotation.Singleton;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
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

        loadMethodParameterSuppliers();
        String defaultName = JDK_PARAMETER_FOUND ? "langx_java8" : "langx_java6";
        MethodParameterSupplier defaultMethodParameterSupplier = methodParameterSupplierRegistry.get(defaultName);
        if (defaultMethodParameterSupplier == null && JDK_PARAMETER_FOUND) {
            defaultMethodParameterSupplier = methodParameterSupplierRegistry.get("langx_java6");
        }
        methodParameterSupplier = defaultMethodParameterSupplier;

        loadConstructorParameterSuppliers();
        ConstructorParameterSupplier defaultConstructorParameterSupplier = constructorParameterSupplierRegistry.get(defaultName);
        if (defaultConstructorParameterSupplier == null && JDK_PARAMETER_FOUND) {
            defaultConstructorParameterSupplier = constructorParameterSupplierRegistry.get("langx_java6");
        }
        constructorParameterSupplier = defaultConstructorParameterSupplier;

    }

    private static void loadMethodParameterSuppliers() {
        ServiceLoader<MethodParameterSupplier> loader = ServiceLoader.load(MethodParameterSupplier.class);
        Collects.forEach(loader, new Consumer<MethodParameterSupplier>() {
            @Override
            public void accept(MethodParameterSupplier supplier) {
                if (supplier.getClass().getPackage().getName().startsWith("com.jn.langx.")) {
                    Class<? extends MethodParameterSupplier> clazz = supplier.getClass();
                    String name = Reflects.getFQNClassName(clazz);
                    methodParameterSupplierRegistry.put(name, supplier);
                    if (Reflects.hasAnnotation(clazz, Name.class)) {
                        Name nameAnno = Reflects.getAnnotation(clazz, Name.class);
                        name = nameAnno.value();
                        methodParameterSupplierRegistry.put(name, supplier);
                    }

                }
            }
        });
    }


    private static void loadConstructorParameterSuppliers() {
        ServiceLoader<ConstructorParameterSupplier> loader = ServiceLoader.load(ConstructorParameterSupplier.class);
        Collects.forEach(loader, new Consumer<ConstructorParameterSupplier>() {
            @Override
            public void accept(final ConstructorParameterSupplier supplier) {
                if (supplier.getClass().getPackage().getName().startsWith("com.jn.langx.")) {
                    Class<? extends ConstructorParameterSupplier> clazz = supplier.getClass();
                    String name = Reflects.getFQNClassName(clazz);
                    constructorParameterSupplierRegistry.put(name, supplier);
                    if (Reflects.hasAnnotation(clazz, Name.class)) {
                        Name nameAnno = Reflects.getAnnotation(clazz, Name.class);
                        name = nameAnno.value();
                        constructorParameterSupplierRegistry.put(name, supplier);
                    }

                }
            }
        });
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
