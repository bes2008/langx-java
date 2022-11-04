package com.jn.langx.text.pinyin;

import com.jn.langx.util.collection.Collects;

import java.util.List;

/**
 * @since 5.1.0
 */
class ChineseSequenceToken extends RegionToken<List<Token>> {
    ChineseSequenceToken() {
        setBody(Collects.<Token>emptyArrayList());
    }

    void addToken(Token token) {
        this.getBody().add(token);
    }

    /**
     * 正常情况下，该集合下都是调用该方法，放的是 映射到的字符token
     * @param item
     */
    void addToken(PinyinDictItem item) {
        PinyinDictItemToken token = new PinyinDictItemToken();
        token.setBody(item);
        addToken(token);
    }

    /**
     * 当遇到中文标点符号、或者某个汉字没有在字典里时，会走这个方法
     * @param item
     */
    void addToken(String item) {
        StringToken token = new StringToken();
        token.setBody(item);
        addToken(token);
    }

    @Override
    public String toString() {
        return isPunctuationSymbol() ? this.getBody().get(0).toString() : this.getBody().toString();
    }
}
