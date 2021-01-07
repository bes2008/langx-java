package com.jn.langx.invocation.aop.expression;

import com.jn.langx.annotation.NonNull;

public class AspectJExpressionPointcutAdvisorProperties {
    @NonNull
    private String expression;
    private int order;

    public String getExpression() {
        return this.expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int getOrder() {
        return this.order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
