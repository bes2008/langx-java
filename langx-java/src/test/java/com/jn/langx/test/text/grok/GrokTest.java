package com.jn.langx.test.text.grok;

import com.jn.langx.cache.Cache;
import com.jn.langx.cache.CacheBuilder;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.test.text.grok.logstash.EcsCompatibility;
import com.jn.langx.test.text.grok.logstash.LogStashLocalPatternDefinitionsLoader;
import com.jn.langx.text.grok.GrokCompiler;
import com.jn.langx.text.grok.GrokTemplate;

import com.jn.langx.text.grok.pattern.DefaultPatternDefinitionRepository;
import com.jn.langx.text.grok.pattern.MultipleLevelPatternDefinitionRepository;
import com.jn.langx.text.grok.pattern.PatternDefinition;
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
    GrokTemplate tomcatLogTemplate;
    GrokTemplate javastackTemplate;

    @Before
    public void init() {


        HashedWheelTimer timer = WheelTimers.newHashedWheelTimer();


        /*
        // single file repository:
        PatternDefinitionRepository singleFileRepository = new PatternDefinitionRepository();
        singleFileRepository.setName("custom-repository");
        Cache<String, PatternDefinition> singleFileRepositoryCache = CacheBuilder.<String, PatternDefinition>newBuilder()
                .timer(timer)
                .build();
        singleFileRepository.setCache(singleFileRepositoryCache);
        singleFileRepository.setTimer(timer);
        PatternDefinitionSingleFileLoader loader = new PatternDefinitionSingleFileLoader(Resources.loadClassPathResource("grok_pattern_tomcat.txt", GrokTest.class));
        singleFileRepository.setConfigurationLoader(loader);
        */

        // log stash directory repository:
        DefaultPatternDefinitionRepository logstashFileRepository = new DefaultPatternDefinitionRepository();
        Cache<String, PatternDefinition> cache = CacheBuilder.<String, PatternDefinition>newBuilder()
                .timer(timer)
                .build();
        LogStashLocalPatternDefinitionsLoader logStashLocalPatternDefinitionsLoader = new LogStashLocalPatternDefinitionsLoader();
        logStashLocalPatternDefinitionsLoader.setEcsCompatibility(EcsCompatibility.v1);
        logstashFileRepository.setConfigurationLoader(logStashLocalPatternDefinitionsLoader);
        logstashFileRepository.setCache(cache);
        logstashFileRepository.setTimer(timer);
        logstashFileRepository.setName("logstash-repository");

        //
        repository = new MultipleLevelPatternDefinitionRepository();
        //repository.addRepository(singleFileRepository, Integer.MIN_VALUE);
        repository.addRepository(logstashFileRepository, Integer.MAX_VALUE);
        Cache<String, PatternDefinition> cache3 = CacheBuilder.<String, PatternDefinition>newBuilder()
                .timer(timer)
                .build();
        repository.setCache(cache3);

        GrokCompiler compiler = new GrokCompiler();
        compiler.setDefinitionRepository(repository);
        compiler.startup();

        this.tomcatLogTemplate = compiler.compile("%{TOMCAT7_LOG}(?:%{CRLF}?%{JAVASTACK:stack})?");
        this.javastackTemplate = compiler.compile("(?:%{CRLF}?%{JAVASTACK:stack})?");
    }

    private void test(final GrokTemplate template, String[] messagePaths) {
        Collects.forEach(messagePaths, new Consumer<String>() {
            @Override
            public void accept(String messagePath) {
                ClassPathResource resource = Resources.loadClassPathResource(messagePath, GrokTest.class);
                InputStream in = null;
                try {
                    in = resource.getInputStream();
                    String message = IOs.readAsString(in);
                    IOs.close(in);
                    Map<String, Object> result = template.extract(message);
                    System.out.println(result);
                } catch (IOException ex) {
                    ex.fillInStackTrace();
                }finally {
                    IOs.close(in);
                }
            }
        });
    }

    @Test
    public void testTomcatInfoLog() {
        String[] messagePaths = new String[]{"tomcat_0.log"};
        test(this.tomcatLogTemplate, messagePaths);
    }

    @Test
    public void testJavaStack() {
        String[] messagePaths = new String[]{"java_stack.log"};
        test(this.javastackTemplate, messagePaths);
    }


    @Test
    public void testTomcatErrorLogWithStack() {
        String[] messagePaths = new String[]{"tomcat_error-0.log"};
        test(this.tomcatLogTemplate, messagePaths);
    }

    @Test
    public void testTomcatErrorLogWithoutStack() {
        String[] messagePaths = new String[]{"tomcat_error-1.log"};
        test(this.tomcatLogTemplate, messagePaths);
    }
}
