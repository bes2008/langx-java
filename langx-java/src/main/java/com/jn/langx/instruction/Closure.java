package com.jn.langx.instruction;

import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Maps;
import com.jn.langx.util.collection.Pipeline;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 闭包, 是指令（语句、表达式）执行的上下文
 */
public class Closure {
    /**
     * 闭包的参数
     */
    private LinkedHashMap<String, Object> arguments;
    /**
     * 在闭包中声明的变量
     */
    private Map<String, Object> localVariables;

    public LinkedHashMap<String, Object> getArguments() {
        return arguments;
    }

    public void setArguments(LinkedHashMap<String, Object> arguments) {
        for (Map.Entry<String, Object> arg: arguments.entrySet()) {
            addArgument(arg.getKey(), arg.getValue());
        }
    }

    public void addArgument(@NotEmpty String argumentName, Object value) {
        Preconditions.checkNotEmpty(argumentName, "argumentName is empty");
        if (this.arguments == null) {
            this.arguments = Maps.newLinkedHashMap();
        }
        this.arguments.put(argumentName, value);
    }

    public void setArgument(int index, Object value) {
        String argumentName = getArgumentName(index);
        if (Strings.isNotEmpty(argumentName)) {
            addArgument(argumentName, value);
        }
    }

    public String getArgumentName(int index) {
        if (index < 0 || index >= Objs.length(arguments)) {
            return null;
        }
        return Pipeline.of(arguments.keySet()).asList().get(index);
    }


    public Map<String, Object> getLocalVariables() {
        return localVariables;
    }

    public void setLocalVariables(Map<String, Object> localVariables) {
        for (Map.Entry<String, Object> arg: localVariables.entrySet()) {
            addLocalVariable(arg.getKey(), arg.getValue());
        }
    }

    public void addLocalVariable(String varName, Object value) {
        Preconditions.checkNotEmpty(varName, "varName is empty");
        if(arguments.containsKey(varName)){
            throw new IllegalArgumentException("variable exists in arguments");
        }
        this.localVariables.put(varName, value);
    }


}
