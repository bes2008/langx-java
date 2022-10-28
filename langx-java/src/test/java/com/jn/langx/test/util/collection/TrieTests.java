package com.jn.langx.test.util.collection;

import com.jn.langx.util.collection.trie.TrieMap;
import org.junit.Test;

public class TrieTests {
    @Test
    public void test() {
        TrieMap<String> trie = new TrieMap<String>();
        trie.put("abolish", "To do away with");
        trie.put("abolition", "The act of abolishing");
        trie.put("absolve", "To free from guilt");
        trie.put("absolute", "Free from imperfection");
        System.out.println(trie.size());

        System.out.println(trie.get("ab"));
    }
}
