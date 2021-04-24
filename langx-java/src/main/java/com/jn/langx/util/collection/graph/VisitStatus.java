package com.jn.langx.util.collection.graph;

import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum VisitStatus implements CommonEnum {
    NOT_VISITED(0, "notVisited", "未访问"),
    VISITING(1, "visiting", "访问中"),
    VISITED(2, "visited", "已访问");

    private EnumDelegate delegate;

    VisitStatus(int code, String name, String displayText) {
        this.delegate = new EnumDelegate(code, name, displayText);
    }

    @Override
    public int getCode() {
        return this.delegate.getCode();
    }

    @Override
    public String getName() {
        return this.delegate.getName();
    }

    @Override
    public String getDisplayText() {
        return this.delegate.getDisplayText();
    }
}
