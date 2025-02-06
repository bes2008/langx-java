package com.jn.langx.text.placeholder;

import com.jn.langx.Parser;

/**
 * PlaceholderParser接口继承自Parser接口，专门用于解析字符串类型的变量
 * 它的目标是提供一个标准的方法来解析变量名，以便获取相应的变量值
 */
public interface PlaceholderParser extends Parser<String, String> {
    /**
     * 解析变量
     * @param variable 变量名
     * @return 变量值
     */
    @Override
    String parse(String variable);
}
