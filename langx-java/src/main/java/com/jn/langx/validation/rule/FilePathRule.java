package com.jn.langx.validation.rule;

import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.file.validator.FilepathValidators;

public class FilePathRule extends PredicateRule {
    public FilePathRule(String errorMessage, final boolean windows) {
        super(errorMessage, new Predicate<String>() {
            public boolean test(String value) {
                return FilepathValidators.validatePath(value, windows);
            }
        });
    }
}
