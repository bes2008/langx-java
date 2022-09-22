package com.jn.langx.util.datetime.parser;

import com.jn.langx.util.datetime.DateTimeParsedResult;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public interface CandidateDateTimeParseService {
    String getName();
    DateTimeParsedResult parse(String dt, List<String> candidatePatterns, List<TimeZone> candidateTimeZones, List<Locale> candidateLocales);
}
