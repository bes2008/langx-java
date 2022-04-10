package com.jn.langx.text.grok;


import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.Strings;
import com.jn.langx.util.Throwables;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @since 4.5.0
 */
public class PatternDefinitions {
    private static final Logger logger = Loggers.getLogger(PatternDefinitions.class);

    private static final Pattern DEFINITION_NAME_PATTERN = Pattern.compile("^\\w+$");

    public static Map<String, PatternDefinition> readDefinitions(InputStream inputStream) {
        try {
            final Map<String, PatternDefinition> map = Collects.emptyHashMap(true);

            List<String> lines = IOs.readLines(inputStream);
            Collects.forEach(lines, new Consumer<String>() {
                @Override
                public void accept(String line) {
                    line = Strings.trimToEmpty(line);
                    if (Strings.isEmpty(line)) {
                        return;
                    }
                    if (Strings.startsWith(line, "#")) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("a comment: {}", line);
                        }
                        return;
                    }
                    int sepIdx = Strings.indexOf(line, " ");
                    if (sepIdx < 1) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("illegal grok pattern definition line: {}", line);
                        }
                        return;
                    }
                    String name = Strings.substring(line, 0, sepIdx);
                    String expression = Strings.substring(line, sepIdx + 1);

                    if (!DEFINITION_NAME_PATTERN.matcher(name).matches() || Strings.isBlank(expression)) {
                        if (logger.isWarnEnabled()) {
                            logger.warn("illegal grok pattern definition line: {}", line);
                        }
                    }

                    PatternDefinition definition = new PatternDefinition(name, expression);
                    map.put(name, definition);
                }
            });
            return map;
        } catch (IOException ex) {
            throw Throwables.wrapAsRuntimeIOException(ex);
        }
    }

    public static Map<String, PatternDefinition> readDefinitions(File file){
        return readDefinitions(Resources.loadFileResource(file));
    }

    public static Map<String, PatternDefinition> readDefinitions(Resource resource) {
        InputStream in = null;
        try {
            in = resource.getInputStream();
            return readDefinitions(in);
        } catch (IOException ex) {
            throw Throwables.wrapAsRuntimeIOException(ex);
        } finally {
            IOs.close(in);
        }
    }

}
