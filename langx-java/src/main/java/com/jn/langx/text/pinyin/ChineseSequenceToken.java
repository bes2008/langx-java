package com.jn.langx.text.pinyin;

import com.jn.langx.util.collection.Collects;

import java.util.List;

class ChineseSequenceToken extends SegmentToken<List<PinyinDictItemToken>> {
    ChineseSequenceToken(){
        setBody( Collects.<PinyinDictItemToken>emptyArrayList());
    }
    void addToken(PinyinDictItemToken token){
        this.getBody().add(token);
    }
    void addToken(PinyinDictItem item){
        PinyinDictItemToken token = new PinyinDictItemToken();
        token.setBody(item);
        addToken(token);
    }

    @Override
    public String toString() {
        return isPunctuationSymbol() ? this.getBody().get(0).toString() : this.getBody().toString();
    }
}
