package com.jn.langx.util.reflect;

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

/**
 * Package scope
 */
@Singleton
class ParameterServiceRegistry {
    private static final ParameterServiceRegistry INSTANCE = new ParameterServiceRegistry();
    private static final MethodParameterSupplier methodParameterSupplier;
    private static final ConstructorParameterSupplier constructorParameterSupplier;

    private ParameterServiceRegistry() {

    }

    static {
        boolean JDK_PARAMETER_FOUND = false;
        try {
            Class.forName("java.lang.reflect.Parameter");
            JDK_PARAMETER_FOUND = true;
        } catch (ClassNotFoundException ex) {
        }

        Map<Boolean, MethodParameterSupplier> methodParameterSupplierMap = loadMethodParameterSuppliers();
        MethodParameterSupplier defaultMethodParameterSupplier = methodParameterSupplierMap.get(JDK_PARAMETER_FOUND);
        if (defaultMethodParameterSupplier == null && JDK_PARAMETER_FOUND) {
            defaultMethodParameterSupplier = methodParameterSupplierMap.get(false);
        }
        methodParameterSupplier = defaultMethodParameterSupplier;


        Map<Boolean, ConstructorParameterSupplier> constructorParameterSupplierMap = loadConstructorParameterSuppliers();
        ConstructorParameterSupplier defaultConstructorParameterSupplier = constructorParameterSupplierMap.get(JDK_PARAMETER_FOUND);
        if (defaultConstructorParameterSupplier == null && JDK_PARAMETER_FOUND) {
            defaultConstructorParameterSupplier = constructorParameterSupplierMap.get(false);
        }
        constructorParameterSupplier = defaultConstructorParameterSupplier;
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

    static ParameterServiceRegistry getInstance() {
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

}
