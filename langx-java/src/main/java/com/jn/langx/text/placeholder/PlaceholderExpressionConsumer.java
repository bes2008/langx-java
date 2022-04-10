package com.jn.langx.text.placeholder;

import com.jn.langx.util.function.Consumer3;
import com.jn.langx.util.struct.Holder;

public interface PlaceholderExpressionConsumer extends Consumer3<String, String, Holder<String>> {
    @Override
    void accept(String variable, String expression, Holder<String> variableValueHolder);
}
