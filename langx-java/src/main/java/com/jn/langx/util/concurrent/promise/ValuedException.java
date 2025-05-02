package com.jn.langx.util.concurrent.promise;

public class ValuedException extends RuntimeException {
    private Object value;

    public ValuedException(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }
}
