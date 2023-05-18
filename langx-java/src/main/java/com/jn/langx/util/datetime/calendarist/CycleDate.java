package com.jn.langx.util.datetime.calendarist;


/**
 * 干支日期
 *
 * @since 5.2.0
 */
@SuppressWarnings("DuplicatedCode")
public class CycleDate extends CalendaristDate {

    // 年份对应的干支
    private String eraYear;

    // 月份对应的干支
    private String eraMonth;

    // 天对应的干支
    private String eraDay;

    // 小时对应的干支
    private String eraHour;

    // 生肖
    private String zodiac;

    public CycleDate() {}

    public CycleDate(int year, int month, int day) {
        this(year, month, day, 0, 0, 0, 0);
    }

    public CycleDate(int year, int month, int day, int hour, int minute, int second, int millis) {
        super(year, month, day, hour, minute, second, millis);

        this.eraYear = Lunars.ganZhi(year);
        this.eraMonth = Lunars.ganZhi(month);
        this.eraDay = Lunars.ganZhi(day);
        this.eraHour = Lunars.hourGanZhi(day, hour);
        this.zodiac = Lunars.getZodiac(year + 1864);
    }

    public String getEraYear() {
        return eraYear;
    }

    public void setEraYear(String eraYear) {
        this.eraYear = eraYear;
    }

    public String getEraMonth() {
        return eraMonth;
    }

    public void setEraMonth(String eraMonth) {
        this.eraMonth = eraMonth;
    }

    public String getEraDay() {
        return eraDay;
    }

    public void setEraDay(String eraDay) {
        this.eraDay = eraDay;
    }

    public String getEraHour() {
        return eraHour;
    }

    public void setEraHour(String eraHour) {
        this.eraHour = eraHour;
    }

    public String getZodiac() {
        return zodiac;
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CycleDate{");
        sb.append("year=").append(year);
        sb.append(", month=").append(month);
        sb.append(", day=").append(day);
        sb.append(", hour=").append(hour);
        sb.append(", minute=").append(minute);
        sb.append(", second=").append(second);
        sb.append(", millis=").append(millis);
        sb.append(", timestamp=").append(timestamp);
        sb.append(", eraYear=").append(eraYear);
        sb.append(", eraMonth=").append(eraMonth);
        sb.append(", eraDay=").append(eraDay);
        sb.append(", eraHour=").append(eraHour);
        sb.append(", zodiac=").append(zodiac);
        sb.append('}');
        return sb.toString();
    }
}
