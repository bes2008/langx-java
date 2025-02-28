package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;

public class UrlRule extends PredicateRule {

    //  <scheme>://<authority><path>?<query>#<fragment>
    private static SegmentsPredicate newSegmentsPredicate(String... schemes) {
        SegmentMetadata schemesMetadata = new SegmentMetadata(true, "[a-z][0-9a-z]+", new InRule(null, schemes));
        schemesMetadata.setName("scheme");
        return new SegmentsPredicateBuilder()
                .addSegment(null, schemesMetadata)
                .addString("://")
                .addSegment(null, "host", true, "\\[[0-9a-fA-F:]*\\]|[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*", new HostnameRule(null))
                .addSegment(":", "port", false, "\\d{1,5}", new PortRangeRule())
                .addSegment("/", "others", false, "[\\w-./?%&=]*")
                .build();
    }

    public UrlRule(String errorMessage, String... schemes) {
        super(Objs.useValueIfEmpty(errorMessage, "url is invalid"), newSegmentsPredicate(schemes));
    }

}

