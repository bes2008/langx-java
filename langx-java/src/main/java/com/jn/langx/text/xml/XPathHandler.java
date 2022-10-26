package com.jn.langx.text.xml;

import com.jn.langx.Named;
import com.jn.langx.Transformer;
import com.jn.langx.util.function.Function;
/**
 * @since 5.0.2
 */
public interface XPathHandler extends Transformer<String, String>, Named {
    @Override
    String transform(String xpathParameter);

}
