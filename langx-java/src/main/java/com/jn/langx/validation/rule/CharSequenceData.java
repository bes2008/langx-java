package com.jn.langx.validation.rule;

public class CharSequenceData {
    private String[] sequences;

    public CharSequenceData(String... sequences) {
        if (sequences.length < 1) {
            throw new IllegalArgumentException("At least one sequence must be defined");
        }

        this.sequences = sequences;
    }

    public String[] getSequences(){
        return this.sequences;
    }

    public static final CharSequenceData DIGITS = new CharSequenceData(CharData.DIGITS.getChars());
    public static final CharSequenceData UPPER_CASE = new CharSequenceData(CharData.UPPER_CASE.getChars());
    public static final CharSequenceData LOWER_CASE = new CharSequenceData(CharData.LOWER_CASE.getChars());
    public static final CharSequenceData KEYBOARD_QWERTY = new CharSequenceData(
            "`1234567890-=",
            "~!@#$%^&*()_+",
            "qwertyuiop[]\\",
            "QWERTYUIOP{}|",
            "asdfghjkl;'",
            "ASDFGHJKL:\"",
            "\\zxcvbnm,./",
            "\\ZXCVBNM<>?"
    );

}
