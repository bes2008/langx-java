package com.jn.langx.text.xml;

import com.jn.langx.Named;
import com.jn.langx.Transformer;
/**
 * <pre>
 * XPathHandler接口定义了处理XPath表达式的标准方法。
 * 它继承了Transformer和Named接口，以实现名称绑定和通用转换功能。
 * 主要用于在提供XPath参数的情况下执行数据转换。
 * </pre>
 *
 * @since 5.0.2
 */
public interface XPathHandler extends Transformer<String, String>, Named {
    /**
     * 根据提供的XPath参数执行转换操作。
     *
     * @param xpathParameter XPath表达式参数，用于指导转换过程。
     * @return 转换后的字符串结果。
     */
    @Override
    String transform(String xpathParameter);

}
