package com.jn.langx.text.ini;


import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.jn.langx.configuration.ConfigurationException;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.io.IOs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * [section name]
 * a1=1
 * a2=2
 * ;this is a comment
 * #this is a comment
 * </pre>
 */
public class Ini implements Map<String, Ini.Section> {
    public static final String DEFAULT_SECTION_NAME = "";
    public static final String COMMENT_POUND = "#";
    public static final String COMMENT_SEMICOLON = ";";
    public static final String SECTION_PREFIX = "[";
    public static final String SECTION_SUFFIX = "]";
    protected static final char ESCAPE_TOKEN = '\\';
    private static final Logger logger = LoggerFactory.getLogger(Ini.class);
    private static final transient Logger log = LoggerFactory.getLogger(Ini.class);
    private final Map<String, Ini.Section> sections;

    public Ini() {
        this.sections = new LinkedHashMap();
    }

    public Ini(Ini defaults) {
        this();
        if (defaults == null) {
            throw new NullPointerException("Defaults cannot be null.");
        } else {
            Iterator var2 = defaults.getSections().iterator();

            while (var2.hasNext()) {
                Ini.Section section = (Ini.Section) var2.next();
                Ini.Section copy = new Ini.Section(section);
                this.sections.put(section.getName(), copy);
            }

        }
    }

    private static String cleanName(String sectionName) {
        return Strings.useValueIfBlank(sectionName, "").trim();
    }

    public static Ini fromResourcePath(String resourcePath) throws ConfigurationException {
        if (!Strings.isNotEmpty(resourcePath)) {
            throw new IllegalArgumentException("Resource Path argument cannot be null or empty.");
        } else {
            Ini ini = new Ini();
            ini.loadFile(resourcePath);
            return ini;
        }
    }

    protected static boolean isSectionHeader(String line) {
        String s = Strings.useValueIfBlank(line, "");
        return s.startsWith("[") && s.endsWith("]");
    }

    protected static String getSectionName(String line) {
        String s = Strings.useValueIfBlank(line, "");
        return isSectionHeader(s) ? cleanName(s.substring(1, s.length() - 1)) : null;
    }

    public boolean isEmpty() {
        Collection<Ini.Section> sections = this.sections.values();
        if (!sections.isEmpty()) {
            Iterator var2 = sections.iterator();

            while (var2.hasNext()) {
                Ini.Section section = (Ini.Section) var2.next();
                if (!section.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    public Set<String> getSectionNames() {
        return Collections.unmodifiableSet(this.sections.keySet());
    }

    public Collection<Ini.Section> getSections() {
        return Collections.unmodifiableCollection(this.sections.values());
    }

    public Ini.Section getSection(String sectionName) {
        String name = cleanName(sectionName);
        return (Ini.Section) this.sections.get(name);
    }

    public Ini.Section addSection(String sectionName) {
        String name = cleanName(sectionName);
        Ini.Section section = this.getSection(name);
        if (section == null) {
            section = new Ini.Section(name);
            this.sections.put(name, section);
        }

        return section;
    }

    public Ini.Section removeSection(String sectionName) {
        String name = cleanName(sectionName);
        return (Ini.Section) this.sections.remove(name);
    }

    public void setSectionProperty(String sectionName, String propertyName, String propertyValue) {
        String name = cleanName(sectionName);
        Ini.Section section = this.getSection(name);
        if (section == null) {
            section = this.addSection(name);
        }

        section.put(propertyName, propertyValue);
    }

    public String getSectionProperty(String sectionName, String propertyName) {
        Ini.Section section = this.getSection(sectionName);
        return section != null ? section.get(propertyName) : null;
    }

    public String getSectionProperty(String sectionName, String propertyName, String defaultValue) {
        String value = this.getSectionProperty(sectionName, propertyName);
        return value != null ? value : defaultValue;
    }

    public void loadFile(String resourcePath) throws ConfigurationException {
        InputStream is = null;
        try {
            is = Resources.loadFileResource(resourcePath).getInputStream();
            this.load(is);
        } catch (IOException ioe) {
            throw new ConfigurationException(ioe);
        } finally {
            IOs.close(is);
        }


    }

    public void load(String iniConfig) throws ConfigurationException {
        this.load(new StringReader(iniConfig));
    }

    public void load(InputStream is) throws ConfigurationException {
        if (is == null) {
            throw new NullPointerException("InputStream argument cannot be null.");
        } else {
            InputStreamReader isr;
            try {
                isr = new InputStreamReader(is, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                throw new ConfigurationException(ex);
            }

            this.load((Reader) isr);
        }
    }


    public void load(Reader reader) {
        String sectionName = "";
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuilder sectionContent = new StringBuilder();

        String rawLine = null;
        try {
            while ((rawLine = bufferedReader.readLine()) != null) {
                String line = Strings.trim(rawLine);
                if (line != null && !line.startsWith("#") && !line.startsWith(";")) {
                    String newSectionName = getSectionName(line);
                    if (newSectionName != null) {
                        this.addSection(sectionName, sectionContent);
                        sectionContent = new StringBuilder();
                        sectionName = newSectionName;
                        if (log.isDebugEnabled()) {
                            log.debug("Parsing [" + newSectionName + "]");
                        }
                    } else {
                        sectionContent.append(rawLine).append("\n");
                    }
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }

        this.addSection(sectionName, sectionContent);
    }

    public void merge(Map<String, Ini.Section> m) {
        Entry entry;
        Ini.Section section;
        if (m != null) {
            for (Iterator var2 = m.entrySet().iterator(); var2.hasNext(); section.putAll((Map) entry.getValue())) {
                entry = (Entry) var2.next();
                section = this.getSection((String) entry.getKey());
                if (section == null) {
                    section = this.addSection((String) entry.getKey());
                }
            }
        }

    }

    private void addSection(String name, StringBuilder content) {
        if (content.length() > 0) {
            String contentString = content.toString();
            String cleaned = Strings.trim(contentString);
            if (cleaned != null) {
                Ini.Section section = new Ini.Section(name, contentString);
                if (!section.isEmpty()) {
                    this.sections.put(name, section);
                }
            }
        }

    }


    public boolean equals(Object obj) {
        if (obj instanceof Ini) {
            Ini ini = (Ini) obj;
            return this.sections.equals(ini.sections);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.sections.hashCode();
    }

    public String toString() {
        if (Emptys.isNotEmpty(this.sections)) {
            final StringBuilder sb = new StringBuilder(256);
            int i = 0;
            Collects.forEach(this.sections, new Consumer2<String, Section>() {
                @Override
                public void accept(String sectionName, Section section) {
                    sb.append("[" + sectionName + "]").append("\n");
                    Collects.forEach(section.props, new Consumer2<String, String>() {
                        @Override
                        public void accept(String key, String value) {
                            sb.append(key + "=" + value).append("\n");
                        }
                    });
                    sb.append("\n");
                }
            });

            return sb.toString();
        } else {
            return "<empty INI>";
        }
    }

    public int size() {
        return this.sections.size();
    }

    public boolean containsKey(Object key) {
        return this.sections.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.sections.containsValue(value);
    }

    public Ini.Section get(Object key) {
        return (Ini.Section) this.sections.get(key);
    }

    public Ini.Section put(String key, Ini.Section value) {
        return (Ini.Section) this.sections.put(key, value);
    }

    public Ini.Section remove(Object key) {
        return (Ini.Section) this.sections.remove(key);
    }

    public void putAll(Map<? extends String, ? extends Ini.Section> m) {
        this.sections.putAll(m);
    }

    public void clear() {
        this.sections.clear();
    }

    public Set<String> keySet() {
        return Collections.unmodifiableSet(this.sections.keySet());
    }

    public Collection<Ini.Section> values() {
        return Collections.unmodifiableCollection(this.sections.values());
    }

    public Set<Entry<String, Ini.Section>> entrySet() {
        return Collections.unmodifiableSet(this.sections.entrySet());
    }

    public static class Section implements Map<String, String> {
        private final String name;
        private final Map<String, String> props;

        private Section(String name) {
            if (name == null) {
                throw new NullPointerException("name");
            } else {
                this.name = name;
                this.props = new LinkedHashMap();
            }
        }

        private Section(String name, String sectionContent) {
            if (name == null) {
                throw new NullPointerException("name");
            } else {
                this.name = name;
                Object props;
                if (Strings.isNotBlank(sectionContent)) {
                    props = toMapProps(sectionContent);
                } else {
                    props = new LinkedHashMap();
                }

                if (props != null) {
                    this.props = (Map) props;
                } else {
                    this.props = new LinkedHashMap();
                }

            }
        }

        private Section(Ini.Section defaults) {
            this(defaults.getName());
            this.putAll(defaults.props);
        }

        protected static boolean isContinued(String line) {
            if (Strings.isBlank(line)) {
                return false;
            } else {
                int length = line.length();
                int backslashCount = 0;

                for (int i = length - 1; i > 0 && line.charAt(i) == '\\'; --i) {
                    ++backslashCount;
                }

                return backslashCount % 2 != 0;
            }
        }

        private static boolean isKeyValueSeparatorChar(char c) {
            return Character.isWhitespace(c) || c == ':' || c == '=';
        }

        private static boolean isCharEscaped(CharSequence s, int index) {
            return index > 0 && s.charAt(index) == '\\';
        }

        protected static String[] splitKeyValue(String keyValueLine) {
            String line = Strings.useValueIfBlank(keyValueLine, null);
            if (line == null) {
                return null;
            } else {
                StringBuilder keyBuffer = new StringBuilder();
                StringBuilder valueBuffer = new StringBuilder();
                boolean buildingKey = true;

                for (int i = 0; i < line.length(); ++i) {
                    char c = line.charAt(i);
                    if (buildingKey) {
                        if (isKeyValueSeparatorChar(c) && !isCharEscaped(line, i)) {
                            buildingKey = false;
                        } else if (!isCharEscaped(line, i)) {
                            keyBuffer.append(c);
                        }
                    } else if (valueBuffer.length() != 0 || !isKeyValueSeparatorChar(c) || isCharEscaped(line, i)) {
                        valueBuffer.append(c);
                    }
                }

                String key = Strings.trim(keyBuffer.toString());
                String value = Strings.trim(valueBuffer.toString());
                if (key != null && value != null) {
                    Ini.log.trace("Discovered key/value pair: {} = {}", key, value);
                    return new String[]{key, value};
                } else {
                    String msg = "Line argument must contain a key and a value.  Only one string token was found.";
                    throw new IllegalArgumentException(msg);
                }
            }
        }

        private static Map<String, String> toMapProps(String content) {
            Map<String, String> props = new LinkedHashMap();
            StringBuilder lineBuffer = new StringBuilder();
            Scanner scanner = new Scanner(content);

            while (scanner.hasNextLine()) {
                String line = Strings.trim(scanner.nextLine());
                if (isContinued(line)) {
                    line = line.substring(0, line.length() - 1);
                    lineBuffer.append(line);
                } else {
                    lineBuffer.append(line);
                    line = lineBuffer.toString();
                    lineBuffer = new StringBuilder();
                    String[] kvPair = splitKeyValue(line);
                    if (kvPair != null && kvPair.length == 2) {
                        props.put(kvPair[0], kvPair[1]);
                    }
                }
            }

            return props;
        }

        public String getName() {
            return this.name;
        }

        public void clear() {
            this.props.clear();
        }

        public boolean containsKey(Object key) {
            return this.props.containsKey(key);
        }

        public boolean containsValue(Object value) {
            return this.props.containsValue(value);
        }

        public Set<Entry<String, String>> entrySet() {
            return this.props.entrySet();
        }

        public String get(Object key) {
            return (String) this.props.get(key);
        }

        public boolean isEmpty() {
            return this.props.isEmpty();
        }

        public Set<String> keySet() {
            return this.props.keySet();
        }

        public String put(String key, String value) {
            return (String) this.props.put(key, value);
        }

        public void putAll(Map<? extends String, ? extends String> m) {
            this.props.putAll(m);
        }

        public String remove(Object key) {
            return (String) this.props.remove(key);
        }

        public int size() {
            return this.props.size();
        }

        public Collection<String> values() {
            return this.props.values();
        }

        public String toString() {
            String name = this.getName();
            return "".equals(name) ? "<default>" : name;
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Ini.Section)) {
                return false;
            } else {
                Ini.Section other = (Ini.Section) obj;
                return this.getName().equals(other.getName()) && this.props.equals(other.props);
            }
        }

        public int hashCode() {
            return this.name.hashCode() * 31 + this.props.hashCode();
        }
    }
}
