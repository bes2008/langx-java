package com.jn.langx.util.reflect.annotation;

import com.jn.langx.annotation.OnClasses;
import com.jn.langx.util.ClassLoaders;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;

public class OnClassesConditions {
    private static final Logger logger = Loggers.getLogger(OnClassesConditions.class);
    private OnClassesConditions(){

    }
    public static boolean anyPresent(final Class klass, boolean defaultValueIfMissOnClassesAnnotation) {
        OnClasses annotation = Reflects.getAnnotation(klass, OnClasses.class);
        if (annotation != null) {
            String[] classes = annotation.value();
            return Pipeline.of(classes).anyMatch(new Predicate<String>() {
                @Override
                public boolean test(String className) {
                    boolean hasClass = ClassLoaders.hasClass(className, klass.getClassLoader());
                    if (!hasClass) {
                        logger.warn("Class {} not found", className);
                    }
                    return hasClass;
                }
            });
        }
        return defaultValueIfMissOnClassesAnnotation;
    }

    public static boolean allPresent(final Class klass, boolean defaultValueIfMissOnClassesAnnotation) {
        OnClasses annotation = Reflects.getAnnotation(klass, OnClasses.class);
        if (annotation != null) {
            String[] classes = annotation.value();
            return Pipeline.of(classes).allMatch(new Predicate<String>() {
                @Override
                public boolean test(String className) {
                    boolean hasClass = ClassLoaders.hasClass(className, klass.getClassLoader());
                    if (!hasClass) {
                        logger.warn("Class {} not found", className);
                    }
                    return hasClass;
                }
            });
        }
        return defaultValueIfMissOnClassesAnnotation;
    }
}
