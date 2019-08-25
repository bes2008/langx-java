package com.jn.langx.algorithm.ahocorasick.trie.handler;

import com.jn.langx.algorithm.ahocorasick.trie.Emit;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractStatefulEmitHandler implements StatefulEmitHandler {

    private final List<Emit> emits = new ArrayList<Emit>();

    public void addEmit(final Emit emit) {
        this.emits.add(emit);
    }

    @Override
    public List<Emit> getEmits() {
        return this.emits;
    }

}
