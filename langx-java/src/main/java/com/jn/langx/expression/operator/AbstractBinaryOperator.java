package com.jn.langx.expression.operator;


import com.jn.langx.expression.Expression;

public abstract class AbstractBinaryOperator<Left extends Expression, Right extends Expression, Result extends Expression> implements BinaryOperator<Left, Right, Result> {
    private Left left;
    private Right right;
    private String operateSymbol;

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

    @Override
    public void setOperateSymbol(String operateSymbol) {
        this.operateSymbol = operateSymbol;
    }

    @Override
    public String getOperateSymbol() {
        return this.operateSymbol;
    }

    @Override
    public String toString() {
        return left.toString() + " " + getOperateSymbol() + " " + right.toString();
    }
}
