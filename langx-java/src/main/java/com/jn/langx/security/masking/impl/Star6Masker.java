package com.jn.langx.security.masking.impl;

import com.jn.langx.security.masking.AbstractStringMarker;
import com.jn.langx.security.masking.Maskings;

/**
 * 使用 6 颗星星替代
 * @since 5.2.0
 */
public class Star6Masker extends AbstractStringMarker {
    @Override
    public String doTransform(String text) {
        return "******";
    }

    @Override
    public String getName() {
        return Maskings.Strategy.STAR_6;
    }
}
