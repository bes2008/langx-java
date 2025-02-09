package com.jn.langx.text.transform.caseconversion;

import com.jn.langx.util.Strings;

public class LowerCaseTransformer implements CaseTransformer{
    @Override
    public String transform(String text) {
        return Strings.lowerCase(text);
    }
}
