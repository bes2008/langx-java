package com.jn.langx.text.transform.caseconversion;

public class DotCaseTransformer extends AbstractTokenCaseTransformer{
    public DotCaseTransformer() {
        super(".", LetterCase.LOWER, LetterCase.NOOP, LetterCase.NOOP);
    }
}
