package com.jn.langx.util.logging.masking.impl;

import com.jn.langx.util.logging.masking.AbstractStringMarker;
import com.jn.langx.util.logging.masking.Maskings;

/**
 * 使用 6 颗星星替代
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
