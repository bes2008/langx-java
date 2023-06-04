package com.jn.langx.instruction;

public interface Action<R> {
    <R> R doAction(Object owner, Object functionOrVariableName, Closure closure);
}
