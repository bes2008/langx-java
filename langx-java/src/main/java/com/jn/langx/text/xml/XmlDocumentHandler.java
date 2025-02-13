package com.jn.langx.text.xml;

import org.w3c.dom.Document;

/**
 * <pre>
 * 接口定义，用于处理XML文档
 * 它允许实现类根据自己的需要定制XML文档的处理逻辑
 * </pre>
 *
 * @param <T> 泛型参数，表示处理XML文档后返回的对象类型
 */
public interface XmlDocumentHandler<T> {

    /**
     * 处理XML文档的方法
     *
     * @param doc 待处理的XML文档对象，它是org.w3c.dom.Document类型
     *            此参数是处理XML文档的原始输入
     * @return T 处理XML文档后返回的对象，具体类型由调用者指定
     *          它可以是任何类型，如字符串、数字、自定义对象等
     * @throws Exception 当处理XML文档过程中发生错误时抛出此异常
     *                   它允许调用者根据异常信息处理错误情况
     */
    T handle(final Document doc) throws Exception;
}
