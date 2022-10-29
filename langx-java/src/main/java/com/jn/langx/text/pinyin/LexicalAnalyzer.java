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

        long nonChineseSegmentStartIndex = -1;
        while (csb.hasRemaining()) {
            String c = csb.get() + "";
            // 是中文 ？
            boolean isChinese = Regexps.match(RegexpPatterns.CHINESE_CHAR, c);
            // 是中文标点符号？
            boolean isChinesePunctuationSymbol = Pinyins.isChinesePunctuationSymbol(c);
            boolean isEnglishPunctuationSymbol = Pinyins.isEnglishPunctuationSymbol(c);
            boolean findStopWord = isChinesePunctuationSymbol || isEnglishPunctuationSymbol;

            if (!isChinese) {
                if (nonChineseSegmentStartIndex < 0) {
                    nonChineseSegmentStartIndex = csb.position() - 1;
                }
            } else {
                if (nonChineseSegmentStartIndex > 0) {
                    long end = csb.position() - 1;
                    StringToken token = new StringToken();
                    String substring = csb.toString(nonChineseSegmentStartIndex, end);
                    token.setBody(substring);
                    tokens.add(token);
                    nonChineseSegmentStartIndex = -1;
                    csb.position(end);
                    csb.mark();
                }
            }

            /**
             * 三种情况下，会进行中文处理：
             * 1）找到了 标点符号
             * 2）文本读完了
             * 3）刚从中文段切换到非中文段时
             */
            if (findStopWord || !csb.hasRemaining() || (nonChineseSegmentStartIndex >= 0 && nonChineseSegmentStartIndex == csb.position() - 1)) {
                // 对中文处理：

                long start = csb.markValue();
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
                if (findStopWord) {
                    if (isChinesePunctuationSymbol) {
                        PinyinDirectoryItem item = find(c, Pinyins.CHINESE_PUNCTUATION_SYMBOLS_DICT);
                        PinyinDirectoryItemToken token = new PinyinDirectoryItemToken();
                        token.setBody(item);
                        tokens.add(token);
                    } else {
                        StringToken token = new StringToken();
                        token.setBody(c);
                        tokens.add(token);
                    }
                    csb.mark();
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
