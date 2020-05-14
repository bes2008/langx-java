package com.jn.langx.util.valuegetter;

public class CompositeValueGetter implements ValueGetter {
    private ValueGetter valueGetter;

    public CompositeValueGetter(ValueGetter valueGetter) {
        this.valueGetter = valueGetter;
    }

    @Override
    public Object get(Object input) {
        return valueGetter.get(input);
    }
}
