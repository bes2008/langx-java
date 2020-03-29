package com.jn.langx.expression.operator;


import com.jn.langx.expression.Expression;

public abstract class AbstractBinaryOperator<Left extends Expression, Right extends Expression, Result extends Expression> implements BinaryOperator<Left, Right, Result> {
    protected Left left;
    protected Right right;

    @Override
    public void setLeft(Left left) {
        this.left = left;
    }

    @Override
    public void setRight(Right right) {
        this.right = right;
    }

    @Override
    public Left getLeft() {
        return this.left;
    }

    @Override
    public Right getRight() {
        return this.right;
    }
}
