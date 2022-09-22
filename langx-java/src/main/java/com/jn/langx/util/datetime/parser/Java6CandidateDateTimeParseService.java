package com.jn.langx.util.datetime.parser;

import com.jn.langx.util.datetime.DateTimeParsedResult;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Java6CandidateDateTimeParseService implements CandidateDateTimeParseService {
    @Override
    public String getName() {
        return "Java6CandidateDateTimeParseService";
    }

    @Override
    public DateTimeParsedResult parse(String dt, List<String> candidatePatterns, List<TimeZone> candidateTimeZones, List<Locale> candidateLocales) {
        return new CandidatePatternsDateTimeParser(candidatePatterns, candidateTimeZones, candidateLocales).parse(dt);
    }
}
