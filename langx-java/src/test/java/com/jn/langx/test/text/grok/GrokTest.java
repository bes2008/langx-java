package com.jn.langx.test.text.grok;

import com.jn.langx.cache.Cache;
import com.jn.langx.cache.CacheBuilder;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.grok.*;
import com.jn.langx.text.grok.logstash.LogStashLocalPatternDefinitionsLoader;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.WheelTimers;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class GrokTest {
    MultipleLevelPatternDefinitionRepository repository;
    GrokTemplatizedPatternParser patternParser;
    GrokTemplate template;

    @Before
    public void init() {
        repository = new MultipleLevelPatternDefinitionRepository();

        HashedWheelTimer timer = WheelTimers.newHashedWheelTimer();


        // single file repository:
        PatternDefinitionRepository singleFileRepository = new PatternDefinitionRepository();

        Cache<String, PatternDefinition> singleFileRepositoryCache = CacheBuilder.<String, PatternDefinition>newBuilder()
                .timer(timer)
                .build();
        singleFileRepository.setCache(singleFileRepositoryCache);
        singleFileRepository.setTimer(timer);
        PatternDefinitionSingleFileLoader loader = new PatternDefinitionSingleFileLoader(Resources.loadClassPathResource("grok_pattern_tomcat.txt", GrokTest.class));
        singleFileRepository.setConfigurationLoader(loader);
        repository.addRepository(singleFileRepository, Integer.MIN_VALUE);

        // log stash directory repository:
        PatternDefinitionRepository logstashFileRepository = new PatternDefinitionRepository();
        Cache<String, PatternDefinition> cache = CacheBuilder.<String, PatternDefinition>newBuilder()
                .timer(timer)
                .build();
        LogStashLocalPatternDefinitionsLoader logStashLocalPatternDefinitionsLoader = new LogStashLocalPatternDefinitionsLoader();
        logstashFileRepository.setConfigurationLoader(logStashLocalPatternDefinitionsLoader);

        repository.setCache(cache);
        repository.setTimer(timer);


        DefaultGrokTemplatizedPatternParser grokTemplatizedPatternParser = new DefaultGrokTemplatizedPatternParser();
        grokTemplatizedPatternParser.setPatternDefinitionRepository(repository);
        patternParser = grokTemplatizedPatternParser;

        this.template = new DefaultGrokTemplate(patternParser.parse("%{TOMCATLOG}"));
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
                } catch (IOException ex) {
                    ex.fillInStackTrace();
                }
            }
        });


    }
}
