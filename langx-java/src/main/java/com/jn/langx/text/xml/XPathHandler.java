package com.jn.langx.text.xml;

import com.jn.langx.Named;
import com.jn.langx.util.function.Function;
/**
 * @since 5.0.2
 */
public interface XPathHandler extends Function<String, String>, Named {
    @Override
    String apply(String xpath);

}
