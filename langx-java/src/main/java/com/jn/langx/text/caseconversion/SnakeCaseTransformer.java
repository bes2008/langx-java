package com.jn.langx.text.caseconversion;

public class SnakeCaseTransformer extends AbstractTokenCaseTransformer {
    public SnakeCaseTransformer() {
        super("_", LetterCase.LOWER, LetterCase.NOOP, LetterCase.NOOP);
    }
}
