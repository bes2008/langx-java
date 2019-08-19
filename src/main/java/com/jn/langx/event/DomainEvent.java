package com.jn.langx.event;

import java.util.EventObject;

public class DomainEvent<Source> extends EventObject {
    public DomainEvent(Object source) {
        super(source);
    }

    @Override
    public Object getSource() {
        return super.getSource();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
