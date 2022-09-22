package com.jn.langx.java8.util.datetime;

import com.jn.langx.util.datetime.DateTimeParsedResult;
import com.jn.langx.util.datetime.parser.CandidateDateTimeParseService;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Java8CandidateDateTimeParseService implements CandidateDateTimeParseService {
    @Override
    public String getName() {
        return "Java8CandidateDateTimeParseService";
    }

    @Override
    public DateTimeParsedResult parse(String dt, List<String> candidatePatterns, List<TimeZone> candidateTimeZones, List<Locale> candidateLocales) {
        return new Java8CandidatePatternsDateTimeParser(candidatePatterns, candidateTimeZones, candidateLocales).parse(dt);
    }
}
