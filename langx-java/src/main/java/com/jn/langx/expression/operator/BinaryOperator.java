package com.jn.langx.expression.operator;


import com.jn.langx.expression.Expression;

public interface BinaryOperator<Left extends Expression, Right extends Expression, Result> extends Operator<Result> {
    void setLeft(Left left);
    void setRight(Right right);
    Left getLeft();
    Right getRight();

    @Override
    Result execute();
}
