package com.jn.langx.algorithm.ahocorasick.trie.handler;

import com.jn.langx.algorithm.ahocorasick.trie.Emit;

import java.util.List;

public interface StatefulEmitHandler extends EmitHandler {
    List<Emit> getEmits();
}
