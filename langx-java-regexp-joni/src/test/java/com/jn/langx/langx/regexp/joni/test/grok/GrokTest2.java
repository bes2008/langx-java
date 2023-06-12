package com.jn.langx.langx.regexp.joni.test.grok;

import com.jn.langx.io.resource.ClassPathResource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.text.grok.GrokCompiler;
import com.jn.langx.text.grok.GrokTemplate;
import com.jn.langx.text.grok.pattern.SimplePatternDefinitionRepository;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.io.IOs;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class GrokTest2 {
    static GrokTemplate tomcatLogTemplate;
    static GrokTemplate javastackTemplate;

    @BeforeClass
    public static void init() {

        SimplePatternDefinitionRepository singleFileRepository = new SimplePatternDefinitionRepository();
        singleFileRepository.setName("simple-repository");
        GrokCompiler grokCompiler = new GrokCompiler();
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