package com.jn.langx.expression.operator.logic;


import com.jn.langx.expression.value.BooleanResultExpression;
import com.jn.langx.expression.Expression;
import com.jn.langx.expression.operator.AbstractUnaryOperator;
import com.jn.langx.expression.value.BooleanExpression;
import com.jn.langx.util.Strings;

public class Non<E extends Expression<BooleanResultExpression>> extends AbstractUnaryOperator<E, BooleanResultExpression> implements UnaryLogicOperator<E> {

    public Non() {
        setOperateSymbol("!");
    }

    public Non(E target) {
        setTarget(target);
    }

    public Non(String operateSymbol, E target) {
        this(target);
        setOperateSymbol(operateSymbol);
    }

    @Override
    public String getOperateSymbol() {
        return Strings.isEmpty(operateSymbol) ? "!" : operateSymbol;
    }

    @Override
    public String toString() {
        return getOperateSymbol() + " " + getTarget().toString();
    }

    @Override
    public BooleanResultExpression execute() {
        return new BooleanExpression(!this.getTarget().execute().execute());
    }
}
