package com.jn.langx.util.boundary;

import com.jn.langx.Parser;

public interface ExpressionParser extends Parser<String, CommonExpressionBoundary> {
    @Override
    CommonExpressionBoundary parse(String expression);
}
