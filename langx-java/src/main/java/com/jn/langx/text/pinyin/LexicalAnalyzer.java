package com.jn.langx.text.pinyin;

import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.buffer.CharSequenceBuffer;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.regexp.RegexpPatterns;
import com.jn.langx.util.regexp.Regexps;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

class LexicalAnalyzer {
    private static final Logger logger = Loggers.getLogger(LexicalAnalyzer.class);
    /**
     * 一个token 的最大字符数
     */
    private int tokenMaxChar = 4;
    private PinyinDict[] dicts;
    private boolean surnameFirst;
    /**
     * 中文姓氏字典
     */
    private static PinyinDict chineseSurnameDict = PinyinDicts.getDict(PinyinDicts.DN_SURNAME);
    private static PinyinDict chinesePunctuationSymbolDict = PinyinDicts.getDict(PinyinDicts.DN_PUNCTUATION_SYMBOL);

    public boolean isSurnameFirst() {
        return surnameFirst;
    }

    public void setSurnameFirst(boolean surnameFirst) {
        this.surnameFirst = surnameFirst;
    }

    public int getTokenMaxChar() {
        return tokenMaxChar;
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
        this.dicts = Collects.toArray(dicts, PinyinDict[].class);
    }

    public List<Token> analyze(String text) {
        if (Strings.isEmpty(text)) {
            return Collects.emptyArrayList();
        }
        List<Token> tokens = new ArrayList<Token>(text.length());

        CharSequenceBuffer csb = new CharSequenceBuffer(text);

        long segmentStartIndex = -1;
        boolean isChineseSegment = false;
        while (csb.hasRemaining()) {
            String c = csb.get() + "";
            // 是中文 ？
            boolean isChinese = Regexps.match(RegexpPatterns.CHINESE_CHAR, c);
            // 是中文标点符号？
            boolean isChinesePunctuationSymbol = isChinese && Pinyins.isChinesePunctuationSymbol(c);
            boolean isEnglishPunctuationSymbol = Pinyins.isEnglishPunctuationSymbol(c);
            boolean isStopWord = isChinesePunctuationSymbol || isEnglishPunctuationSymbol;

            if (segmentStartIndex < 0) {
                segmentStartIndex = csb.position() - 1;
                isChineseSegment = isChinese;
            }
            boolean segmentFinished = isStopWord || !csb.hasRemaining() || isChineseSegment != isChinese;

            if (segmentFinished) {
                long segmentEnd = (csb.hasRemaining() || isStopWord) ? csb.position() - 1 : csb.position();
                if (!isChineseSegment) {
                    // 对非中文处理
                    long end = segmentEnd;
                    StringToken token = new StringToken();
                    String substring = csb.toString(segmentStartIndex, end);
                    token.setBody(substring);
                    tokens.add(token);

                } else {
                    // 对中文处理：
                    long start = segmentStartIndex;
                    long end = segmentEnd;
                    PinyinDictItem surname = null;

                    while (start < end) {
                        if (start + tokenMaxChar < end) {
                            // 避免总是进行无意义的查找
                            end = start + tokenMaxChar;
                        }
                        String chineseWords = csb.toString(start, end);
                        PinyinDictItem item = find(surname, chineseWords);
                        if (item != null) {
                            if (surnameFirst && surname == null) {
                                surname = item;
                            }
                            PinyinDictItemToken token = new PinyinDictItemToken();
                            token.setBody(item);
                            tokens.add(token);
                            start = end;
                            end = segmentEnd;
                        } else {
                            end = end - 1;

                            if (end <= start) {
                                // 汉字在字典里没找到，如果 dict/hanzi.dict 字典库足够全，可能这个代码片段就进不来了
                                StringToken token = new StringToken();
                                String w = csb.get(start) + "";
                                logger.warn("Can't find the pinyin for {}", w);
                                token.setBody(w);
                                tokens.add(token);
                                start++;
                                end = segmentEnd;
                            }

                        }
                    }
                }

                // 对停止词处理
                if (isStopWord) {
                    if (isChinesePunctuationSymbol) {
                        PinyinDictItem item = find(c, chinesePunctuationSymbolDict);
                        PinyinDictItemToken token = new PinyinDictItemToken();
                        token.setBody(item);
                        tokens.add(token);
                    } else {
                        StringToken token = new StringToken();
                        token.setBody(c);
                        tokens.add(token);
                    }
                }

                // 重置段开始
                segmentStartIndex = -1;
                if (isChinese != isChineseSegment) {
                    segmentStartIndex = csb.position() - 1;
                    isChineseSegment = isChinese;
                }
            }
        }

        return tokens;
    }

    private PinyinDictItem find(PinyinDictItem surname, String chineseWords, PinyinDict... dicts) {
        if (surnameFirst && surname == null) {
            // 姓氏还没找到时，就是优先找姓氏
            surname = findSurname(chineseWords);
            if (surname == null) {
                surname = find(chineseWords, dicts);
            }
            return surname;
        }
        return find(chineseWords, dicts);
    }

    private PinyinDictItem findSurname(String chineseWords) {
        return find(chineseWords, chineseSurnameDict);
    }

    private PinyinDictItem find(String chineseWords, PinyinDict... dicts) {
        if (Objs.isEmpty(dicts)) {
            dicts = this.dicts;
        }
        PinyinDictItem item = null;
        for (PinyinDict dict : dicts) {
            item = dict.getItem(chineseWords);
            if (item != null) {
                break;
            }
        }
        return item;
    }
}
