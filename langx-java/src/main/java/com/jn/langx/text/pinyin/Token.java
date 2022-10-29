package com.jn.langx.text.pinyin;

abstract class Token<T> {
    protected T body;
    boolean isChinese(){
       return body instanceof PinyinDictItem;
    }

    T getBody(){
        return this.body;
    }

     void setBody(T body) {
        this.body = body;
    }
}
