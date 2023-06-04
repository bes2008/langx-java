package com.jn.langx.instruction;

public class GetVariableStatement<R> extends Statement<R> {

    /**
     * 闭包中的变量，或者对象的字段
     */
    private String variableName;


    public Object getOwner() {
        return getClosure().getOwner();
    }

    public void setOwner(Object owner) {
        getClosure().setOwner(owner);
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public <R1> R1 execute() {
        return getAction().doAction(getOwner(), variableName, getClosure());
    }
}
