package com.jn.langx.util.os.virtualization;

import java.util.Map;

public class RuntimeContainer {
    private String type;
    private Map<String, String> props;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, String> getProps() {
        return props;
    }

    public void setProps(Map<String, String> props) {
        this.props = props;
    }
}
