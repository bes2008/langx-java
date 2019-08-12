package com.jn.langx.util.enums.base;

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