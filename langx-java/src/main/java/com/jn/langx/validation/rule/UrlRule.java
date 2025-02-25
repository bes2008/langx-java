package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;

public class UrlRule extends RegexpRule {
    public UrlRule(String errorMessage, String... schemas) {
        super(Objs.useValueIfEmpty(errorMessage, "url is invalid"), getPattern(schemas));
    }

    public static String getPattern(String... schemas) {
        String schemaPattern = "[a-z][0-9a-z]+";
        if (Objs.isNotEmpty(schemas)) {
            schemaPattern = Strings.join("|", schemas);
        }
        String regex = "^("+schemaPattern+")://"    // 协议部分
                + "([a-zA-Z0-9-]+\\.)+"             // 主域名（含子域名）
                + "[a-zA-Z]{2,6}"                   // 顶级域名（2-6位字母）
                + "(:\\d{1,5})?"                    // 端口号（可选）
                + "(/[\\w-./?%&=]*)?$";             // query string（可选）
        return regex;
    }
}

