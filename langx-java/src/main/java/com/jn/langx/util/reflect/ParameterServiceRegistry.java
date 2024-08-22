package com.jn.langx.util.reflect;

import com.jn.langx.annotation.Name;
import com.jn.langx.annotation.Singleton;
import com.jn.langx.registry.Registry;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.reflect.parameter.*;
import com.jn.langx.util.spi.CommonServiceProvider;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

@Singleton
public class ParameterServiceRegistry implements Registry<String, ParameterSupplier> {
    private volatile static ParameterServiceRegistry INSTANCE;
    private static final MethodParameterSupplier methodParameterSupplier;
    private static final ConstructorParameterSupplier constructorParameterSupplier;
    private static final Map<String, MethodParameterSupplier> methodParameterSupplierRegistry = new HashMap<String, MethodParameterSupplier>();
    private static final Map<String, ConstructorParameterSupplier> constructorParameterSupplierRegistry = new HashMap<String, ConstructorParameterSupplier>();

    public static final String JAVA_6_SUPPLIER_NAME = "langx_java6";
    public static final String JAVA_8_SUPPLIER_NAME = "langx_java8";

    private ParameterServiceRegistry() {

    }

    static {
        boolean JDK_PARAMETER_FOUND = false;
        try {
            Class.forName("java.lang.reflect.Parameter");
            JDK_PARAMETER_FOUND = true;
        } catch (ClassNotFoundException ex) {
            // ignore it
        }

        loadMethodParameterSuppliers();
        String defaultName = JDK_PARAMETER_FOUND ? JAVA_8_SUPPLIER_NAME : JAVA_6_SUPPLIER_NAME;
        MethodParameterSupplier defaultMethodParameterSupplier = methodParameterSupplierRegistry.get(defaultName);
        if (defaultMethodParameterSupplier == null && JDK_PARAMETER_FOUND) {
            defaultMethodParameterSupplier = methodParameterSupplierRegistry.get(JAVA_6_SUPPLIER_NAME);
        }
        methodParameterSupplier = defaultMethodParameterSupplier;

        loadConstructorParameterSuppliers();
        ConstructorParameterSupplier defaultConstructorParameterSupplier = constructorParameterSupplierRegistry.get(defaultName);
        if (defaultConstructorParameterSupplier == null && JDK_PARAMETER_FOUND) {
            defaultConstructorParameterSupplier = constructorParameterSupplierRegistry.get(JAVA_6_SUPPLIER_NAME);
        }
        constructorParameterSupplier = defaultConstructorParameterSupplier;

    }

    public static ParameterServiceRegistry getInstance() {
        if (INSTANCE == null) {
            synchronized (ParameterServiceRegistry.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ParameterServiceRegistry();
                }
            }
        }
        return INSTANCE;
    }

    private static void loadMethodParameterSuppliers() {
        Collects.forEach(CommonServiceProvider.loadService(MethodParameterSupplier.class), new Consumer<MethodParameterSupplier>() {
            @Override
            public void accept(MethodParameterSupplier supplier) {
                if (supplier.getClass().getPackage().getName().startsWith("com.jn.langx.")) {
                    registerInternal(supplier);
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
                    registerInternal(supplier);
                }
            }
        });
    }


    public List<MethodParameter> getMethodParameters(final Method method) {
        return getMethodParameters(null, method);
    }

    public List<MethodParameter> getMethodParameters(final String supplierName, final Method method) {
        Preconditions.checkNotNull(method);
        int count = method.getParameterTypes().length;
        final List<MethodParameter> parameters = Collects.newArrayList();
        if (count > 0) {
            Collects.forEach(Arrs.range(count), new Consumer<Integer>() {
                @Override
                public void accept(Integer index) {
                    parameters.add(getMethodParameter(supplierName, method, index));
                }
            });
        }
        return parameters;
    }

    public MethodParameter getMethodParameter(Method method, int index) {
        return getMethodParameter(null, method, index);
    }

    public MethodParameter getMethodParameter(String supplierName, Method method, int index) {
        return getMethodParameter(supplierName, "arg" + index, 0, method, index);
    }

    public MethodParameter getMethodParameter(String name, int modifiers, Method method, int index) {
        return getMethodParameter(null, name, modifiers, method, index);
    }

    public MethodParameter getMethodParameter(String supplierName, String name, int modifiers, Method method, int index) {
        return findMethodParameterSupplier(supplierName, true).get(new ParameterMeta(name, modifiers, method, index));
    }

    public List<ConstructorParameter> getConstructorParameters(final Constructor constructor) {
        return getConstructorParameters(null, constructor);
    }

    public List<ConstructorParameter> getConstructorParameters(final String supplierName, final Constructor constructor) {
        Preconditions.checkNotNull(constructor);
        int count = constructor.getParameterTypes().length;
        final List<ConstructorParameter> parameters = Collects.newArrayList();
        if (count > 0) {
            Collects.forEach(Arrs.range(count), new Consumer<Integer>() {
                @Override
                public void accept(Integer index) {
                    parameters.add(getConstructorParameter(supplierName, constructor, index));
                }
            });
        }
        return parameters;
    }

    public ConstructorParameter getConstructorParameter(Constructor constructor, int index) {
        return getConstructorParameter(null, constructor, index);
    }

    public ConstructorParameter getConstructorParameter(String supplierName, Constructor constructor, int index) {
        return getConstructorParameter(supplierName, "arg" + index, 0, constructor, index);
    }

    public ConstructorParameter getConstructorParameter(String name, int modifiers, Constructor constructor, int index) {
        return getConstructorParameter(null, name, modifiers, constructor, index);
    }

    public ConstructorParameter getConstructorParameter(String supplierName, String name, int modifiers, Constructor constructor, int index) {
        return findConstructorParameterSupplier(supplierName, true).get(new ParameterMeta(name, modifiers, constructor, index));
    }


    public MethodParameterSupplier getDefaultMethodParameterSupplier() {
        return methodParameterSupplier;
    }

    public ConstructorParameterSupplier getDefaultConstructorParameterSupplier() {
        return constructorParameterSupplier;
    }

    public MethodParameterSupplier findMethodParameterSupplier(String name) {
        return findMethodParameterSupplier(name, true);
    }

    public MethodParameterSupplier findMethodParameterSupplier(String name, boolean useDefaultIfNotFound) {
        MethodParameterSupplier supplier = methodParameterSupplierRegistry.get(name);
        if (supplier == null && useDefaultIfNotFound) {
            return getDefaultMethodParameterSupplier();
        }
        return supplier;
    }

    public ConstructorParameterSupplier findConstructorParameterSupplier(String name) {
        return findConstructorParameterSupplier(name, true);
    }

    public ConstructorParameterSupplier findConstructorParameterSupplier(String name, boolean useDefaultIfNotFound) {
        ConstructorParameterSupplier supplier = constructorParameterSupplierRegistry.get(name);
        if (supplier == null && useDefaultIfNotFound) {
            return getDefaultConstructorParameterSupplier();
        }
        return supplier;
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

    private static void registerInternal(ConstructorParameterSupplier supplier) {
        Class<? extends ConstructorParameterSupplier> clazz = supplier.getClass();
        String name = Reflects.getFQNClassName(clazz);
        constructorParameterSupplierRegistry.put(name, supplier);
        if (Reflects.hasAnnotation(clazz, Name.class)) {
            Name nameAnno = Reflects.getAnnotation(clazz, Name.class);
            name = nameAnno.value();
            constructorParameterSupplierRegistry.put(name, supplier);
        }
    }

    private static void registerInternal(MethodParameterSupplier supplier) {
        Class<? extends MethodParameterSupplier> clazz = supplier.getClass();
        String name = Reflects.getFQNClassName(clazz);
        methodParameterSupplierRegistry.put(name, supplier);
        if (Reflects.hasAnnotation(clazz, Name.class)) {
            Name nameAnno = Reflects.getAnnotation(clazz, Name.class);
            name = nameAnno.value();
            methodParameterSupplierRegistry.put(name, supplier);
        }

    }

    @Override
    public void register(ParameterSupplier parameterSupplier) {
        if (parameterSupplier != null) {
            if (parameterSupplier instanceof MethodParameterSupplier) {
                registerInternal((MethodParameterSupplier) parameterSupplier);
            } else if (parameterSupplier instanceof ConstructorParameterSupplier) {
                registerInternal((ConstructorParameterSupplier) parameterSupplier);
            }
        }
    }

    @Override
    public void register(String name, ParameterSupplier parameterSupplier) {
        if (parameterSupplier != null) {
            if (parameterSupplier instanceof MethodParameterSupplier) {
                register(name, (MethodParameterSupplier) parameterSupplier);
            } else if (parameterSupplier instanceof ConstructorParameterSupplier) {
                register(name, (ConstructorParameterSupplier) parameterSupplier);
            }
        }
    }

    @Override
    public void unregister(String key) {
        methodParameterSupplierRegistry.remove(key);
        constructorParameterSupplierRegistry.remove(key);
    }

    @Override
    public boolean contains(String key) {
        return methodParameterSupplierRegistry.containsKey(key) || constructorParameterSupplierRegistry.containsKey(key);
    }

    @Override
    public ParameterSupplier get(String input) {
        return null;
    }
}
