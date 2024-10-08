package com.jn.langx.el.expression.operator;


import com.jn.langx.el.expression.BaseExpression;
import com.jn.langx.el.expression.Expression;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.hash.HashCodeBuilder;

@SuppressWarnings("ALL")
public abstract class AbstractBinaryOperator<Left extends Expression, Right extends Expression, Result> extends BaseExpression<Result> implements BinaryOperator<Left, Right, Result> {
    private Left left;
    private Right right;
    protected String operateSymbol;

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
        if (Strings.isNotEmpty(operateSymbol)) {
            this.operateSymbol = operateSymbol;
        }
    }

    @Override
    public String getOperateSymbol() {
        return this.operateSymbol;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().with(operateSymbol).with(left).with(right).build();
    }

    @Override
    public String toString() {
        return left.toString() + " " + getOperateSymbol() + " " + right.toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        if (!super.equals(object)){
            return false;
        }

        AbstractBinaryOperator<?, ?, ?> that = (AbstractBinaryOperator<?, ?, ?>) object;

        return Objs.equals(this.toString(), that.toString());
    }
}
