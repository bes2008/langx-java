package com.jn.langx.text.pinyin;

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
    private List<PinyinDirectory> dicts;

    public int getTokenMaxChar() {
        return tokenMaxChar;
    }

    public void setTokenMaxChar(int tokenMaxChar) {
        this.tokenMaxChar = tokenMaxChar;
    }

    public List<PinyinDirectory> getDicts() {
        return dicts;
    }

    public void setDicts(List<PinyinDirectory> dicts) {
        this.dicts = dicts;
    }

    public List<Token> analyze(String text) {
        if (Strings.isEmpty(text)) {
            return Collects.emptyArrayList();
        }
        List<Token> tokens = new ArrayList<Token>(text.length());

        CharSequenceBuffer csb = new CharSequenceBuffer(text);

        while (csb.position() < csb.limit() && (csb.markValue() < 0 || csb.position() - csb.markValue() < tokenMaxChar)) {
            String c = csb.get() + "";
            // 是中文 ？
            boolean findStopWord = Regexps.match(RegexpPatterns.CHINESE_CHAR, c);
            if (findStopWord) {
                // 对中文处理：
                long start = csb.markValue();
                long end = csb.position();


                while (start < end) {
                    String chineseWords = csb.toString(start, end);
                    PinyinDirectoryItem item = null;
                    for (PinyinDirectory dict : dicts) {
                        item = dict.getItem(chineseWords);
                        if (item != null) {
                            break;
                        }
                    }
                    if (item != null) {
                        PinyinDirectoryItemToken token = new PinyinDirectoryItemToken();
                        token.setBody(item);
                        tokens.add(token);
                        start = end;
                        end = csb.position();
                    } else {
                        end = end - 1;

                        if (end <= start && start == csb.markValue()) {
                            StringToken token = new StringToken();
                            String w = csb.get(start) + "";
                            token.setBody(w);
                            tokens.add(token);
                            start++;
                            end = csb.position();
                        }

                    }
                }


                // 对停止词处理
                StringToken token = new StringToken();
                token.setBody(c);
                tokens.add(token);
            }
        }

        return tokens;
    }
}
