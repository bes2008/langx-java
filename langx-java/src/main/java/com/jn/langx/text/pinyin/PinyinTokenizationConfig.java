package com.jn.langx.text.pinyin;

import com.jn.langx.util.collection.Collects;

import java.util.List;

public class PinyinTokenizationConfig {
    /**
     * 一个token 的最大字符数
     */
    private int tokenMaxChar = 4;
    private List<PinyinDict> dicts;
    private boolean surnameFirst;

    public int getTokenMaxChar() {
        return tokenMaxChar;
    }

    public boolean isSurnameFirst() {
        return surnameFirst;
    }

    public void setSurnameFirst(boolean surnameFirst) {
        this.surnameFirst = surnameFirst;
    }


    public void setTokenMaxChar(int tokenMaxChar) {
        if (tokenMaxChar >= 1) {
            this.tokenMaxChar = tokenMaxChar;
        }
    }

    public List<PinyinDict> getDicts() {
        return Collects.asList(dicts);
    }

    public void setDicts(List<PinyinDict> dicts) {
        this.dicts = dicts;
    }
}
