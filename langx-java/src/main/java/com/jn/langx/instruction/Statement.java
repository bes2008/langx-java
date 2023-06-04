package com.jn.langx.instruction;

abstract class Statement<R> implements Instruction<R> {
    private Closure closure;
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
}
