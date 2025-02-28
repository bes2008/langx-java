package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;

public class EmailRule extends PredicateRule {
    public EmailRule(){
        this(null);
    }
    public EmailRule(String errorMessage, final String... validDomains) {
        super(new SegmentsPredicateBuilder()
                        .addSegment(null, "username", true, "[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*",
                                new LengthRangeRule(1, 64))
                        .addString("@")
                        .addSegment(null, "domain", true, "[a-z0-9-]+(\\.[a-z0-9-]+)+",
                                new LengthRangeRule(4, 255), new Rfc1123HostnameRule(), new PredicateRule(new Predicate<String>() {
                                    @Override
                                    public boolean test(String domain) {
                                        if (Objs.isNotEmpty(validDomains)) {
                                            return Collects.contains(validDomains, domain);
                                        }
                                        return true;
                                    }
                                },null)
                        ).build()
                , Objs.useValueIfEmpty(errorMessage, "invalid email address"));
    }
}
