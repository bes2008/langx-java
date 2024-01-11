package com.jn.langx.el.expression.value;

import com.jn.langx.el.expression.Expression;

public interface StringResultExpression extends Expression<String> {
    @Override
    String execute();
}
