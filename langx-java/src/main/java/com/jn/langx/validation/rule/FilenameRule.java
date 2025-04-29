package com.jn.langx.validation.rule;

import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.file.validator.FilepathValidators;

public class FilenameRule extends PredicateRule {

    public FilenameRule(String errorMessage, final boolean windows) {
        super(errorMessage, new Predicate<String>() {
            @Override
            public boolean test(String filename) {
                return FilepathValidators.validateName(filename, windows);
            }
        });
    }
}
