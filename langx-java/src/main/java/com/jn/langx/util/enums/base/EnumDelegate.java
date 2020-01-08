package com.jn.langx.util.enums.base;

/**
 * Use it, every enum will have the follow field: code, name, displayText;
 * <p>
 * you can declare any enum like this:
 * <pre>
 *     public enum Period implements Delegatable<EnumDelegate> {
 *         MINUTES(0, "minutes", "minutes"),
 *         HOURS(1, "hours", "hours"),
 *         DAY(2, "day", "day"),
 *         MONTH(3, "month", "month");
 *
 *
 *         private int code;
 *         private String name;
 *         private String displayText;
 *         private EnumDelegate delegate;
 *
 *         Period(int code, String name, String displayText){
 *             this.code = code;
 *             this.name = name;
 *             this.displayText = displayText;
 *
 *             setDelegate(new EnumDelegate(code, name, displayText));
 *         }
 *
 *         public int getCode(){
 *             return code;
 *         }
 *
 *         public String getName(){
 *             return name;
 *         }
 *
 *         public String getDisplayText(){
 *             return displayText;
 *         }
 *
 *         public EnumDelegate getDelegate(){
 *             return delegate;
 *         }
 *
 *         public void setDelegate(EnumDelegate delegate){
 *             this.delegate = delegate;
 *         }
 *     }
 * </pre>
 * <p>
 * if use it, you can get any enum instance by CommonEnums:
 * for examples:
 * <pre>
 *     // get an enum instance by code
 *     Period p = CommonEnums.ofCode(0);
 *
 *     // get an enum instance by name
 *     Period p = CommonEnums.ofName("minutes");
 *
 *     // get an enum instance by displayText
 *     Period p = CommonEnums.ofDisplayText("minutes");
 * </pre>
 */
public class EnumDelegate implements CommonEnum {
    private int code;
    private String name;
    private String displayText;

    public EnumDelegate(int code, String name, String displayText) {
        this.code = code;
        this.name = name;
        this.displayText = displayText;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getDisplayText() {
        return this.displayText;
    }

    @Override
    public void setDisplayText(String text) {
        this.displayText = text;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}