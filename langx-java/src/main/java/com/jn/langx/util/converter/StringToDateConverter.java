package com.jn.langx.util.converter;

import com.jn.langx.Converter;
import com.jn.langx.util.Dates;

import java.util.Date;

public class StringToDateConverter implements Converter<String, Date> {
    public static final StringToDateConverter INSTANCE = new StringToDateConverter();

    private String pattern;

    public StringToDateConverter(){
        this(Dates.yyyy_MM_dd_HH_mm_ss_SSS);
    }

    public StringToDateConverter(String pattern){
        this.pattern = pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    @Override
    public boolean isConvertible(Class sourceClass, Class targetClass) {
        return sourceClass==String.class;
    }

    @Override
    public Date apply(String value) {
        return Dates.parse(value, pattern);
    }
}
