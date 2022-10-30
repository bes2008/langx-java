package com.jn.langx.text.pinyin;

abstract class Token<T> {
    private T body;
    private boolean punctuationSymbol = false;

    boolean isChinese() {
        return body instanceof PinyinDictItem;
    }

    boolean isPunctuationSymbol() {
        return punctuationSymbol;
    }

    public void setPunctuationSymbol(boolean punctuationSymbol) {
        this.punctuationSymbol = punctuationSymbol;
    }

    T getBody() {
        return this.body;
    }

    void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return body.toString();
    }
}
