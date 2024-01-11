package com.jn.langx.el.expression.value;


import com.jn.langx.el.expression.BaseExpression;
import com.jn.langx.el.expression.Expression;
import com.jn.langx.util.Objs;

public class ValueExpression<E> extends BaseExpression<E> implements Expression<E> {
    private E value;

    public ValueExpression() {
    }

    public ValueExpression(E value) {
        setValue(value);
    }

    public void setValue(E value) {
        this.value = value;
    }

    public E getValue(){
        return this.value;
    }

    @Override
    public E execute() {
        return value;
    }

    @Override
    public String toString() {
        return value == null ? "" : value.toString();
    }

    @Override
    public int hashCode() {
        return Objs.hash(value);
    }

    public boolean equals(Object obj){
        if(!(obj instanceof ValueExpression)){
            return false;
        }
        if(this.getClass()!=obj.getClass()){
            return false;
        }
        Object that = ((ValueExpression<?>) obj).value;
        return Objs.equals(this.value, that);
    }

}
