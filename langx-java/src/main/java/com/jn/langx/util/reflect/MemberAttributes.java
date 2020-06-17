package com.jn.langx.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.util.Collection;

public interface MemberAttributes<M extends Member> {
    String getName();
    <T extends Annotation> T getAnnotation(Class<T> annotation);
    Collection<Annotation> getAnnotations();
    boolean hasModifier(int modifier);
    Class getDeclaringClass();
    int getModifier();
    M get();
}
