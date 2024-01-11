package com.jn.langx.el.expression.operator;


import com.jn.langx.el.expression.Expression;

public interface Operator<R> extends Expression<R> {
    void setOperateSymbol(String symbol);
    String getOperateSymbol();

    @Override
    R execute();
}
