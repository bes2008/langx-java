package com.jn.langx.text;

/**
 * 参数化消息
 * <br>
 * 使用方式：<br>
 * <code>
 * new ParameterizedMessage("hello {0}, welcome you to {1} . ", new Object[]{"zhang san", "BeiJing"});
 * </code> <br>那么结果就是：
 * "hello zhang san, welcome you to BeiJing ."
 *
 * @author fangjinuo
 */
public class ParameterizedMessage {
    private volatile String template;
    private Object[] args = null;

    public ParameterizedMessage() {
    }

    public ParameterizedMessage(String template) {
        this.template = template;
    }

    public ParameterizedMessage(String template, Object... args) {
        this.template = template;
        this.args = args;
    }

    public String getMessage() {
        if (template != null) {
            return StringTemplates.format(template, args);
        }
        return "";
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
