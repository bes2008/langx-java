package com.jn.langx.event;

import java.util.EventObject;

/**
 * @author jinuo.fang
 * @param <Source>
 */
public class DomainEvent<Source> extends EventObject {
    private static final long serialVersionUID=1L;
    private String domain;
    private Source source;
    public DomainEvent(String eventDomain, Source source) {
        super(source);
        this.domain = eventDomain;
        this.source = source;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    @Override
    public Source getSource() {
        return source;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
