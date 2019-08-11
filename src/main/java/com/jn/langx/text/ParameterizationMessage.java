package com.jn.langx.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 参数化消息
 * <br>
 * 使用方式：<br>
 * <code>
 * new ParameterizationMessage("hello {0}, welcome you to {1} . ", new Object[]{"zhang san", "BeiJing"});
 * </code> <br>那么结果就是：
 * "hello zhang san, welcome you to BeiJing ."
 *
 * @author fangjinuo
 */
public class ParameterizationMessage {
    protected final static Pattern pattern = Pattern.compile("\\{\\d+\\}");
    private volatile String msg = "";

    public ParameterizationMessage() {
    }

    public ParameterizationMessage(String msg) {
        this.msg = msg;
    }

    public ParameterizationMessage(String msg, Object... object) {
        this.msg = msg;
        Matcher matcher = pattern.matcher(this.msg);
        StringBuffer sb = new StringBuffer();
        int i = 0;
        while (matcher.find()) {
            matcher.appendReplacement(sb, object[i++].toString());
        }
        matcher.appendTail(sb);
        this.msg = sb.toString();
    }

    public String getMessage() {
        return msg;
    }


    public void setMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return this.msg;
    }
}
