package com.jn.langx.text.grok;

import java.util.Map;

/**
 * GrokTemplate接口定义了用于从文本中提取数据的通用模板
 * 它提供了一个方法来根据特定的模式从给定的文本中提取信息，并以键值对的形式返回
 *
 * @since 4.5.0
 */
public interface GrokTemplate {
    /**
     * 从给定的文本中提取信息
     *
     * @param text 待提取信息的文本数据这是未经过处理的原始文本，可能包含多种格式的数据
     * @return Map<String, Object> 返回一个映射，其中包含从文本中提取的键值对
     *         键是匹配到的字段名称，值是对应的提取数据
     */
    Map<String, Object> extract(String text);
}
