package com.jn.langx.regexp.joni.test.grok;

import com.jn.langx.cache.Cache;
import com.jn.langx.cache.CacheBuilder;
import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.regexp.joni.test.grok.logstash.EcsCompatibility;
import com.jn.langx.regexp.joni.test.grok.logstash.LogStashLocalPatternDefinitionsLoader;
import com.jn.langx.text.grok.GrokCompiler;
import com.jn.langx.text.grok.pattern.*;
import com.jn.langx.text.grok.GrokTemplate;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.regexp.DefaultMatcherWatchdog;
import com.jn.langx.util.timing.timer.HashedWheelTimer;
import com.jn.langx.util.timing.timer.WheelTimers;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class GrokTest {
    static MultipleLevelPatternDefinitionRepository repository;
    static GrokTemplate tomcatLogTemplate;
    static GrokTemplate javastackTemplate;

    @BeforeClass
    public static void init() {


        HashedWheelTimer timer = WheelTimers.newHashedWheelTimer();


        // single file repository:
        SimplePatternDefinitionRepository singleFileRepository = new SimplePatternDefinitionRepository();
        singleFileRepository.setName("simple-repository");
        /*
        Cache<String, PatternDefinition> singleFileRepositoryCache = CacheBuilder.<String, PatternDefinition>newBuilder()
                .timer(timer)
                .build();
        singleFileRepository.setCache(singleFileRepositoryCache);
        singleFileRepository.setTimer(timer);
        */
        PatternDefinitionSingleFileLoader loader = new PatternDefinitionSingleFileLoader(Resources.loadClassPathResource("grok_pattern_tomcat.txt", GrokTest.class));
        singleFileRepository.setConfigurationLoader(loader);

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
        repository.addRepository(singleFileRepository, Integer.MIN_VALUE);
        repository.addRepository(logstashFileRepository, Integer.MAX_VALUE);

        Cache<String, PatternDefinition> cache3 = CacheBuilder.<String, PatternDefinition>newBuilder()
                .timer(timer)
                .build();
        repository.setCache(cache3);
        GrokCompiler grokCompiler = new GrokCompiler("joni-grok");
        grokCompiler.setRegexpEngine("joni");
        grokCompiler.setDefinitionRepository(repository);

        final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        grokCompiler.setWatchdog(new DefaultMatcherWatchdog(50, 1000, new Consumer2<Long, Runnable>() {
            @Override
            public void accept(Long intervalInMills, Runnable interruptTask) {
                scheduledThreadPoolExecutor.schedule(interruptTask, intervalInMills, TimeUnit.MILLISECONDS);
            }
        }));
        grokCompiler.startup();
        tomcatLogTemplate = grokCompiler.compile("%{TOMCAT7_LOG}(?:%{CRLF}?%{JAVASTACK:stack})?");
        javastackTemplate = grokCompiler.compile("(?:%{CRLF}?%{JAVASTACK:stack})?");
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

                    // message = Strings.replace(Strings.replace(message,"\n", "\\n"),"\t","\\t");
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
