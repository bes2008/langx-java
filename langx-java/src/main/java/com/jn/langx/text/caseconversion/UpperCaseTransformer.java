package com.jn.langx.text.caseconversion;

import com.jn.langx.util.Strings;

public class UpperCaseTransformer implements CaseTransformer{
    @Override
    public String transform(String text) {
        return Strings.upperCase(text);
    }
}
