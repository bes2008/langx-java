package com.jn.langx.text.grok;

import com.jn.langx.Parser;

/**
 * @since 4.5.0
 */
public interface GrokTemplatizedPatternParser extends Parser<String, TemplatizedPattern> {
    @Override
    TemplatizedPattern parse(String template);
}
