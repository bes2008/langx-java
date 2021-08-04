package com.jn.langx.text.properties;

import com.jn.langx.annotation.Nullable;
import com.jn.langx.io.resource.Resource;
import com.jn.langx.io.resource.Resources;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Predicate2;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.io.IOs;

import java.io.*;
import java.net.URL;
import java.util.Comparator;
import java.util.Map;
import java.util.Properties;

public class Props {
    private Props() {
    }

    public static Properties loadFromFile(File file) throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return load(inputStream);
        } finally {
            IOs.close(inputStream);
        }
    }

    public static Properties loadFromFile(String location) throws IOException {
        return load(Resources.loadFileResource(location));
    }

    public static Properties loadFromClasspath(String classpath) throws IOException {
        return load(Resources.loadClassPathResource(classpath));
    }

    public static Properties loadFromURL(String url) throws IOException {
        return load(Resources.loadUrlResource(url));
    }

    public static Properties loadFromURL(URL url) throws IOException {
        return load(Resources.loadUrlResource(url));
    }

    public static Properties loadFromString(String string) throws IOException {
        return load(Resources.asByteArrayResource(string.getBytes(Charsets.UTF_8)));
    }

    public static Properties load(Resource resource) throws IOException {
        Preconditions.checkNotNull(resource);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            return load(inputStream);
        } finally {
            IOs.close(inputStream);
        }
    }

    public static Properties load(Reader reader) throws IOException {
        Properties props = new Properties();
        props.load(reader);
        return props;
    }

    public static Properties load(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.load(inputStream);
        return props;
    }

    public static Properties loadFromXML(Resource resource) throws IOException {
        Preconditions.checkNotNull(resource);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            return loadFromXML(inputStream);
        } finally {
            IOs.close(inputStream);
        }
    }

    public static Properties loadFromXML(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.loadFromXML(inputStream);
        return props;
    }

    public static Map<String, String> toStringMap(@Nullable Properties properties) {
        return Collects.propertiesToStringMap(properties);
    }

    public static Map<String, String> toStringMap(@Nullable Properties properties, boolean sort) {
        return Collects.propertiesToStringMap(properties, sort);
    }

    public static Map<String, String> toStringMap(@Nullable Properties properties, @Nullable Comparator<String> keyComparator) {
        return Collects.propertiesToStringMap(properties, keyComparator);
    }

    public static Map<String, String> filter(Properties properties, Predicate2<String, String> predicate) {
        Map<String, String> map = toStringMap(properties);
        return Collects.filter(map, predicate);
    }
}
