package com.jn.langx.util.reflect;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author jinuo.fang
 */
public class Modifiers {
    private static final Set<Integer> supportedModifiers = new HashSet<Integer>(Collects.asList(new Integer[]{
            Modifier.ABSTRACT,
            Modifier.FINAL,
            Modifier.INTERFACE,
            Modifier.NATIVE,
            Modifier.PUBLIC,
            Modifier.PRIVATE,
            Modifier.PROTECTED,
            Modifier.STATIC,
            Modifier.SYNCHRONIZED,
            Modifier.STRICT,
            Modifier.TRANSIENT,
            Modifier.VOLATILE
    }));

    static {
        List<Field> fields = Reflects.getAllPublicFields(Modifier.class, true);
        fields = Collects.filter(fields, new Predicate<Field>() {
            @Override
            public boolean test(Field field) {
                return Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers());
            }
        });
        List<Integer> supportedModifiersInModifierClass = Collects.map(fields, new Function<Field, Integer>() {
            @Override
            public Integer apply(Field field) {
                try {
                    return (Integer) Reflects.getFieldValue(field, Modifier.class, true, false);
                } catch (Throwable ex) {
                    return Modifier.PUBLIC;
                }
            }
        });
        supportedModifiers.addAll(supportedModifiersInModifierClass);
    }
    public static boolean isSupportedModifier(int modifier){
        return supportedModifiers.contains(modifier);
    }


    public static int modifiers(Member member) {
        return member.getModifiers();
    }


    public static boolean hasModifier(int modifiers, int modifier) {
        if (!isSupportedModifier(modifier)) {
            throw new IllegalArgumentException(StringTemplates.formatWithoutIndex("Unsupported modifier: {}", modifier));
        }
        return (modifiers & modifier) != 0;
    }

    public static boolean hasModifier(Class clazz, int modifier) {
        return hasModifier(clazz.getModifiers(), modifier);
    }

    public static boolean hasModifier(Member member, int modifier) {
        return hasModifier(modifiers(member), modifier);
    }

    public static boolean isAbstract(Class clazz) {
        return hasModifier(clazz, Modifier.ABSTRACT);
    }

    public static boolean isAbstract(Member member) {
        return hasModifier(member, Modifier.ABSTRACT);
    }

    public static boolean isFinal(Class clazz) {
        return hasModifier(clazz, Modifier.FINAL);
    }

    public static boolean isFinal(Member member) {
        return hasModifier(member, Modifier.FINAL);
    }

    public static boolean isInterface(Class clazz) {
        return clazz.isInterface();
    }

    public static boolean isInterface(Member member) {
        return hasModifier(member, Modifier.INTERFACE);
    }

    public static boolean isNative(Class clazz) {
        return hasModifier(clazz, Modifier.NATIVE);
    }

    public static boolean isNative(Member member) {
        return hasModifier(member, Modifier.NATIVE);
    }

    public static boolean isPublic(Class clazz) {
        return hasModifier(clazz, Modifier.PUBLIC);
    }

    public static boolean isPublic(Member member) {
        return hasModifier(member, Modifier.PUBLIC);
    }

    public static boolean isPrivate(Class clazz) {
        return hasModifier(clazz, Modifier.PRIVATE);
    }

    public static boolean isPrivate(Member member) {
        return hasModifier(member, Modifier.PRIVATE);
    }

    public static boolean isProtected(Class clazz) {
        return hasModifier(clazz, Modifier.PROTECTED);
    }

    public static boolean isProtected(Member member) {
        return hasModifier(member, Modifier.PROTECTED);
    }

    public static boolean isStatic(Class clazz) {
        return hasModifier(clazz, Modifier.STATIC);
    }

    public static boolean isStatic(Member member) {
        return hasModifier(member, Modifier.STATIC);
    }

    public static boolean isSynchronized(Member member) {
        return hasModifier(member, Modifier.SYNCHRONIZED);
    }

    public static boolean isStrict(Member member) {
        return hasModifier(member, Modifier.STRICT);
    }

    public static boolean isTransient(Member member) {
        return hasModifier(member, Modifier.TRANSIENT);
    }

    public static boolean isVolatile(Member member) {
        return hasModifier(member, Modifier.VOLATILE);
    }


}
