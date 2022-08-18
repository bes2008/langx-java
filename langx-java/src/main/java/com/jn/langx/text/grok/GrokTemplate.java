package com.jn.langx.text.grok;

import java.util.Map;

/**
 * @since 4.5.0
 */
public interface GrokTemplate {
    Map<String, Object> extract(String text);
}
