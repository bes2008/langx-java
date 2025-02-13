package com.jn.langx.text.stringtemplate;

import com.jn.langx.Formatter;

/**
 * <pre>
 * StringTemplateFormatter 接口继承了 Formatter 接口，专门用于字符串模板的格式化。
 * 它的目标是提供一个标准的方法，将输入的字符串模板与一系列参数结合起来，生成最终的格式化字符串。
 * 这个接口主要应用于需要动态生成字符串的场景，比如配置消息模板、生成SQL语句等。
 * </pre>
 */
public interface StringTemplateFormatter extends Formatter<String, String> {
    /**
     * 格式化字符串模板。
     *
     * @param input 待格式化的字符串模板，可以包含占位符来指示参数的位置。
     * @param args 用于替换模板中占位符的实际参数，参数类型不限，将按照顺序替换模板中的占位符。
     * @return 根据输入模板和参数生成的最终格式化字符串。
     */
    @Override
    String format(String input, Object... args);
}
