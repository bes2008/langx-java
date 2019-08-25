package com.jn.langx.text.pinyin;

import com.jn.langx.algorithm.ahocorasick.trie.Emit;

import java.util.Collection;
import java.util.List;

interface SegmentationSelector {
  List<Emit> select(Collection<Emit> paramCollection);
}