package com.jn.langx.text.pinyin;


import com.jn.langx.Named;
import com.jn.langx.configuration.Configuration;
import com.jn.langx.pipeline.Pipelines;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.trie.TrieMap;
import com.jn.langx.util.function.Functions;

import java.util.Set;

/**
 * 拼音词典
 */
class PinyinDirectory implements Named, Configuration {
    private String id;
    /**
     * 词典名称
     */
    private String name;
    private TrieMap<PinyinDirectoryItem> dict = new TrieMap<PinyinDirectoryItem>();


    public void putItem(String key, PinyinDirectoryItem item) {
        this.dict.put(key, item);
    }

    public PinyinDirectoryItem getItem(String key) {
        return this.dict.get(key);
    }

    public Set<String> keys(){
        return Pipeline.of(this.dict.keySet()).map(Functions.<CharSequence>toStringFunction()).asSet(true);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
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
}
