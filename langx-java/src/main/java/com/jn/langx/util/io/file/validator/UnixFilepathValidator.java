package com.jn.langx.util.io.file.validator;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.file.OsFileSystem;

public class UnixFilepathValidator extends AbstractFilepathValidator {

    @Override
    public boolean isLegalFilename(String name) {
        return OsFileSystem.LINUX.isLegalFileName(name);
    }

    @Override
    public boolean isLegalFilepath(String path) {
        if (Strings.isEmpty(path)) {
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
