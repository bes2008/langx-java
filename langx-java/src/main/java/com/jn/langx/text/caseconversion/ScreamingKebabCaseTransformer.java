package com.jn.langx.text.caseconversion;

public class ScreamingKebabCaseTransformer extends AbstractTokenCaseTransformer{
    public ScreamingKebabCaseTransformer() {
        super("-", LetterCase.UPPER, LetterCase.NOOP, LetterCase.NOOP);
    }
}
