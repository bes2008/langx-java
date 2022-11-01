package com.jn.langx.text.pinyin;

import com.jn.langx.util.collection.Collects;

import java.util.List;

/**
 * @since 5.1.0
 */
class ChineseSequenceToken extends SegmentToken<List<Token>> {
    ChineseSequenceToken() {
        setBody(Collects.<Token>emptyArrayList());
    }

    void addToken(Token token) {
        this.getBody().add(token);
    }

    void addToken(PinyinDictItem item) {
        PinyinDictItemToken token = new PinyinDictItemToken();
        token.setBody(item);
        addToken(token);
    }

    void addToken(String item){
        StringToken token = new StringToken();
        token.setBody(item);
        addToken(token);
    }

    @Override
    public String toString() {
        return isPunctuationSymbol() ? this.getBody().get(0).toString() : this.getBody().toString();
    }
}
