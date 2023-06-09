package com.jn.langx.util.io.file.validator;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;

public class UnixFilepathValidator extends AbstractFilepathValidator {
    private Character[] illegalChars = {'\0', '\'', '"', ':', '?', '*', '<', '>', '|'};

    @Override
    public boolean isLegalFilename(String name) {
        if (Strings.startsWith(name, " ") || Strings.startsWith(name, "\t")) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Collects.contains(illegalChars, c)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isLegalFilepath(String path) {
        if(Strings.isEmpty(path)){
            return false;
        }
        String[] segments = Strings.split(path, "/", false, false);
        boolean matched = Collects.<String>allMatch(new Predicate<String>() {
            @Override
            public boolean test(String segment) {
                return isLegalFilename(segment);
            }
        }, segments);
        return matched;
    }

    static final UnixFilepathValidator INSTANCE = new UnixFilepathValidator();
}
