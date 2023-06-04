package com.jn.langx.instruction;

public final class InvocationStatement<R> extends Statement<R> {
    /**
     * 函数名称，或者对象的方法名
     */
    private String functionName;

    /**
     * 函数是在哪个对象上执行的
     * a.b(); a 就是 owner
     */

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public Object getOwner() {
        return getClosure().getOwner();
    }

    public void setOwner(Object owner) {
        getClosure().setOwner(owner);
    }

    @Override
    public <R1> R1 execute() {
        return getAction().doAction(getOwner(), functionName, getClosure());
    }
}
