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
import java.util.StringTokenizer;

/**
 * A property editor for int[].
 */
public class IntArrayEditor extends PropertyEditorSupport {
    /**
     * Build a int[] from comma or eol seperated elements
     */
    public void setAsText(final String text) {
        StringTokenizer tokenizer = new StringTokenizer(text, ",\r\n");
        int[] theValue = new int[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            theValue[i++] = Integer.decode(tokenizer.nextToken()).intValue();
        }
        setValue(theValue);
    }

    /**
     * @return a comma seperated string of the array elements
     */
    public String getAsText() {
        int[] theValue = (int[]) getValue();
        StringBuffer text = new StringBuffer();
        int length = theValue == null ? 0 : theValue.length;
        for (int n = 0; n < length; n++) {
            if (n > 0)
                text.append(',');
            text.append(theValue[n]);
        }
        return text.toString();
    }
}
