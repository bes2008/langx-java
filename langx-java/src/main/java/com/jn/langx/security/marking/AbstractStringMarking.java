package com.jn.langx.security.marking;

public abstract class AbstractStringMarking extends AbstractMarking<String> {
    @Override
    public abstract String doTransform(String input);
}
