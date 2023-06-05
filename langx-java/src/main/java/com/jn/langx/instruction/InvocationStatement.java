package com.jn.langx.instruction;

import java.util.List;

public final class InvocationStatement<R> extends Statement<R> {
    /**
     * 函数名称，或者对象的方法名
     */
    private String functionName;

    private List<String> argumentNames;

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

    @Override
    public <R1> R1 execute() {
        return getAction().doAction(getOwner(), functionName, getClosure());
    }

    public List<String> getArgumentNames() {
        return argumentNames;
    }

    public void setArgumentNames(List<String> argumentNames) {
        this.argumentNames = argumentNames;
    }
}
