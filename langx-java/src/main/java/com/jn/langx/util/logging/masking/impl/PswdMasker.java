package com.jn.langx.util.logging.masking.impl;

import com.jn.langx.util.logging.masking.AbstractStringMarker;

public class PswdMasker extends AbstractStringMarker {
    @Override
    public String doTransform(String text) {
        return "******";
    }

    @Override
    public String getName() {
        return "pswd";
    }
}
