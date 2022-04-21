package com.jn.langx.util.datetime.time;

import com.jn.langx.text.StringTemplates;

/**
 * @since 4.5.2
 */
public class TimeParsedResult {
    /**
     * 24 小时制
     * 取值范围 0-23
     */
    private int hour;
    /**
     * 取值范围 0-59
     */
    private int minute;
    /**
     * 取值范围 0-59
     */
    private int second;

    /**
     * 毫秒
     * 取值范围 0-999
     */
    private int mills;

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
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
            return StringTemplates.formatWithPlaceholder("{}:{}:{}.{}", hour, minute, second, mills);
        } else {
            return StringTemplates.formatWithPlaceholder("{}:{}:{}", hour, minute, second);
        }
    }
}
