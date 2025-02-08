package com.jn.langx.text.transform;

public class TextToSnakeCaseTransformer extends AbstractTokenTextCaseTransformer {
    public TextToSnakeCaseTransformer() {
        super("_", LetterCase.LOWER, LetterCase.NOOP, LetterCase.NOOP);
    }
}
