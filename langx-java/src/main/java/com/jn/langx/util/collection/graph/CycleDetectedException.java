package com.jn.langx.util.collection.graph;

import com.jn.langx.util.Strings;

import java.util.List;

public class CycleDetectedException extends RuntimeException {
    private List<String> cycle;

    public CycleDetectedException(final String message, final List<String> cycle) {
        super(message);
        this.cycle = cycle;
    }

    public List<String> getCycle() {
        return cycle;
    }

    public String cycleToString() {
        return Strings.join("-->", cycle);
    }

    @Override
    public String getMessage() {
        return super.getMessage() + " " + cycleToString();
    }
}

