package com.jn.langx.security.marking;

public abstract class AbstractStringMarker extends Marker<String> {
    @Override
    public abstract String doTransform(String input);
}
