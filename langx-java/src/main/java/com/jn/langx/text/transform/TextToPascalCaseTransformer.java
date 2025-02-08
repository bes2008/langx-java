package com.jn.langx.text.transform;

public class TextToPascalCaseTransformer extends AbstractTokenTextCaseTransformer {
    public TextToPascalCaseTransformer() {
        super("",LetterCase.LOWER, LetterCase.UPPER, LetterCase.NOOP);
    }
}
