package com.jn.langx.exception;

import com.jn.langx.text.ParameterizedMessage;

public class ExceptionMessage extends ParameterizedMessage {
    public ExceptionMessage(String msg) {
        super(msg);
    }

    public ExceptionMessage(String msg, Object... object) {
        super(msg, object);
    }

}
