package com.jn.langx.expression.operator.arithmetic;

import com.jn.langx.expression.Expression;

public interface Add<Left extends Expression, Right extends Expression, Result extends Expression> extends ArithmeticOperator<Left, Right, Result> {
}
