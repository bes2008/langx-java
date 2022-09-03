package com.jn.langx.invocation.matcher;

import com.jn.langx.Matcher;
import com.jn.langx.invocation.MethodInvocation;

public interface MethodInvocationMatcher extends Matcher<MethodInvocation, Boolean> {
    @Override
    Boolean matches(MethodInvocation methodInvocation);
}
