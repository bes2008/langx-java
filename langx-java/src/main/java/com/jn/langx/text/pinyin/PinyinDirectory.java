package com.jn.langx.text.pinyin;


import com.jn.langx.Named;
import com.jn.langx.configuration.Configuration;
import com.jn.langx.util.collection.trie.TrieMap;

/**
 * 拼音词典
 */
class PinyinDirectory implements Named, Configuration {
    private String id;
    /**
     * 词典名称
     */
    private String name;
    private TrieMap<PinyinDirectoryItem> directory = new TrieMap<PinyinDirectoryItem>();

    public TrieMap<PinyinDirectoryItem> getDirectory() {
        return directory;
    }

    public void putItem(String key, PinyinDirectoryItem item) {
        this.directory.put(key, item);
    }

    public PinyinDirectoryItem getItem(String key) {
        return this.directory.get(key);
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
