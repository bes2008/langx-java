package com.jn.langx.text;

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
    private volatile String template;
    private Object[] args = null;

    public ParameterizationMessage() {
    }

    public ParameterizationMessage(String template) {
        this.template = template;
    }

    public ParameterizationMessage(String template, Object... object) {
        this.template = template;
        this.args = args;
    }

    public String getMessage() {
        return StringTemplates.format(template, args);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
