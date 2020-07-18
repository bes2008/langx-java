package com.jn.langx.util.timing.cron;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum CronExpressionType implements CommonEnum {

    /**
     *
     */
    QUARTZ(1,"quartz","quartz"),
    /**
     * https://man7.org/linux/man-pages/man5/crontab.5.html
     *
     * <pre>
     *      The time and date fields are:
     *
     *               field          allowed values
     *               -----          --------------
     *               minute         0-59
     *               hour           0-23
     *               day of month   1-31
     *               month          1-12 (or names, see below)
     *               day of week    0-7 (0 or 7 is Sunday, or use names)
     *
     *        A field may contain an asterisk (*), which always stands for
     *        "first-last".
     *
     *        Ranges of numbers are allowed.  Ranges are two numbers separated with
     *        a hyphen.  The specified range is inclusive.  For example, 8-11 for
     *        an 'hours' entry specifies execution at hours 8, 9, 10, and 11. The
     *        first number must be less than or equal to the second one.
     *
     *        Lists are allowed.  A list is a set of numbers (or ranges) separated
     *        by commas.  Examples: "1,2,5,9", "0-4,8-12".
     *
     *        Step values can be used in conjunction with ranges.  Following a
     *        range with "/<number>" specifies skips of the number's value through
     *        the range.  For example, "0-23/2" can be used in the 'hours' field to
     *        specify command execution for every other hour (the alternative in
     *        the V7 standard is "0,2,4,6,8,10,12,14,16,18,20,22").  Step values
     *        are also permitted after an asterisk, so if specifying a job to be
     *        run every two hours, you can use "*\/2".
     *
     *        Names can also be used for the 'month' and 'day of week' fields.  Use
     *        the first three letters of the particular day or month (case does not
     *        matter).  Ranges and lists of names are allowed. Examples:
     *        "mon,wed,fri", "jan-mar".
     * </pre>
     *
     *
     */
    UNIX_LIKE(2, "unix_like", "unix-like");

    private EnumDelegate enumDelegate;

    private CronExpressionType(int code, String name, String displayText){
        this.enumDelegate = new EnumDelegate(code, name, displayText);
    }

    @Override
    public int getCode() {
        return this.enumDelegate.getCode();
    }

    @Override
    public String getName() {
        return this.enumDelegate.getName();
    }

    @Override
    public String getDisplayText() {
        return this.enumDelegate.getDisplayText();
    }
}
