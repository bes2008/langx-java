package com.jn.langx.el.expression.value;

import com.jn.langx.el.expression.Expression;

public interface NumberResultExpression<N extends Number> extends Expression<N> {
    @Override
    N execute();
}
