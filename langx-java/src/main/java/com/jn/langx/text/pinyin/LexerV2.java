package com.jn.langx.text.pinyin;

import com.jn.langx.text.tokenizer.CommonTokenizer;
import com.jn.langx.text.tokenizer.TokenFactory;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.regexp.RegexpPatterns;
import com.jn.langx.util.regexp.Regexps;
import org.slf4j.Logger;

import java.util.List;

/**
 * @since 5.1.0
 */
class LexerV2 extends CommonTokenizer<RegionToken> {
    private static Logger logger = Loggers.getLogger(LexerV2.class);
    private LexerConfig config;
    /**
     * 中文姓氏字典
     */
    private static PinyinDict chineseSurnameDict = PinyinDicts.getDict(PinyinDicts.DN_SURNAME);
    private static PinyinDict chinesePunctuationSymbolDict = PinyinDicts.getDict(PinyinDicts.DN_PUNCTUATION_SYMBOL);

    LexerV2(String text, LexerConfig config) {
        super(text, true);
        this.config = config;
        this.tokenFactory = new TokenFactory<RegionToken>() {
            @Override
            public RegionToken get(String tokenContent, Boolean isDelimiter) {
                if (isDelimiter) {
                    if (Strings.isEmpty(tokenContent)) {
                        return new EmptyStringToken();
                    }
                    String firstChar = tokenContent.charAt(0) + "";
                    // 是英文标点符号？
                    boolean isEnglishPunctuationSymbol = Pinyins.isEnglishPunctuationSymbol(firstChar);
                    if (isEnglishPunctuationSymbol) {
                        StringToken stringToken = new StringToken();
                        stringToken.setBody(tokenContent);
                        stringToken.setPunctuationSymbol(true);
                        return stringToken;
                    } else {
                        ChineseSequenceToken chineseSequenceToken = new ChineseSequenceToken();
                        PinyinDictItem item = find(tokenContent, chinesePunctuationSymbolDict);
                        chineseSequenceToken.addToken(item);
                        chineseSequenceToken.setPunctuationSymbol(true);
                        return chineseSequenceToken;
                    }
                } else {
                    String firstChar = tokenContent.charAt(0) + "";
                    boolean isChinese = Regexps.match(RegexpPatterns.CHINESE_CHAR, firstChar);
                    if (!isChinese) {
                        StringToken stringToken = new StringToken();
                        stringToken.setBody(tokenContent);
                        return stringToken;
                    } else {
                        int start = 0;
                        int regionEnd = tokenContent.length();
                        int end = regionEnd;
                        PinyinDictItem surname = null;
                        ChineseSequenceToken chineseSequenceToken = new ChineseSequenceToken();
                        while (start < end) {
                            if (start + LexerV2.this.config.getTokenMaxChar() < end) {
                                // 避免总是进行无意义的查找
                                end = start + LexerV2.this.config.getTokenMaxChar();
                            }
                            String chineseWords = tokenContent.substring(start, end);
                            PinyinDictItem item = find(surname, chineseWords);
                            if (item != null) {
                                if (LexerV2.this.config.isSurnameFirst() && surname == null) {
                                    surname = item;
                                }
                                chineseSequenceToken.addToken(item);
                                start = end;
                                end = regionEnd;
                            } else {
                                end = end - 1;

                                if (end <= start) {
                                    // 汉字在字典里没找到，如果 dict/hanzi.dict 字典库足够全，可能这个代码片段就进不来了
                                    String w = tokenContent.charAt(start) + "";
                                    logger.warn("Can't find the pinyin for {}", w);
                                    chineseSequenceToken.addToken(w);
                                    start++;
                                    end = regionEnd;
                                }

                            }
                        }
                        return chineseSequenceToken;
                    }
                }
            }
        };
    }

    @Override
    protected String getIfDelimiterStart(long position, char c) {
        String s = c + "";
        // 是中文 ？
        boolean isChinese = Regexps.match(RegexpPatterns.CHINESE_CHAR, s);
        // 是中文标点符号？
        boolean isChinesePunctuationSymbol = isChinese && Pinyins.isChinesePunctuationSymbol(s);
        boolean isEnglishPunctuationSymbol = Pinyins.isEnglishPunctuationSymbol(s);
        if (isEnglishPunctuationSymbol || isChinesePunctuationSymbol) {
            return s;
        }
        // 下面是 非 标点符号的情况
        // 接下来看看 前一个字符，与当前字符
        long lastCharPosition = position - 1;
        if (lastCharPosition >= Maths.maxLong(getBuffer().markValue(), 0L)) {
            String lastChar = getBuffer().get(lastCharPosition) + "";
            boolean lastCharIsChinese = Regexps.match(RegexpPatterns.CHINESE_CHAR, lastChar);
            boolean lastCharPunctuationSymbol = (lastCharIsChinese && Pinyins.isChinesePunctuationSymbol(lastChar)) || Pinyins.isEnglishPunctuationSymbol(lastChar);
            if (lastCharPunctuationSymbol) {
                return null;
            }
            // 当前字符与上一个字符 同一类
            if (lastCharIsChinese == isChinese) {
                return null;
            }
            return Emptys.EMPTY_STRING;
        }
        return null;
    }


    private PinyinDictItem find(PinyinDictItem surname, String chineseWords, PinyinDict... dicts) {
        if (config.isSurnameFirst() && surname == null) {
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
        List<PinyinDict> _dicts = Objs.useValueIfEmpty(Collects.asList(dicts), config.getDicts());

        PinyinDictItem item = null;
        for (PinyinDict dict : _dicts) {
            item = dict.getItem(chineseWords);
            if (item != null) {
                break;
            }
        }
        return item;
    }
}
