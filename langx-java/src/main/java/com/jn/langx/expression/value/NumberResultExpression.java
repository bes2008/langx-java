package com.jn.langx.expression.value;

import com.jn.langx.expression.Expression;

public interface NumberResultExpression<N extends Number> extends Expression<N> {
    @Override
    N execute();
}
