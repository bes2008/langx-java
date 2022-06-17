package com.jn.langx.navigation;

public interface Navigator<Context> {
    <T> T get(Context context, String expression);

    <T> void set(Context context, String expression, T value);
}
