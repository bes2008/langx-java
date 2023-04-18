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

import com.jn.langx.util.logging.Loggers;
import com.jn.langx.util.reflect.type.Primitives;
import org.slf4j.Logger;

import java.beans.*;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

/**
 * A collection of PropertyEditor utilities. Provides the same interface
 * as PropertyEditorManager plus more...
 *
 * <p>Installs the default PropertyEditors.
 */
@SuppressWarnings("unchecked")
public class PropertyEditors {

    /**
     * The null string
     */
    private static final String NULL = "null";

    /**
     * Whether we handle nulls
     */
    private static boolean disableIsNull = false;

    /**
     * Whether or not initialization of the editor search path has been done
     */
    private static boolean initialized = false;

    static {
        init();
    }

    /**
     * Augment the PropertyEditorManager search path to incorporate the JBoss
     * specific editors by appending the org.jboss.util.propertyeditor package
     * to the PropertyEditorManager editor search path.
     */
    public static synchronized  void init() {
        if (!initialized ) {
            AccessController.doPrivileged(Initialize.instance);
            initialized = true;
        }
    }

    /**
     * Whether a string is interpreted as the null value,
     * including the empty string.
     *
     * @param value the value
     * @return true when the string has the value null
     */
    public static final boolean isNull(final String value) {
        return isNull(value, true, true);
    }

    /**
     * Whether a string is interpreted as the null value
     *
     * @param value the value
     * @param trim  whether to trim the string
     * @param empty whether to include the empty string as null
     * @return true when the string has the value null
     */
    public static final boolean isNull(final String value, final boolean trim, final boolean empty) {
        // For backwards compatibility
        if (disableIsNull)
            return false;
        // No value?
        if (value == null)
            return true;
        // Trim the text when requested
        String trimmed = trim ? value.trim() : value;
        // Is the empty string null?
        if (empty && trimmed.length() == 0)
            return true;
        // Just check it.
        return NULL.equalsIgnoreCase(trimmed);
    }

    /**
     * Will the standard editors return null from their
     * {@link PropertyEditor#setAsText(String)} method for non-primitive targets?
     *
     * @return True if nulls can be returned; false otherwise.
     */
    public static boolean isNullHandlingEnabled() {
        return !disableIsNull;
    }

    /**
     * Locate a value editor for a given target type.
     *
     * @param type The class of the object to be edited.
     * @return An editor for the given type or null if none was found.
     */
    public static PropertyEditor findEditor(final Class<?> type) {
        return PropertyEditorManager.findEditor(type);
    }

    /**
     * Locate a value editor for a given target type.
     *
     * @param typeName The class name of the object to be edited.
     * @return An editor for the given type or null if none was found.
     * @throws ClassNotFoundException when the class could not be found
     */
    public static PropertyEditor findEditor(final String typeName)
            throws ClassNotFoundException {
        // see if it is a primitive type first
        Class<?> type = Primitives.get(typeName);
        if (type == null) {
            // nope try look up
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            type = loader.loadClass(typeName);
        }

        return PropertyEditorManager.findEditor(type);
    }

    /**
     * Get a value editor for a given target type.
     *
     * @param type The class of the object to be edited.
     * @return An editor for the given type.
     * @throws RuntimeException No editor was found.
     */
    public static PropertyEditor getEditor(final Class<?> type) {
        PropertyEditor editor = findEditor(type);
        if (editor == null) {
            throw new RuntimeException("No property editor for type: " + type);
        }

        return editor;
    }

    /**
     * Get a value editor for a given target type.
     *
     * @param typeName The class name of the object to be edited.
     * @return An editor for the given type.
     * @throws RuntimeException       No editor was found.
     * @throws ClassNotFoundException when the class is not found
     */
    public static PropertyEditor getEditor(final String typeName)
            throws ClassNotFoundException {
        PropertyEditor editor = findEditor(typeName);
        if (editor == null) {
            throw new RuntimeException("No property editor for type: " + typeName);
        }

        return editor;
    }

    /**
     * Register an editor class to be used to editor values of a given target class.
     *
     * @param type       The class of the objetcs to be edited.
     * @param editorType The class of the editor.
     */
    public static void registerEditor(final Class<?> type, final Class<?> editorType) {
        PropertyEditorManager.registerEditor(type, editorType);
    }

    /**
     * Register an editor class to be used to editor values of a given target class.
     *
     * @param typeName       The classname of the objetcs to be edited.
     * @param editorTypeName The class of the editor.
     * @throws ClassNotFoundException when the class could not be found
     */
    public static void registerEditor(final String typeName,
                                      final String editorTypeName)
            throws ClassNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Class<?> type = loader.loadClass(typeName);
        Class<?> editorType = loader.loadClass(editorTypeName);

        PropertyEditorManager.registerEditor(type, editorType);
    }

    /**
     * Convert a string value into the true value for typeName using the
     * PropertyEditor associated with typeName.
     *
     * @param text     the string represention of the value. This is passed to
     *                 the PropertyEditor.setAsText method.
     * @param typeName the fully qualified class name of the true value type
     * @return the PropertyEditor.getValue() result
     * @throws ClassNotFoundException thrown if the typeName class cannot
     *                                be found
     * @throws IntrospectionException thrown if a PropertyEditor for typeName
     *                                cannot be found
     */
    public static Object convertValue(String text, String typeName)
            throws ClassNotFoundException, IntrospectionException {
        // see if it is a primitive type first
        Class<?> typeClass = Primitives.get(typeName);
        if (typeClass == null) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            typeClass = loader.loadClass(typeName);
        }

        PropertyEditor editor = PropertyEditorManager.findEditor(typeClass);
        if (editor == null) {
            throw new IntrospectionException
                    ("No property editor for type=" + typeClass);
        }

        editor.setAsText(text);
        return editor.getValue();
    }

    /**
     * This method takes the properties found in the given beanProps
     * to the bean using the property editor registered for the property.
     * Any property in beanProps that does not have an associated java bean
     * property will result in an IntrospectionException. The string property
     * values are converted to the true java bean property type using the
     * java bean PropertyEditor framework. If a property in beanProps does not
     * have a PropertyEditor registered it will be ignored.
     *
     * @param bean      - the java bean instance to apply the properties to
     * @param beanProps - map of java bean property name to property value.
     * @throws IntrospectionException thrown on introspection of bean and if
     *                                a property in beanProps does not map to a property of bean.
     */
    public static void mapJavaBeanProperties(Object bean, Properties beanProps)
            throws IntrospectionException {
        mapJavaBeanProperties(bean, beanProps, true);
    }

    /**
     * This method takes the properties found in the given beanProps
     * to the bean using the property editor registered for the property.
     * Any property in beanProps that does not have an associated java bean
     * property will result in an IntrospectionException. The string property
     * values are converted to the true java bean property type using the
     * java bean PropertyEditor framework. If a property in beanProps does not
     * have a PropertyEditor registered it will be ignored.
     *
     * @param bean      - the java bean instance to apply the properties to
     * @param beanProps - map of java bean property name to property value.
     * @param isStrict  - indicates if should throw exception if bean property can not
     *                  be matched.  True for yes, false for no.
     * @throws IntrospectionException thrown on introspection of bean and if
     *                                a property in beanProps does not map to a property of bean.
     */
    public static void mapJavaBeanProperties(Object bean, Properties beanProps, boolean isStrict)
            throws IntrospectionException {

        HashMap<String, PropertyDescriptor> propertyMap = new HashMap<String, PropertyDescriptor>();
        BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
        PropertyDescriptor[] props = beanInfo.getPropertyDescriptors();
        for (int p = 0; p < props.length; p++) {
            String fieldName = props[p].getName();
            propertyMap.put(fieldName, props[p]);
        }
        Logger log = Loggers.getLogger(PropertyEditors.class);
        boolean trace = log.isTraceEnabled();
        Iterator keys = beanProps.keySet().iterator();
        if (trace)
            log.trace("Mapping properties for bean: {}" ,bean);
        while (keys.hasNext()) {
            String name = (String) keys.next();
            String text = beanProps.getProperty(name);
            PropertyDescriptor pd = propertyMap.get(name);
            if (pd == null) {
            /* Try the property name with the first char uppercased to handle
            a property name like dLQMaxResent whose expected introspected
            property name would be DLQMaxResent since the JavaBean
            Introspector would view setDLQMaxResent as the setter for a
            DLQMaxResent property whose Introspector.decapitalize() method
            would also return "DLQMaxResent".
            */
                if (name.length() > 1) {
                    char first = name.charAt(0);
                    String exName = Character.toUpperCase(first) + name.substring(1);
                    pd = propertyMap.get(exName);

                    // Be lenient and check the other way around, e.g. ServerName -> serverName
                    if (pd == null) {
                        exName = Character.toLowerCase(first) + name.substring(1);
                        pd = propertyMap.get(exName);
                    }
                }

                if (pd == null) {
                    if (isStrict) {
                        String msg = "No property found for: " + name + " on JavaBean: " + bean;
                        throw new IntrospectionException(msg);
                    } else {
                        // since is not strict, ignore that this property was not found
                        continue;
                    }
                }
            }
            Method setter = pd.getWriteMethod();
            if (trace)
                log.trace("Property editor found for: {}, editor: {}, setter: {}" , name, pd,setter);
            if (setter != null) {
                Class<?> ptype = pd.getPropertyType();
                PropertyEditor editor = PropertyEditorManager.findEditor(ptype);
                if (editor == null && trace) {
                        log.trace("Failed to find property editor for: {}" , name);
                }
                if (editor != null) {
                    try {
                        editor.setAsText(text);
                        Object args[] = {editor.getValue()};
                        setter.invoke(bean, args);
                    } catch (Exception e) {
                        if (trace) {
                            log.trace("Failed to write property", e);
                        }
                    }
                }
            }
        }
    }

    /**
     * Gets the package names that will be searched for property editors.
     *
     * @return The package names that will be searched for property editors.
     */
    public String[] getEditorSearchPath() {
        return PropertyEditorManager.getEditorSearchPath();
    }

    /**
     * Sets the package names that will be searched for property editors.
     *
     * @param path The serach path.
     */
    public void setEditorSearchPath(final String[] path) {
        PropertyEditorManager.setEditorSearchPath(path);
    }

    private static class Initialize implements PrivilegedAction<Object> {
        static Initialize instance = new Initialize();

        public Object run() {
            String[] currentPath = PropertyEditorManager.getEditorSearchPath();
            int length = currentPath != null ? currentPath.length : 0;
            String[] newPath = new String[length + 2];
            System.arraycopy(currentPath, 0, newPath, 2, length);
            // Put the JBoss editor path first
            // The default editors are not very flexible
            newPath[0] = "org.jboss.util.propertyeditor";
            newPath[1] = "org.jboss.mx.util.propertyeditor";
            PropertyEditorManager.setEditorSearchPath(newPath);
   
         /* Register the editor types that will not be found using the standard
         class name to editor name algorithm. For example, the type String[] has
         a name '[Ljava.lang.String;' which does not map to a XXXEditor name.
         */
            Class<?> strArrayType = String[].class;
            PropertyEditorManager.registerEditor(strArrayType, StringArrayEditor.class);
            Class<?> clsArrayType = Class[].class;
            PropertyEditorManager.registerEditor(clsArrayType, ClassArrayEditor.class);
            Class<?> intArrayType = int[].class;
            PropertyEditorManager.registerEditor(intArrayType, IntArrayEditor.class);
            Class<?> byteArrayType = byte[].class;
            PropertyEditorManager.registerEditor(byteArrayType, ByteArrayEditor.class);

            // There is no default char editor.
            PropertyEditorManager.registerEditor(Character.TYPE, CharacterEditor.class);

            try {
                if (System.getProperty("com.jn.langx.util.null.disabled") != null) {
                    disableIsNull = true;
                }
            } catch (Throwable ignored) {
                Logger log = Loggers.getLogger(PropertyEditors.class);
                log.error("Error retrieving system property com.jn.langx.util.null.disabled", ignored);
            }
            return null;
        }
    }
}
