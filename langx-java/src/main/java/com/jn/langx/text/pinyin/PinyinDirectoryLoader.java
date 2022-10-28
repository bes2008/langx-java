package com.jn.langx.text.pinyin;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;
import org.slf4j.Logger;

import java.util.List;

class PinyinDirectoryLoader {
    private static final Logger logger = Loggers.getLogger(PinyinDirectoryLoader.class);
    private Regexp regexp = Regexps.createRegexp("(?<word>.+):(?<mapping>.+)(\\s*#.*)?");

    PinyinDirectory load(String dictName, final Resource resource) {
        try {
            List<String> lines = IOs.readLines(resource.getInputStream());

            final PinyinDirectory directory = new PinyinDirectory();

            // 标点符号
            final boolean isPunctuationSymbol = Strings.startsWith(dictName, "chinese_punctuation_symbol");
            // 姓氏
            final boolean isSurname = Strings.startsWith(dictName, "chinese_surname");
            // 繁体字
            final boolean isTraditional = Strings.startsWith(dictName, "traditional_");


            Collects.forEach(lines, new Consumer<String>() {
                @Override
                public void accept(String line) {
                    if (Strings.isBlank(line)) {
                        return;
                    }
                    line = Strings.trim(line);
                    if (Strings.startsWith(line, "#")) {
                        // comment line
                        return;
                    }
                    RegexpMatcher matcher = regexp.matcher(line);
                    if (matcher.matches()) {
                        String word = matcher.group("word");
                        String mapping = matcher.group("mapping");
                        if (Strings.isBlank(word) || Strings.isBlank(mapping)) {
                            // ignore
                            return;
                        }
                        String[] maybe = Strings.split(mapping, ",");
                        if (Objs.length(maybe) < 1) {
                            return;
                        }

                        PinyinDirectoryItem item = new PinyinDirectoryItem();
                        item.setWord(word);
                        item.setMapping(mapping);


                        item.setPunctuationSymbol(isPunctuationSymbol);
                        item.setTraditional(isTraditional);

                        if (!isPunctuationSymbol && !isTraditional) {
                            String pinyinWithTone = maybe[0];
                            String pinyinWithoutTone = PinyinTable.replaceTone(pinyinWithTone);

                            item.setPinyinWithTone(pinyinWithTone);
                            item.setPinyinWithoutTone(pinyinWithoutTone);

                            if (isSurname) {
                                item.setSurnamePinyinWithTone(pinyinWithTone);
                                item.setSurnamePinyinWithoutTone(pinyinWithoutTone);
                            }
                        }

                        directory.putItem(word, item);
                    }
                }
            });

            return directory;
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }
}
