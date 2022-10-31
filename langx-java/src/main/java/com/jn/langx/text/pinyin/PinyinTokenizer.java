package com.jn.langx.text.pinyin;

import com.jn.langx.text.tokenizer.AbstractTokenizer;

import java.util.Iterator;

public class PinyinTokenizer extends AbstractTokenizer<SegmentToken> {
    private Lexer lexer;
    private String text;
    private Iterator<SegmentToken> iter;

    public PinyinTokenizer(String text, PinyinTokenizationConfig config) {
        this.lexer = new Lexer();
        this.lexer.setConfig(config);
        this.text = text;
    }

    @Override
    protected SegmentToken getNext() {
        if (this.iter == null) {
            this.iter = this.lexer.analyze(this.text).iterator();
        }
        if (this.iter.hasNext()) {
            return this.iter.next();
        }
        return null;
    }
}
