package com.jn.langx.util;

public class NamedInheritableThreadLocal<T> extends InheritableThreadLocal<T> {

    private final String name;


    /**
     * Create a new NamedInheritableThreadLocal with the given name.
     *
     * @param name a descriptive name for this ThreadLocal
     */
    public NamedInheritableThreadLocal(String name) {
        Preconditions.checkNotNull(name, "Name must not be empty");
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
