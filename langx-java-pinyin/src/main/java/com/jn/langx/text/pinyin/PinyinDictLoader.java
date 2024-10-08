package com.jn.langx.text.pinyin;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;
import org.slf4j.Logger;

import java.util.List;

/**
 * @since 5.1.0
 */
class PinyinDictLoader {
    private static final Logger logger = Loggers.getLogger(PinyinDictLoader.class);
    private Regexp regexp = Regexps.createRegexp("(?<word>.+):(?<mapping>.+)(?:\\s*\\#.*)?");

    private static final String LOG_INVALID_ITEM = "invalid dict item {} : {}";

    PinyinDict load(final String dictName, final Resource resource) {
        try {
            List<String> lines = Resources.readLines(resource, Charsets.UTF_8);

            final PinyinDict directory = new PinyinDict();
            directory.setName(dictName);
            directory.setId(dictName);

            // 标点符号
            final boolean isPunctuationSymbol = Strings.startsWith(dictName, "chinese_punctuation_symbol");
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
                        mapping = Strings.trim(mapping);
                        int commentIndex = Strings.indexOf(mapping, "#");
                        if (commentIndex >= 0) {
                            mapping = Strings.substring(mapping, 0, commentIndex);
                        }
                        if (Strings.isBlank(word) || Strings.isBlank(mapping)) {
                            // ignore
                            logger.warn(LOG_INVALID_ITEM, dictName, line);
                            return;
                        }
                        String[] maybe = Strings.split(mapping, ",");
                        if (Objs.length(maybe) < 1 && !isPunctuationSymbol) {
                            logger.warn(LOG_INVALID_ITEM, dictName, line);
                            return;
                        }

                        PinyinDictItem item = new PinyinDictItem();
                        item.setWord(word);
                        item.setMapping(mapping);


                        item.setPunctuationSymbol(isPunctuationSymbol);
                        item.setTraditional(isTraditional);

                        if (!isPunctuationSymbol && !isTraditional) {
                            String pinyinWithTone = maybe[0];
                            String pinyinWithoutTone = PinyinTable.replaceTone(pinyinWithTone);

                            item.setPinyinWithTone(pinyinWithTone);
                            item.setPinyinWithoutTone(pinyinWithoutTone);
                        }

                        directory.putItem(word, item);
                    } else {
                        logger.warn(LOG_INVALID_ITEM, dictName, line);
                    }
                }
            });

            return directory;
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return null;
    }
}
