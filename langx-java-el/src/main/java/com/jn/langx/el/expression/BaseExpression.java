package com.jn.langx.el.expression;

import com.jn.langx.util.reflect.Reflects;

public abstract class BaseExpression<Result> implements Expression<Result> {

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (Reflects.isSubClassOrEquals(getClass(), obj.getClass())) {
            return toString().equals(obj.toString());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
