package com.jn.langx.util.regexp;

import com.jn.langx.Named;
import com.jn.langx.util.function.Supplier2;

/**
 * @since 4.5.0
 * A factory to create a regexp
 */
public interface RegexpEngine extends Named, Supplier2<String, Option, Regexp> {
    @Override
    Regexp get(String pattern, Option option);
}
