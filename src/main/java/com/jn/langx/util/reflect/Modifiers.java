package com.jn.langx.util.reflect;

import com.jn.langx.text.StringTemplates;

import java.lang.reflect.Member;
import java.lang.reflect.Modifier;

/**
 * @author jinuo.fang
 */
public class Modifiers {
    public static int modifiers(Member member) {
        return member.getModifiers();
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>public</tt> modifier, <tt>false</tt> otherwise.
     * <p>
     * <tt>public</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isPublic(Member member) {
        return Modifier.isPublic(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>private</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>private</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isPrivate(Member member) {
        return Modifier.isPrivate(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>protected</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>protected</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isProtected(Member member) {
        return Modifier.isProtected(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>static</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>static</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isStatic(Member member) {
        return Modifier.isStatic(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>final</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>final</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isFinal(Member member) {
        return Modifier.isFinal(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>synchronized</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>synchronized</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isSynchronized(Member member) {
        return Modifier.isSynchronized(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>volatile</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>volatile</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isVolatile(Member member) {
        return Modifier.isVolatile(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>transient</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>transient</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isTransient(Member member) {
        return Modifier.isTransient(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>native</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>native</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isNative(Member member) {
        return Modifier.isNative(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>interface</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>interface</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isInterface(Member member) {
        return Modifier.isInterface(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>abstract</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>abstract</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isAbstract(Member member) {
        return Modifier.isAbstract(modifiers(member));
    }

    /**
     * Return <tt>true</tt> if the integer argument includes the
     * <tt>strictfp</tt> modifier, <tt>false</tt> otherwise.
     *
     * @return <tt>true</tt> if <code>mod</code> includes the
     * <tt>strictfp</tt> modifier; <tt>false</tt> otherwise.
     */
    public static boolean isStrict(Member member) {
        return Modifier.isStrict(modifiers(member));
    }

    public static boolean contains(int modifiers, int modifier){
        if(modifier<=0){
            throw new IllegalArgumentException(StringTemplates.formatWithoutIndex("Illegal modifier: {}", modifier));
        }
        return (modifiers & modifier) != 0;
    }

    public static boolean contains(Member member, int modifier){
        return contains(modifiers(member), modifier);
    }
}
