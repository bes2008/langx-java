package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;

public class UrlRule extends PredicateRule {

    //  <scheme>://<authority><path>?<query>#<fragment>
    private static SegmentsPredicate newSegmentsPredicate(String... schemes){
        SegmentMetadata schemesMetadata = new SegmentMetadata(true, "[a-z][0-9a-z]+", new InRule(null,schemes));
        schemesMetadata.setName("scheme");
        return new SegmentsPredicateBuilder()
                .addSegment(null, schemesMetadata)
                .addString("://")
                .addSegment(null,"host",true, "\\[.*\\]|[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*", new HostnameRule(null))
                .addSegment(":","port",false, "\\d{1,5}", new PortRangeRule())
                .addSegment("/","others",false, "[\\w-./?%&=]*")
                .build();
    }
    public UrlRule(String errorMessage, String... schemes) {
        super(newSegmentsPredicate(schemes), Objs.useValueIfEmpty(errorMessage, "url is invalid"));
    }


    private static String getPattern(String... schemes) {
        String schemaPattern = "[a-z][0-9a-z]+";
        if (Objs.isNotEmpty(schemes)) {
            schemaPattern = Strings.join("|", schemes);
        }
        String regex = "^("+schemaPattern+")://"    // 协议部分
                + "([a-zA-Z0-9-]+\\.)+"             // 主域名（含子域名）
                + "[a-zA-Z]{2,6}"                   // 顶级域名（2-6位字母）
                + "(:\\d{1,5})?"                    // 端口号（可选）
                + "(/[\\w-./?%&=]*)?$";             // path & query string（可选）
        return regex;
    }
}

