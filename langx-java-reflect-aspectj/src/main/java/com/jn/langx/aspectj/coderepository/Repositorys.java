package com.jn.langx.aspectj.coderepository;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.ConcurrentReferenceHashMap;
import com.jn.langx.util.reflect.Reflects;
import com.jn.langx.util.reflect.reference.ReferenceType;
import org.aspectj.apache.bcel.classfile.JavaClass;
import org.aspectj.apache.bcel.util.ClassLoaderRepository;
import org.aspectj.apache.bcel.util.ClassPath;
import org.aspectj.apache.bcel.util.Repository;
import org.aspectj.apache.bcel.util.SyntheticRepository;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

public class Repositorys {

    private static Repository bootstrapRepository = new ClassLoaderRepository((ClassLoader) null);

    private static ConcurrentReferenceHashMap<ClassLoader, Repository> classLoaderRepositoryMap = new ConcurrentReferenceHashMap<ClassLoader, Repository>(16, 0.95f, Runtime.getRuntime().availableProcessors(), ReferenceType.SOFT, ReferenceType.STRONG);

    public static Repository getClassLoaderRepository(Class clazz) {
        Preconditions.checkNotNull(clazz);
        ClassLoader classLoader = clazz.getClassLoader();
        // bootstrap class loader
        if (classLoader == null) {
            return bootstrapRepository;
        }
        Repository repository = classLoaderRepositoryMap.get(classLoader);
        if (repository == null) {
            repository = new ClassLoaderRepository(classLoader);
            classLoaderRepositoryMap.putIfAbsent(classLoader, repository);
        }

        Repository repository1 = classLoaderRepositoryMap.get(classLoader);
        if (repository1 == null) {
            return repository;
        }
        return repository1;
    }

    public static SyntheticRepository getSyntheticRepository(Class clazz) {
        Preconditions.checkNotNull(clazz);
        URL codeBase = Reflects.getCodeLocation(clazz);
        if (codeBase != null) {
            try {
                File classpathRoot = new File(codeBase.toURI());

                ClassPath classPath = new ClassPath(classpathRoot.getAbsolutePath());
                return SyntheticRepository.getInstance(classPath);
            } catch (URISyntaxException ex) {
                //
            }
        }
        return null;
    }

    public static JavaClass loadJavaClass(@NonNull Repository repository, @NonNull String classname) throws ClassNotFoundException {
        Preconditions.checkNotNull(repository);
        Preconditions.checkNotNull(classname);

        JavaClass classAj = repository.findClass(classname);
        if (classAj == null) {
            classAj = repository.loadClass(classname);
            JavaClass classAj1 = repository.findClass(classname);
            if (classAj1 == null) {
                repository.storeClass(classAj);
            } else {
                classAj = classAj1;
            }
        }
        return classAj;
    }

    public static JavaClass loadJavaClass(@NonNull Repository repository, @NonNull Class clazz) throws ClassNotFoundException {
        return loadJavaClass(repository, Reflects.getFQNClassName(clazz));
    }
}
