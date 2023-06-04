package com.jn.langx.instruction;

import com.jn.langx.annotation.Nullable;

public class SetVariableStatement<R> extends Statement<R> {
    @Nullable
    private Object owner;
    private String variableName;


    public Object getOwner() {
        return owner;
    }

    public void setOwner(Object owner) {
        this.owner = owner;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public <R1> R1 execute() {
        return getAction().doAction(owner, variableName, getClosure());
    }
}
