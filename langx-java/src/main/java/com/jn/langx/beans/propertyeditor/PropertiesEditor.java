/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc., and individual contributors as indicated
 * by the @authors tag.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jn.langx.beans.propertyeditor;

import org.jboss.util.NestedRuntimeException;
import org.jboss.util.StringPropertyReplacer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

/**
 * A property editor for {@link Properties}.
 */
@SuppressWarnings("unchecked")
public class PropertiesEditor extends TextPropertyEditorSupport {
    /**
     * Returns a Properties object initialized with current getAsText value
     * interpretted as a .properties file contents. This replaces any
     * references of the form ${x} with the corresponding system property.
     *
     * @return a Properties object
     * @throws NestedRuntimeException An IOException occured.
     */
    public Object getValue() {
        try {
            // Load the current key=value properties into a Properties object
            String propsText = getAsText();
            Properties rawProps = new Properties(System.getProperties());
            ByteArrayInputStream bais = new ByteArrayInputStream(propsText.getBytes());
            rawProps.load(bais);
            // Now go through the rawProps and replace any ${x} refs
            Properties props = new Properties();
            Iterator keys = rawProps.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = rawProps.getProperty(key);
                String value2 = StringPropertyReplacer.replaceProperties(value, rawProps);
                props.setProperty(key, value2);
            }
            rawProps.clear();

            return props;
        } catch (IOException e) {
            throw new NestedRuntimeException(e);
        }
    }
}
