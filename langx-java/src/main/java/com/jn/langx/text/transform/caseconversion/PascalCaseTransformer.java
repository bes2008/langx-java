package com.jn.langx.text.transform.caseconversion;

import com.jn.langx.text.transform.LetterCase;

public class PascalCaseTransformer extends AbstractTokenCaseTransformer {
    public PascalCaseTransformer() {
        super("", LetterCase.LOWER, LetterCase.UPPER, LetterCase.NOOP);
    }
}
