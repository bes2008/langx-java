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

import com.jn.langx.util.StrTokenizer;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;

/**
 * A property editor for Class[].
 */
public class ClassArrayEditor extends PropertyEditorSupport {
    /**
     * Build a Class[] from a comma/whitespace seperated list of classes
     *
     * @param text - the class name list
     */
    @Override
    public void setAsText(final String text) throws IllegalArgumentException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        StrTokenizer tokenizer = new StrTokenizer(text, ","," ","\t","\r","\n");
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        while (tokenizer.hasNext()) {
            String name = tokenizer.next();
            try {
                Class<?> c = loader.loadClass(name);
                classes.add(c);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Failed to find class: " + name);
            }
        }

        Class<?>[] theValue = new Class[classes.size()];
        classes.toArray(theValue);
        setValue(theValue);
    }

    /**
     * @return a comma seperated string of the class array
     */
    @Override
    public String getAsText() {
        Class<?>[] theValue = (Class[]) getValue();
        StringBuilder text = new StringBuilder();
        int length = theValue == null ? 0 : theValue.length;
        for (int n = 0; n < length; n++) {
            text.append(theValue[n].getName());
            text.append(',');
        }
        // Remove the trailing ','
        text.setLength(text.length() - 1);
        return text.toString();
    }
}
