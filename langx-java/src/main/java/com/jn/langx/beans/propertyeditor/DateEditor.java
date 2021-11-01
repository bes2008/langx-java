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
import org.jboss.util.Strings;

import java.beans.PropertyEditorSupport;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A property editor for {@link Date}.
 */
@SuppressWarnings("unchecked")
public class DateEditor extends PropertyEditorSupport {
    /**
     * The formats to use when parsing the string date
     */
    private static DateFormat[] formats;

    static {
        initialize();
    }

    /**
     * Setup the parsing formats. Offered as a separate static method to allow
     * testing of locale changes, since SimpleDateFormat will use the default
     * locale upon construction. Should not be normally used!
     */
    public static void initialize() {
        PrivilegedAction action = new PrivilegedAction() {
            public Object run() {
                String defaultFormat = System.getProperty("org.jboss.util.propertyeditor.DateEditor.format", "MMM d, yyyy");
                String defaultLocale = System.getProperty("org.jboss.util.propertyeditor.DateEditor.locale");
                DateFormat defaultDateFormat;
                if (defaultLocale == null || defaultLocale.length() == 0) {
                    defaultDateFormat = new SimpleDateFormat(defaultFormat);
                } else {
                    defaultDateFormat = new SimpleDateFormat(defaultFormat, Strings.parseLocaleString(defaultLocale));
                }

                formats = new DateFormat[]
                        {
                                defaultDateFormat,
                                // Tue Jan 04 00:00:00 PST 2005
                                new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy"),
                                // Wed, 4 Jul 2001 12:08:56 -0700
                                new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z")
                        };
                return null;
            }
        };
        AccessController.doPrivileged(action);
    }

    /**
     * Keep the text version of the date
     */
    private String text;

    /**
     * Sets directly the java.util.Date value
     *
     * @param value a java.util.Date
     */
    public void setValue(Object value) {
        if (value instanceof Date || value == null) {
            this.text = null;
            super.setValue(value);
        } else {
            throw new IllegalArgumentException("setValue() expected java.util.Date value, got "
                    + value.getClass().getName());
        }
    }

    /**
     * Parse the text into a java.util.Date by trying
     * one by one the registered DateFormat(s).
     *
     * @param text the string date
     */
    public void setAsText(String text) {
        ParseException pe = null;

        for (int i = 0; i < formats.length; i++) {
            try {
                // try to parse the date
                DateFormat df = formats[i];
                Date date = df.parse(text);

                // store the date in both forms
                this.text = text;
                super.setValue(date);

                // done
                return;
            } catch (ParseException e) {
                // remember the last seen exception
                pe = e;
            }
        }
        // couldn't parse
        throw new NestedRuntimeException(pe);
    }

    /**
     * Returns either the cached string date, or the stored
     * java.util.Date instance formated to string using the
     * last of the registered DateFormat(s)
     *
     * @return date as string
     */
    public String getAsText() {
        if (text == null) {
            DateFormat df = formats[formats.length - 1];
            Date date = (Date) getValue();
            text = df.format(date);
        }
        return text;
    }

}