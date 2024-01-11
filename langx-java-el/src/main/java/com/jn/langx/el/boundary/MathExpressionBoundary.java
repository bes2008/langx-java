package com.jn.langx.el.boundary;

import com.jn.langx.util.Strings;
import com.jn.langx.util.function.Predicate;

public class MathExpressionBoundary extends CommonExpressionBoundary {

    public MathExpressionBoundary() {
    }

    public MathExpressionBoundary(String expression) {
        setExpression(expression);
    }

    @Override
    public void setExpression(String expression) {
        if ((expression.startsWith("[") || expression.startsWith("(")) && (expression.endsWith("]") || expression.endsWith(")"))) {
            super.setExpression(expression);
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
                addPredicate(buildLow(low, expression.startsWith("[")));
            }
            if (high != null) {
                addPredicate(buildHigh(high, expression.endsWith("]")));
            }
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
