package com.jn.langx.instruction;

import com.jn.langx.annotation.NonNull;

abstract class Statement<R> implements Instruction<R> {
    @NonNull
    private Closure closure;
    @NonNull
    private Object owner;
    @NonNull
    private Action<R> action;

    @Override
    public Closure getClosure() {
        return closure;
    }

    @Override
    public void setClosure(Closure closure) {
        this.closure = closure;
    }

    public Action<R> getAction() {
        return action;
    }

    public void setAction(Action<R> action) {
        this.action = action;
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }
}
