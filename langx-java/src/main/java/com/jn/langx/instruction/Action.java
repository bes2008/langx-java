package com.jn.langx.instruction;

public interface Action<R> {
    <R> R doAction(Object owner, String functionOrVariableName, Closure closure);
}
