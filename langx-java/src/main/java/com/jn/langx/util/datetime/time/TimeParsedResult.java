package com.jn.langx.util.datetime.time;

import com.jn.langx.text.StringTemplates;

public class TimeParsedResult {
    /**
     * 24 小时制
     * 取值范围 0-23
     */
    private int hours;
    /**
     * 取值范围 0-59
     */
    private int minutes;
    /**
     * 取值范围 0-59
     */
    private int seconds;

    /**
     * 毫秒
     * 取值范围 0-999
     */
    private int mills;

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getMills() {
        return mills;
    }

    public void setMills(int mills) {
        this.mills = mills;
    }

    @Override
    public String toString() {
        if (mills > 0) {
            return StringTemplates.formatWithPlaceholder("{}:{}:{}.{}", hours, minutes, seconds, mills);
        } else {
            return StringTemplates.formatWithPlaceholder("{}:{}:{}", hours, minutes, seconds);
        }
    }
}
