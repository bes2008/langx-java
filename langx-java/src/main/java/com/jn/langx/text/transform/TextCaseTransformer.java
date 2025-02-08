package com.jn.langx.text.transform;

import com.jn.langx.Transformer;

public interface TextCaseTransformer extends Transformer<String, String> {
    @Override
    String transform(String text);
}
