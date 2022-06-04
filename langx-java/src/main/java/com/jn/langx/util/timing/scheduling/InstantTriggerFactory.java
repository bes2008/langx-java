package com.jn.langx.util.timing.scheduling;

import com.jn.langx.util.Dates;
import com.jn.langx.util.collection.Collects;

import java.util.Date;
import java.util.List;

/**
 * @since 4.6.7
 */
public class InstantTriggerFactory implements TriggerFactory{
    private static final String NAME = "instant";

    @Override
    public String getName() {
        return NAME;
    }

    private List<String> datePatterns = Collects.asList(Dates.yyyy_MM_dd_HH_mm_ss,
            Dates.yyyy_MM_dd_HH_mm_ss_SSS);

    public void setDatePatterns(List<String> datePatterns) {
        this.datePatterns = datePatterns;
    }

    @Override
    public Trigger get(String expression) {
        Date date = Dates.parse(expression, datePatterns);

        return new InstantTrigger(date);
    }
}
