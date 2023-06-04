package com.jn.langx.instruction;

public class GetVariableAction<R> implements Action<R> {

    @Override
    public R doAction(Object owner, String functionOrVariableName, Closure closure) {
        String variableName = functionOrVariableName;
        if (closure.hasVariable(variableName)) {
            return (R) closure.getVariable(variableName);
        }


        return null;
    }
}
