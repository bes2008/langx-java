package com.jn.langx.util.logging.masking;

public abstract class AbstractStringMarker extends Masker<String> {
    @Override
    public abstract String doTransform(String text);
}
