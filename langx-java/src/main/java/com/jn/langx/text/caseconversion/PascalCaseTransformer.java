package com.jn.langx.text.caseconversion;

public class PascalCaseTransformer extends AbstractTokenCaseTransformer {
    public PascalCaseTransformer() {
        super("", LetterCase.LOWER, LetterCase.UPPER, LetterCase.NOOP);
    }
}
