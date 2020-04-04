package com.jn.langx.expression.operator.logic;


import com.jn.langx.expression.BooleanResultExpression;
import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.AbstractBinaryOperator;
import com.jn.langx.expression.value.BooleanExpression;
import com.jn.langx.util.Strings;

public class And<E extends Expression<BooleanResultExpression>, F extends Expression<BooleanResultExpression>> extends AbstractBinaryOperator<E, F, BooleanResultExpression> implements BinaryLogicOperator<E, F> {
    public And() {
        setOperateSymbol("&&");
    }

    public And(E left, F right) {
        this.setLeft(left);
        this.setRight(right);
    }

    public And(String operateSymbol, E left, F right) {
        this(left, right);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public String getOperateSymbol() {
        return Strings.isEmpty(operateSymbol) ? "&&" : operateSymbol;
    }

    @Override
    public BooleanResultExpression execute() {
        return new BooleanExpression(getLeft().execute().execute() && getRight().execute().execute());
    }

}
