package com.jn.langx.util.regexp;

import com.jn.langx.Named;
import com.jn.langx.util.function.Supplier2;

public interface RegexpFactory extends Named, Supplier2<String, Option, Regexp> {
    @Override
    Regexp get(String pattern, Option option);
}
