package com.jn.langx.util.datetime.parser;

import com.jn.langx.util.datetime.DateTimeParsedResult;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * 候选日期时间解析服务接口
 * 用于定义日期时间解析的服务，该服务允许通过多种候选模式、时区和语言环境来解析日期时间字符串
 */
public interface CandidateDateTimeParseService {

    /**
     * 获取服务名称
     *
     * @return 服务的名称，用于标识服务
     */
    String getName();

    /**
     * 解析日期时间字符串
     * 该方法尝试通过提供的候选模式、时区和语言环境来解析输入的日期时间字符串
     *
     * @param dt 日期时间字符串，需要被解析
     * @param candidatePatterns 一组候选的日期时间格式模式，用于尝试解析日期时间字符串
     * @param candidateTimeZones 一组候选的时区，用于确定解析后的日期时间的时区
     * @param candidateLocales 一组候选的语言环境，用于适应不同地区的日期时间格式
     * @return 解析结果对象，包含解析后的日期时间信息
     */
    DateTimeParsedResult parse(String dt, List<String> candidatePatterns, List<TimeZone> candidateTimeZones, List<Locale> candidateLocales);
}
