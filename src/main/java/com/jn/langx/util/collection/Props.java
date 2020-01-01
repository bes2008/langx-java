package com.jn.langx.util.collection;

import com.jn.langx.io.resource.Resource;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.io.IOs;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Properties;

public class Props {
    private Props() {
    }

    public Properties load(Resource resource) throws IOException {
        Preconditions.checkNotNull(resource);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            return load(inputStream);
        } finally {
            IOs.close(inputStream);
        }
    }

    public Properties load(Reader reader) throws IOException {
        Properties props = new Properties();
        props.load(reader);
        return props;
    }

    public Properties load(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.load(inputStream);
        return props;
    }

    public Properties loadFromXml(Resource resource) throws IOException {
        Preconditions.checkNotNull(resource);
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            return loadFromXml(inputStream);
        } finally {
            IOs.close(inputStream);
        }
    }

    public Properties loadFromXml(InputStream inputStream) throws IOException {
        Properties props = new Properties();
        props.loadFromXML(inputStream);
        return props;
    }
}
