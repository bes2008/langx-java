package com.jn.langx.instruction;

public interface Action<R> {
    <R> R doAction(Statement statement);
}
