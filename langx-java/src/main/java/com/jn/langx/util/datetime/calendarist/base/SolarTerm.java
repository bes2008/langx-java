package com.jn.langx.util.datetime.calendarist.base;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

/**
 * 节气类型
 */
public enum SolarTerm implements CommonEnum {

    LICHUN(1, "立春"),
    YUSHUI(2, "雨水"),
    JINGZHE(3, "惊蛰"),
    CHUNFEN(4, "春分"),

    QINGMING(5, "清明"),
    GUYU(6, "谷雨"),
    LIXIA(7, "立夏"),
    XIAOMAN(8, "小满"),

    MANGZHONG(9, "芒种"),
    XIAZHI(10, "夏至"),
    XIAOSHU(11, "小暑"),
    DASHU(12, "大暑"),

    LIQIU(13, "立秋"),
    CHUSHU(14, "处暑"),
    BAILU(15, "白露"),
    QIUFEN(16, "秋分"),

    HANLU(17, "寒露"),
    SHUANGJIANG(18, "霜降"),
    LIDONG(19, "立冬"),
    XIAOXUE(20, "小雪"),

    DAXUE(21, "大雪"),
    DONGZHI(22, "冬至"),
    XIAOHAN(23, "小寒"),
    DAHAN(24, "大寒");

    private EnumDelegate delegate;

    SolarTerm(int code, String displayText) {
        this.delegate = new EnumDelegate(code, name(), displayText);
    }


    @Override
    public String getName() {
        return delegate.getName();
    }

    @Override
    public int getCode() {
        return delegate.getCode();
    }

    @Override
    public String getDisplayText() {
        return delegate.getDisplayText();
    }
}
