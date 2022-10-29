package com.jn.langx.text.pinyin;


import com.jn.langx.Named;
import com.jn.langx.configuration.Configuration;
import com.jn.langx.util.Maths;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.trie.TrieMap;
import com.jn.langx.util.function.Functions;

import java.util.Set;

/**
 * 拼音词典
 */
class PinyinDict implements Named, Configuration {
    private String id;
    /**
     * 词典名称
     */
    private String name;
    private TrieMap<PinyinDictItem> dict = new TrieMap<PinyinDictItem>();
    private int maxKeyLength = 0;

    void putItem(String key, PinyinDictItem item) {
        this.dict.put(key, item);
        this.maxKeyLength = Maths.max(this.maxKeyLength, key.length());
    }

    public PinyinDictItem getItem(String key) {
        return this.dict.get(key);
    }

    public Set<String> keys() {
        return Pipeline.of(this.dict.keySet()).map(Functions.<CharSequence>toStringFunction()).asSet(true);
    }

    @Override
    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public int size() {
        return this.dict.size();
    }

    public int getMaxKeyLength() {
        return this.maxKeyLength;
    }

}
