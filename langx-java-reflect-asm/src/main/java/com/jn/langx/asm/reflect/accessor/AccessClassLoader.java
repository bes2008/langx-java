package com.jn.langx.asm.reflect.accessor;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.WeakHashMap;

class AccessClassLoader extends ClassLoader {
    // Weak-references to class loaders, to avoid perm gen memory leaks, for example in app servers/web containters if the
    // reflectasm library (including this class) is loaded outside the deployed applications (WAR/EAR) using ReflectASM/Kryo (exts,
    // user classpath, etc).
    // The key is the parent class loader and the value is the AccessClassLoader, both are weak-referenced in the hash table.
    static private final WeakHashMap<ClassLoader, WeakReference<AccessClassLoader>> accessClassLoaders = new WeakHashMap();

    // Fast-path for classes loaded in the same ClassLoader as this class.
    static private final ClassLoader selfContextParentClassLoader = getParentClassLoader(AccessClassLoader.class);
    static private volatile AccessClassLoader selfContextAccessClassLoader = new AccessClassLoader(selfContextParentClassLoader);

    static private volatile Method defineClassMethod;

    private final HashSet<String> localClassNames = new HashSet();

    private AccessClassLoader(ClassLoader parent) {
        super(parent);
    }

    /**
     * Returns null if the access class has not yet been defined.
     */
    Class loadAccessClass(String name) {
        // No need to check the parent class loader if the access class hasn't been defined yet.
        if (localClassNames.contains(name)) {
            try {
                return loadClass(name, false);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex); // Should not happen, since we know the class has been defined.
            }
        }
        return null;
    }

    Class defineAccessClass(String name, byte[] bytes) throws ClassFormatError {
        localClassNames.add(name);
        return defineClass(name, bytes);
    }

    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        // These classes come from the classloader that loaded AccessClassLoader.
        if (name.equals(FieldAccessor.class.getName())) {
            return FieldAccessor.class;
        }
        if (name.equals(MethodAccessor.class.getName())) {
            return MethodAccessor.class;
        }
        if (name.equals(ConstructorAccessor.class.getName())) {
            return ConstructorAccessor.class;
        }
        if (name.equals(PublicConstructorAccessor.class.getName())) {
            return PublicConstructorAccessor.class;
        }
        // All other classes come from the classloader that loaded the type we are accessing.
        return super.loadClass(name, resolve);
    }

    Class<?> defineClass(String name, byte[] bytes) throws ClassFormatError {
        try {
            // Attempt to load the access class in the same loader, which makes protected and default access members accessible.
            return (Class<?>) getDefineClassMethod().invoke(getParent(),
                    new Object[]{name, bytes, Integer.valueOf(0), Integer.valueOf(bytes.length), getClass().getProtectionDomain()});
        } catch (Exception ignored) {
            // continue with the definition in the current loader (won't have access to protected and package-protected members)
        }
        return defineClass(name, bytes, 0, bytes.length, getClass().getProtectionDomain());
    }

    // As per JLS, section 5.3,
    // "The runtime package of a class or interface is determined by the package name and defining class loader of the class or
    // interface."
    static boolean areInSameRuntimeClassLoader(Class type1, Class type2) {
        if (type1.getPackage() != type2.getPackage()) {
            return false;
        }
        ClassLoader loader1 = type1.getClassLoader();
        ClassLoader loader2 = type2.getClassLoader();
        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        if (loader1 == null) {
            return (loader2 == null || loader2 == systemClassLoader);
        }
        if (loader2 == null) {
            return loader1 == systemClassLoader;
        }
        return loader1 == loader2;
    }

    static private ClassLoader getParentClassLoader(Class type) {
        ClassLoader parent = type.getClassLoader();
        if (parent == null) {
            parent = ClassLoader.getSystemClassLoader();
        }
        return parent;
    }

    static private Method getDefineClassMethod() throws Exception {
        if (defineClassMethod == null) {
            synchronized (accessClassLoaders) {
                if (defineClassMethod == null) {
                    defineClassMethod = ClassLoader.class.getDeclaredMethod("defineClass",
                            new Class[]{String.class, byte[].class, int.class, int.class, ProtectionDomain.class});
                    try {
                        defineClassMethod.setAccessible(true);
                    } catch (Exception ignored) {
                    }
                }
            }
        }
        return defineClassMethod;
    }

    static AccessClassLoader get(Class type) {
        ClassLoader parent = getParentClassLoader(type);
        // 1. fast-path:
        if (selfContextParentClassLoader.equals(parent)) {
            if (selfContextAccessClassLoader == null) {
                synchronized (accessClassLoaders) { // DCL with volatile semantics
                    if (selfContextAccessClassLoader == null) {
                        selfContextAccessClassLoader = new AccessClassLoader(selfContextParentClassLoader);
                    }
                }
            }
            return selfContextAccessClassLoader;
        }
        // 2. normal search:
        synchronized (accessClassLoaders) {
            WeakReference<AccessClassLoader> ref = accessClassLoaders.get(parent);
            if (ref != null) {
                AccessClassLoader accessClassLoader = ref.get();
                if (accessClassLoader != null)
                    return accessClassLoader;
                else
                    accessClassLoaders.remove(parent); // the value has been GC-reclaimed, but still not the key (defensive sanity)
            }
            AccessClassLoader accessClassLoader = new AccessClassLoader(parent);
            accessClassLoaders.put(parent, new WeakReference<AccessClassLoader>(accessClassLoader));
            return accessClassLoader;
        }
    }

    static public void remove(ClassLoader parent) {
        // 1. fast-path:
        if (selfContextParentClassLoader.equals(parent)) {
            selfContextAccessClassLoader = null;
        } else {
            // 2. normal search:
            synchronized (accessClassLoaders) {
                accessClassLoaders.remove(parent);
            }
        }
    }

    static public int activeAccessClassLoaders() {
        int sz = accessClassLoaders.size();
        if (selfContextAccessClassLoader != null) {
            sz++;
        }
        return sz;
    }
}
