package com.jn.langx.security.masking.impl;

import com.jn.langx.util.Strings;
import com.jn.langx.security.masking.AbstractStringMarker;
import com.jn.langx.security.masking.Maskings;
/**
 * @since 5.2.0
 */
public class PhoneMasker extends AbstractStringMarker {
    @Override
    public String doTransform(String text) {
        String mask = "********";
        if (text.length() >= 8) {
            int start = text.length() - 8;
            int end = text.length() - 4;
            mask = Strings.replace(text, start, end, "****");
        }
        return mask;
    }

    @Override
    public String getName() {
        return Maskings.Strategy.PHONE;
    }
}
