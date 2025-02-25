package com.jn.langx.validation.rule;

import com.jn.langx.util.Objs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate;

public class HistoryRecordsRule extends PredicateRule {
    public HistoryRecordsRule(String errorMessage, final String... historyRecords) {
        super(new Predicate<String>() {
            @Override
            public boolean test(String value) {
                return !Collects.contains(historyRecords, value);
            }
        }, Objs.useValueIfNull(errorMessage, "不能是历史记录"));
    }
}
