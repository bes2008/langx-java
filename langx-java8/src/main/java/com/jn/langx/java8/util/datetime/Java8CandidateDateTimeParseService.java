package com.jn.langx.java8.util.datetime;

import com.jn.langx.util.datetime.DateTimeParser;
import com.jn.langx.util.datetime.parser.AbstractCandidateDateTimeParseService;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Java8CandidateDateTimeParseService extends AbstractCandidateDateTimeParseService {
    @Override
    public String getName() {
        return "Java8CandidateDateTimeParseService";
    }

    @Override
    protected DateTimeParser newParser(List<String> candidatePatterns, List<TimeZone> candidateTimeZones, List<Locale> candidateLocales) {
        return new Java8CandidatePatternsDateTimeParser(candidatePatterns, candidateTimeZones, candidateLocales);
    }

}
