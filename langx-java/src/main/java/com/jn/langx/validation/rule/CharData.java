package com.jn.langx.validation.rule;

public class CharData {
    private String chars;

    public CharData(String chars) {
        this.chars = chars;
    }

    public String getChars() {
        return chars;
    }

    public static CharData DIGITS = new CharData("0123456789");
    public static CharData UPPER_CASE = new CharData("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    public static CharData LOWER_CASE = new CharData("abcdefghijklmnopqrstuvwxyz");
    public static CharData ALPHABETICAL = new CharData(LOWER_CASE.getChars() + UPPER_CASE.getChars());

    public static CharData ALPHABET_DIGITS = new CharData(ALPHABETICAL.getChars() + DIGITS.getChars());

    public static CharData SPECIAL_ASCII = new CharData(" `~@#$%^&*()-_=+[]{};:'\",<.>/?");
    public static CharData HTTP_HEADER_NAME = new CharData(ALPHABET_DIGITS + "-");
    public static CharData HTTP_HEADER_NAME_UNDERLINE = new CharData(HTTP_HEADER_NAME + "_");

}
