package com.jn.langx.text.sensitive;

import com.jn.langx.Transformer;
import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.collection.trie.TrieMap;

public class SensitiveWordTransformer implements Transformer<String, String> {

    public SensitiveWordTransformer(){

    }

    public SensitiveWordTransformer(Transformer<String, String> commonWordTransformer){

    }

    @NonNull
    private TrieMap<Transformer<String,String>> trieMap;
    @NonNull
    private Transformer<String, String> commonWordTransformer;

    public void addSensitiveWord(String word) {
        addSensitiveWord(word, commonWordTransformer);
    }

    public void addSensitiveWord(String word, Transformer<String, String> transformer) {
        trieMap.put(word, transformer);
    }



    @Override
    public String transform(String input) {
        StringBuilder builder=new StringBuilder(input.length());
        int start=0;

        out:
        while(start<input.length()) {
            int currentIndex=start;
            char ch = input.charAt(currentIndex);


            start = currentIndex + 1;
        }
        return builder.toString();
    }
}
