package com.jn.langx.instruction;

public class GetVariableAction<R> implements Action<R> {

    @Override
    public R doAction(Statement statement) {
        GetVariableStatement stmt = (GetVariableStatement) statement;

        String variableName = stmt.getVariableName();
        Closure closure = stmt.getClosure();
        if (closure.hasVariable(variableName)) {
            return (R) closure.getVariable(variableName);
        }
        return null;
    }
}
