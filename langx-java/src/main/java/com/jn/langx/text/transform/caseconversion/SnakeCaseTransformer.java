package com.jn.langx.text.transform.caseconversion;

import com.jn.langx.text.transform.LetterCase;

public class SnakeCaseTransformer extends AbstractTokenCaseTransformer {
    public SnakeCaseTransformer() {
        super("_", LetterCase.LOWER, LetterCase.NOOP, LetterCase.NOOP);
    }
}
