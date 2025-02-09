package com.jn.langx.text.transform.caseconversion;

public class ScreamingSnakeCaseTransformer extends AbstractTokenCaseTransformer{
    public ScreamingSnakeCaseTransformer() {
        super("_", LetterCase.UPPER, LetterCase.NOOP, LetterCase.NOOP);
    }
}
