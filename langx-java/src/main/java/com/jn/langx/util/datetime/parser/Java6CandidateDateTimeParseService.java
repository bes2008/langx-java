package com.jn.langx.util.datetime.parser;

import com.jn.langx.util.datetime.DateTimeParser;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Java6CandidateDateTimeParseService extends AbstractCandidateDateTimeParseService {

    @Override
    public String getName() {
        return "Java6CandidateDateTimeParseService";
    }

    @Override
    protected DateTimeParser newParser(List<String> candidatePatterns, List<TimeZone> candidateTimeZones, List<Locale> candidateLocales) {
        return new CandidatePatternsDateTimeParser(candidatePatterns, candidateTimeZones, candidateLocales);
    }

}
