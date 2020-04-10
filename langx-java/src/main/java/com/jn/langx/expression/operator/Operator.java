package com.jn.langx.expression.operator;


import com.jn.langx.expression.Expression;

public interface Operator<R> extends Expression<R> {
    void setOperateSymbol(String symbol);
    String getOperateSymbol();
}
