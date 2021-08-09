package com.jn.langx.util.collection;

import java.io.Serializable;
import java.util.Map;

public class CommonProps<P> implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Map<String, P> props;

    public Map<String, P> getProps() {
        return props;
    }

    public void setProps(Map<String, P> props) {
        this.props = props;
    }

    public MapAccessor getPropsAccessor() {
        return props == null ? new MapAccessor(Collects.emptyHashMap()) : new MapAccessor(props);
    }

}
