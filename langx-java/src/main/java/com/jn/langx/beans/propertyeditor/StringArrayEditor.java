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

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;

/**
 * A property editor for String[]. The text format of a string array is a
 * comma or \n, \r seperated list with \, representing an escaped comma to
 * include in the string element.
 */
public class StringArrayEditor
        extends PropertyEditorSupport {

    static String[] parseList(String text) {
        ArrayList<String> list = new ArrayList<String>();
        StringBuffer tmp = new StringBuffer();
        for (int n = 0; n < text.length(); n++) {
            char c = text.charAt(n);
            switch (c) {
                case '\\':
                    tmp.append(c);
                    if (n < text.length() && text.charAt(n + 1) == ',') {
                        tmp.setCharAt(tmp.length() - 1, ',');
                        n++;
                    }
                    break;
                case ',':
                case '\n':
                case '\r':
                    if (tmp.length() > 0)
                        list.add(tmp.toString());
                    tmp.setLength(0);
                    break;
                default:
                    tmp.append(c);
                    break;
            }
        }
        if (tmp.length() > 0)
            list.add(tmp.toString());

        String[] x = new String[list.size()];
        list.toArray(x);
        return x;
    }

    /**
     * Build a String[] from comma or eol seperated elements with a \,
     * representing a ',' to include in the current string element.
     */
    public void setAsText(final String text) {
        String[] theValue = parseList(text);
        setValue(theValue);
    }

    /**
     * @return a comma seperated string of the array elements
     */
    public String getAsText() {
        String[] theValue = (String[]) getValue();
        StringBuffer text = new StringBuffer();
        int length = theValue == null ? 0 : theValue.length;
        for (int n = 0; n < length; n++) {
            String s = theValue[n];
            if (s.equals(","))
                text.append('\\');
            text.append(s);
            text.append(',');
        }
        // Remove the trailing ','
        if (text.length() > 0)
            text.setLength(text.length() - 1);
        return text.toString();
    }
}
