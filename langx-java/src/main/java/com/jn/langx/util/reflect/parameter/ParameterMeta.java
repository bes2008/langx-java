package com.jn.langx.util.reflect.parameter;

import com.jn.langx.util.Preconditions;

import java.lang.reflect.Method;

public class ParameterMeta {
    private Object executable;
    private int index;
    private int modifiers;
    private String name;

    public ParameterMeta(String name, int modifiers, Object executable, int index) {
        this.name = name;
        this.modifiers = modifiers;
        Preconditions.checkArgument(executable instanceof Method || executable instanceof Class);
        this.executable = executable;
        this.index = index;
    }

    public Object getExecutable() {
        return executable;
    }

    public void setExecutable(Object executable) {
        this.executable = executable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifiers) {
        this.modifiers = modifiers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
