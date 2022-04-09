package com.jn.langx.text.grok;

import java.util.Map;

public interface GrokTemplate {
    Map<String, Object> extract(String text);

    String format(Map<String, Object> args);
}
