package com.jn.langx.text.pinyin;

/**
 * @since 5.1.0
 */
abstract class Token<T> {
    private T body;
    private boolean punctuationSymbol = false;

    public boolean isChinese() {
        return body instanceof PinyinDictItem;
    }

    public boolean isPunctuationSymbol() {
        return punctuationSymbol;
    }

    void setPunctuationSymbol(boolean punctuationSymbol) {
        this.punctuationSymbol = punctuationSymbol;
    }

    public T getBody() {
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
