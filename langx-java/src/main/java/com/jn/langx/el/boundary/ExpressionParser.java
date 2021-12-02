package com.jn.langx.el.boundary;

import com.jn.langx.Parser;

public interface ExpressionParser extends Parser<String, CommonExpressionBoundary> {
    @Override
    CommonExpressionBoundary parse(String expression);
}
