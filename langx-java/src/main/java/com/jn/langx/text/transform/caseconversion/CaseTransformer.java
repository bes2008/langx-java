package com.jn.langx.text.transform.caseconversion;

import com.jn.langx.Transformer;

public interface CaseTransformer extends Transformer<String, String> {
    @Override
    String transform(String text);
}
