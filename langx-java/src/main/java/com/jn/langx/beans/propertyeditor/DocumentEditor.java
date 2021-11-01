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
import org.jboss.util.xml.DOMWriter;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.StringReader;

/**
 * A property editor for {@link Document}.
 */
public class DocumentEditor extends PropertyEditorSupport {
    /**
     * Returns the property as a String.
     *
     * @throws NestedRuntimeException conversion exception occured
     */
    public String getAsText() {
        return DOMWriter.printNode((Node) getValue(), false);
    }

    /**
     * Sets as an Document created by a String.
     *
     * @throws NestedRuntimeException A parse exception occured
     */
    public void setAsText(String text) {
        setValue(getAsDocument(text));
    }

    protected Document getAsDocument(String text) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(text);
            InputSource is = new InputSource(sr);
            Document d = db.parse(is);
            return d;
        } catch (ParserConfigurationException e) {
            throw new NestedRuntimeException(e);
        } catch (SAXException e) {
            throw new NestedRuntimeException(e);
        } catch (IOException e) {
            throw new NestedRuntimeException(e);
        }
    }
}
