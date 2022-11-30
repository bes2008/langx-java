package com.jn.langx.text.grok;


import com.jn.langx.lifecycle.AbstractLifecycle;
import com.jn.langx.lifecycle.InitializationException;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.text.grok.pattern.DefaultPatternDefinitionRepository;
import com.jn.langx.text.grok.pattern.PatternDefinition;
import com.jn.langx.text.grok.pattern.PatternDefinitionRepository;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;
import com.jn.langx.util.regexp.Regexp;
import com.jn.langx.util.regexp.RegexpMatcher;
import com.jn.langx.util.regexp.Regexps;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import static java.lang.String.format;

/**
 * @since 4.7.2
 */
public class GrokCompiler extends AbstractLifecycle {
    private PatternDefinitionRepository definitionRepository;

    // We don't want \n and commented line
    private static final Regexp patternLinePattern = Regexps.createRegexp("^([A-z0-9_]+)\\s+(.*)$");


    public void setDefinitionRepository(PatternDefinitionRepository definitionRepository) {
        this.definitionRepository = definitionRepository;
    }

    public Map<String, PatternDefinition> getPatternDefinitions() {
        return definitionRepository.getAll();
    }

    public GrokCompiler() {
        setName("grok-compiler");
    }

    @Override
    public void init() throws InitializationException {
        if (this.definitionRepository == null) {
            this.definitionRepository = new DefaultPatternDefinitionRepository();
            this.definitionRepository.setName(this.getName());
        }
    }

    @Override
    protected void doStart() {
        this.definitionRepository.startup();
    }

    @Override
    protected void doStop() {
        this.definitionRepository.shutdown();
    }

    /**
     * Registers a new pattern definition.
     *
     * @param name    : Pattern Name
     * @param pattern : Regular expression Or {@code Grok} pattern
     * @throws GrokException runtime expt
     **/
    public void register(String name, String pattern) {
        name = Objs.requireNonNull(name).trim();
        pattern = Objs.requireNonNull(pattern).trim();

        if (!name.isEmpty() && !pattern.isEmpty()) {
            definitionRepository.add(new PatternDefinition(name, pattern));
        }
    }

    /**
     * Registers multiple pattern definitions.
     */
    public void register(Map<String, String> patternDefinitions) {
        Objs.requireNonNull(patternDefinitions);
        Collects.forEach(patternDefinitions, new Consumer2<String, String>() {
            @Override
            public void accept(String key, String value) {
                register(key, value);
            }
        });
    }

    public void registerDefaultPatterns() {
        registerPatternFromClasspath("/patterns/patterns");
    }

    public void registerPatternFromClasspath(String path) throws GrokException {
        registerPatternFromClasspath(path, Charsets.UTF_8);
    }

    public void registerPatternFromClasspath(String path, Charset charset) throws GrokException {
        final InputStream inputStream = this.getClass().getResourceAsStream(path);
        try {
            register(inputStream, charset);
        } catch (IOException ex) {
            throw new GrokException(ex);
        }
    }

    /**
     * Registers multiple pattern definitions from a given inputStream, and decoded as a UTF-8 source.
     */
    public void register(InputStream input) throws IOException {
        register(input, Charsets.UTF_8);
    }

    /**
     * Registers multiple pattern definitions from a given inputStream.
     */
    public void register(InputStream input, Charset charset) throws IOException {
        register(new InputStreamReader(input, charset));
    }

    /**
     * Registers multiple pattern definitions from a given Reader.
     */
    public void register(Reader input) throws IOException {
        Pipeline.of(IOs.readLines(input)).map(new Function<String, RegexpMatcher>() {
            @Override
            public RegexpMatcher apply(String line) {
                return patternLinePattern.matcher(line);
            }
        }).filter(new Predicate<RegexpMatcher>() {
            @Override
            public boolean test(RegexpMatcher matcher) {
                return matcher.matches();
            }
        }).forEach(new Consumer2<Integer, RegexpMatcher>() {
            @Override
            public void accept(Integer key, RegexpMatcher m) {
                register(m.group(1), m.group(2));
            }
        });
        IOs.close(input);
    }

    /**
     * Compiles a given Grok pattern and returns a Grok object which can parse the pattern.
     */
    public GrokTemplate compile(String pattern) throws IllegalArgumentException {
        return compile(pattern, false);
    }

    public GrokTemplate compile(final String pattern, boolean namedOnly) throws IllegalArgumentException {
        return compile(pattern, TimeZone.getDefault().getID(), namedOnly);
    }

    /**
     * Compiles a given Grok pattern and returns a Grok object which can parse the pattern.
     *
     * @param pattern    : Grok pattern (ex: %{IP})
     * @param timeZoneId : time zone used to parse a timestamp when it doesn't contain the time zone
     * @param namedOnly  : Whether to capture named expressions only or not (i.e. %{IP:ip} but not ${IP})
     * @return a compiled pattern
     * @throws IllegalArgumentException when pattern definition is invalid
     */
    public GrokTemplate compile(final String pattern, String timeZoneId, boolean namedOnly) throws IllegalArgumentException {

        if (Strings.isBlank(pattern)) {
            throw new IllegalArgumentException("{pattern} should not be empty or null");
        }

        String namedRegex = pattern;
        int index = 0;

        int iterationLeft = 1000;
        boolean continueIteration = true;

        Map<String, PatternDefinition> patternDefinitionsRegistry = this.definitionRepository.getAll();
        final Map<String, String> patternDefinitions = new HashMap<String, String>();
        Collects.forEach(patternDefinitionsRegistry, new Consumer2<String, PatternDefinition>() {
            @Override
            public void accept(String key, PatternDefinition value) {
                patternDefinitions.put(key, value.getExpr());
            }
        });

        // output
        Map<String, String> namedRegexCollection = new HashMap<String, String>();
        Set<String> namedGroups = Groks.GROK_PATTERN_NAMED_GROUPS;

        // Replace %{foo} with the regex (mostly group name regex)
        // and then compile the regex
        while (continueIteration) {
            continueIteration = false;
            if (iterationLeft <= 0) {
                throw new IllegalArgumentException("Deep recursion pattern compilation of " + pattern);
            }
            iterationLeft--;
            RegexpMatcher matcher = Groks.GROK_PATTERN.matcher(namedRegex);
            // Match %{Foo:bar} -> pattern name and subname
            // Match %{Foo=regex} -> add new regex definition

            //优化： if(matcher.find()) {
            while (matcher.find()) {
                continueIteration = true;
                Map<String, String> group = Regexps.namedGroups(matcher, namedGroups);
                if (group.get("definition") != null) {
                    patternDefinitions.put(group.get("pattern"), group.get("definition"));
                    group.put("name", group.get("name") + "=" + group.get("definition"));
                }
                String grokName = group.get("name");
                if (Strings.isBlank(grokName)) {
                    throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("invalid grok, missing name: {}", namedRegex));
                }
                int count = Strings.countMatches(namedRegex, "%{" + grokName + "}");
                String _namedRegexp = namedRegex;
                for (int i = 0; i < count; i++) {
                    String definitionOfPattern = patternDefinitions.get(group.get("pattern"));
                    if (definitionOfPattern == null) {
                        throw new IllegalArgumentException(format("No definition for key '%s' found, aborting", group.get("pattern")));
                    }
                    String replacement = StringTemplates.formatWithPlaceholder("(?<name{}>{})", index, definitionOfPattern);
                    String subName = group.get("subname");
                    if (namedOnly && Strings.isEmpty(subName)) {
                        replacement = StringTemplates.formatWithPlaceholder("(?:{})", definitionOfPattern);
                    }
                    String rename = Objs.useValueIfEmpty(subName, grokName);
                    namedRegexCollection.put("name" + index, rename);
                    _namedRegexp = Strings.replace(_namedRegexp, "%{" + grokName + "}", replacement, 1);
                    index++;
                }
                if(Objs.equals(namedRegex, _namedRegexp)){
                    continueIteration = false;
                }else {
                    namedRegex = _namedRegexp;
                }

            }
        }

        if (namedRegex.isEmpty()) {
            throw new IllegalArgumentException("Pattern not found");
        }

        return new Grok(
                pattern,
                namedRegex,
                namedRegexCollection,
                patternDefinitions,
                timeZoneId
        );
    }
}
