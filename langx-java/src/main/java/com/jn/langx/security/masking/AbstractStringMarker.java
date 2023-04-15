package com.jn.langx.security.masking;
/**
 * @since 5.2.0
 */
public abstract class AbstractStringMarker extends Masker<String> {
    @Override
    public abstract String doTransform(String text);
}
