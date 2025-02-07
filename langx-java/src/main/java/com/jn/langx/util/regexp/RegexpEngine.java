package com.jn.langx.util.regexp;

import com.jn.langx.Named;
import com.jn.langx.util.function.Supplier2;

/**
 * @since 4.5.0
 * A factory to create a regexp
 */
public interface RegexpEngine extends Named, Supplier2<String, Option, Regexp> {
    /**
     * 创建 Regexp实例
     * @param pattern 正则表达式
     * @param option 选项
     * @return 正则表达式
     */
    @Override
    Regexp get(String pattern, Option option);
}
