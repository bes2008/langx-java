package com.jn.langx.el.expression.operator;


import com.jn.langx.el.expression.Expression;

public interface BinaryOperator<Left extends Expression, Right extends Expression, Result> extends Operator<Result> {
    void setLeft(Left left);
    void setRight(Right right);
    Left getLeft();
    Right getRight();

    @Override
    Result execute();
}
