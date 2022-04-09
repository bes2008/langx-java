package com.jn.langx.test.text.grok;

import com.jn.langx.cache.Cache;
import com.jn.langx.cache.CacheBuilder;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.grok.*;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.WheelTimers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class GrokTest {
    PatternDefinitionRepository repository;
    GrokTemplatizedPatternParser patternParser;
    GrokTemplate template;

    @Before
    public void init() {
        repository = new PatternDefinitionRepository();
        HashedWheelTimer timer = WheelTimers.newHashedWheelTimer();
        Cache<String, PatternDefinition> cache = CacheBuilder.<String, PatternDefinition>newBuilder()
                .timer(timer)
                .build();
        repository.setCache(cache);
        repository.setTimer(timer);
        PatternDefinitionFileLoader loader = new PatternDefinitionFileLoader(Resources.loadClassPathResource("grok_pattern_tomcat.txt", GrokTest.class));
        repository.setConfigurationLoader(loader);


        DefaultGrokTemplatizedPatternParser grokTemplatizedPatternParser = new DefaultGrokTemplatizedPatternParser();
        grokTemplatizedPatternParser.setPatternDefinitionRepository(repository);
        patternParser = grokTemplatizedPatternParser;

        this.template  =new DefaultGrokTemplate( patternParser.parse("%{TOMCATLOG}"));
    }

    @Test
    public void test() {
        String[] messagePaths = new String[]{"tomcat-error-0.log", "tomcat-error-1.log"};
        Collects.forEach(messagePaths, new Consumer<String>() {
            @Override
            public void accept(String messagePath) {
                ClassPathResource resource = Resources.loadClassPathResource(messagePath, GrokTest.class);
                try {
                    InputStream in = resource.getInputStream();
                    String message = IOs.readAsString(in);
                    IOs.close(in);
                    Map<String, Object> result = GrokTest.this.template.extract(message);
                    System.out.println(result);
                }catch (IOException ex){
                    ex.fillInStackTrace();
                }
            }
        });


    }
}
