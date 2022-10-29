package com.jn.langx.text.pinyin;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.buffer.CharSequenceBuffer;
import com.jn.langx.util.regexp.RegexpPatterns;
import com.jn.langx.util.regexp.Regexps;

import java.util.ArrayList;
import java.util.List;

class LexicalAnalyzer {
    /**
     * 一个token 的最大字符数
     */
    private int tokenMaxChar = 4;
    private PinyinDirectory[] dicts;

    public int getTokenMaxChar() {
        return tokenMaxChar;
    }

    public void setTokenMaxChar(int tokenMaxChar) {
        this.tokenMaxChar = tokenMaxChar;
    }

    public List<PinyinDirectory> getDicts() {
        return Collects.asList(dicts);
    }

    public void setDicts(List<PinyinDirectory> dicts) {
        this.dicts = Collects.toArray(dicts, PinyinDirectory[].class);
    }

    public List<Token> analyze(String text) {
        if (Strings.isEmpty(text)) {
            return Collects.emptyArrayList();
        }
        List<Token> tokens = new ArrayList<Token>(text.length());

        CharSequenceBuffer csb = new CharSequenceBuffer(text);
        csb.mark();

        while (csb.position() < csb.limit() && (csb.markValue() < 0 || csb.position() - csb.markValue() < tokenMaxChar)) {
            String c = csb.get() + "";
            // 是中文 ？
            boolean isChinese = Regexps.match(RegexpPatterns.CHINESE_CHAR, c);
            // 是中文标点符号？
            boolean isChinesePunctuationSymbol = Pinyins.isChinesePunctuationSymbol(c);
            boolean findStopWord = !isChinese || isChinesePunctuationSymbol;

            if (findStopWord) {
                // 对中文处理：
                long start = csb.markValue();
                if (start < 0) {
                    // 没标记时
                    start = 0;
                }
                long end = findStopWord ? csb.position() - 1 : csb.position();


                while (start < end) {
                    String chineseWords = csb.toString(start, end);
                    PinyinDirectoryItem item = find(chineseWords);
                    if (item != null) {
                        PinyinDirectoryItemToken token = new PinyinDirectoryItemToken();
                        token.setBody(item);
                        tokens.add(token);
                        start = end;
                        end = findStopWord ? csb.position() - 1 : csb.position();
                    } else {
                        end = end - 1;

                        if (end <= start && start == csb.markValue()) {
                            StringToken token = new StringToken();
                            String w = csb.get(start) + "";
                            token.setBody(w);
                            tokens.add(token);
                            start++;
                            end = findStopWord ? csb.position() - 1 : csb.position();
                        }

                    }
                }

                // 对停止词处理
                if (isChinesePunctuationSymbol) {
                    PinyinDirectoryItem item = find(c, Pinyins.CHINESE_PUNCTUATION_SYMBOLS);
                    PinyinDirectoryItemToken token = new PinyinDirectoryItemToken();
                    token.setBody(item);
                    tokens.add(token);
                } else {
                    StringToken token = new StringToken();
                    token.setBody(c);
                    tokens.add(token);
                }
            }
        }

        return tokens;
    }

    private PinyinDirectoryItem find(String chineseWords, PinyinDirectory... dicts) {
        if (Objs.isEmpty(dicts)) {
            dicts = this.dicts;
        }
        PinyinDirectoryItem item = null;
        for (PinyinDirectory dict : dicts) {
            item = dict.getItem(chineseWords);
            if (item != null) {
                break;
            }
        }
        return item;
    }
}
