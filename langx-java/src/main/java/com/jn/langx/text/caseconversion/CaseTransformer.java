package com.jn.langx.text.caseconversion;

import com.jn.langx.Transformer;

/**
 * @since 5.4.7
 */
public interface CaseTransformer extends Transformer<String, String> {
    @Override
    String transform(String text);
}
