package com.jn.langx.text.placeholder;

import com.jn.langx.Parser;

public interface PlaceholderParser extends Parser<String, String> {
    /**
     * 解析变量
     * @param variable 变量名
     * @return 变量值
     */
    @Override
    String parse(String variable);
}
