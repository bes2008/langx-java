package com.jn.langx.util.boundary;

import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Predicate;

public class MathExpressionBoundary extends ExpressionBoundary {
    private Predicate<String> low = Functions.truePredicate();
    private Predicate<String> high = Functions.truePredicate();

    public MathExpressionBoundary() {
    }

    public MathExpressionBoundary(String expression) {
        setExpression(expression);
    }

    @Override
    public boolean test(String value) {
        Preconditions.checkState(low != null && high != null);
        return low.test(value) && high.test(value);
    }

    @Override
    public void setExpression(String expression) {
        if ((expression.startsWith("[") || expression.startsWith("(")) && (expression.endsWith("]") || expression.endsWith(")"))) {
            this.expression = expression;

            String lowAndHighString = expression.substring(1, expression.length() - 1).trim();
            String[] lowAndHigh = Strings.split(lowAndHighString, ",");
            if (lowAndHigh.length > 2 || lowAndHigh.length == 0) {
                throw new IllegalArgumentException("illegal expression: " + expression);
            }

            String low = null;
            String high = null;
            if (lowAndHigh.length == 2) {
                low = lowAndHigh[0];
                high = lowAndHigh[1];
            } else {
                if (lowAndHighString.startsWith(",")) {
                    high = lowAndHigh[0];
                } else {
                    low = lowAndHigh[0];
                }
            }

            if (low != null) {
                setLow(buildLow(low, expression.startsWith("[")));
            }
            if (high != null) {
                setHigh(buildHigh(high, expression.endsWith("]")));
            }
        }
    }

    public void setLow(Predicate<String> low) {
        if (low != null) {
            this.low = low;
        }
    }

    public void setHigh(Predicate<String> high) {
        if (high != null) {
            this.high = high;
        }
    }

    private Predicate<String> buildLow(final String low, final boolean judgeEquals) {
        return new Predicate<String>() {
            @Override
            public boolean test(String value) {
                if (judgeEquals) {
                    return low.compareTo(value) <= 0;
                } else {
                    return low.compareTo(value) < 0;
                }
            }
        };
    }

    private Predicate<String> buildHigh(final String high, final boolean judgeEquals) {
        return new Predicate<String>() {
            @Override
            public boolean test(String value) {
                if (judgeEquals) {
                    return high.compareTo(value) >= 0;
                } else {
                    return high.compareTo(value) > 0;
                }
            }
        };
    }
}
