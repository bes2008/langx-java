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

/**
 * A property editor for {@link String}.
 * <p>
 * It is really a no-op but it is hoped to provide
 * slightly better performance, by avoiding the continuous
 * lookup/failure of a property editor for plain Strings
 * within the org.jboss.util.propertyeditor package,
 * before falling back to the jdk provided String editor.
 */
public class StringEditor extends PropertyEditorSupport {
    /**
     * Keep the provided String as is.
     */
    @Override
    public void setAsText(String text) {
        setValue(text);
    }
}
