package com.jn.langx.util.time;

import com.jn.langx.util.enums.base.CommonEnum;

public enum Month implements CommonEnum {
    jan(1, "01", "january", "一月"),
    feb(2, "02", "february", "二月"),
    mar(3, "03", "march", "三月"),
    apr(4, "04", "april", "四月"),
    may(5, "05", "may", "五月"),
    jun(6, "06", "june", "六月"),
    jul(7, "07", "july", "七月"),
    aug(8, "08", "august", "八月"),
    sep(9, "09", "september", "九月"),
    oct(10, "10", "october", "十月"),
    nov(11, "11", "november", "十一月"),
    dec(12, "12", "december", "十二月");

    private int code;
    private String fullCode;
    private String fullname;
    private String chinese;

    Month(int code, String fullCode, String fullname, String chinese) {
        this.code = code;
        this.fullCode = fullCode;
        this.fullname = fullname;
        this.chinese = chinese;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    public String getFullCode() {
        return fullCode;
    }

    public String getFullname() {
        return fullname;
    }

    public String getChinese() {
        return chinese;
    }

    @Override
    public String getDisplayText() {
        return this.chinese;
    }

    @Override
    public String getName() {
        return name();
    }
}
