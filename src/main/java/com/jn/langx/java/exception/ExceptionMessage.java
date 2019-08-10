package com.jn.langx.java.exception;

import com.jn.langx.java.text.ParameterizationMessage;

public class ExceptionMessage extends ParameterizationMessage {
    public ExceptionMessage (String msg){
        super(msg);
    }

    public ExceptionMessage (String msg, Object... object){
        super(msg, object);
    }

}
