package com.jn.langx.el.expression.value;


import com.jn.langx.el.expression.Expression;

public interface BooleanResultExpression extends Expression<Boolean> {
    @Override
    Boolean execute();
}
