package com.jn.langx.instruction;

import com.jn.langx.annotation.Nullable;

public final class InvocationStatement<R> extends Statement<R> {
    /**
     * 函数名称
     */
    private String functionName;

    /**
     * 函数是在哪个对象上执行的
     * a.b(); a 就是 owner
     */
    @Nullable
    private Object owner;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    @Override
    public <R1> R1 execute() {
        return getAction().doAction(owner, functionName, getClosure());
    }
}
