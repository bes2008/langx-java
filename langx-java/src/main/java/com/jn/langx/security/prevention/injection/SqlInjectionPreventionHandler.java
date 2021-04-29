package com.jn.langx.security.prevention.injection;

import com.jn.langx.util.collection.Collects;

import java.util.List;

/**
 * 目前这个做法，太过暴力，不适合将其运用到所有的参数上。
 */
public class SqlInjectionPreventionHandler extends InjectionPreventionHandler {
    private final List<String> DEFAULT_REMOVED_SYMBOLS = Collects.asList(
            "--", "/*", "*/", "waitfor delay",
            "#", "|", "&", ";", "$", "%", "@", "'", "\"", "<", ">", "(", ")", "+", "\t", "\r", "\f", ",", "\\"

    );

    @Override
    public List<String> getBlacklist() {
        List<String> blacklist = super.getBlacklist();
        return blacklist == null ? DEFAULT_REMOVED_SYMBOLS : blacklist;
    }
}
