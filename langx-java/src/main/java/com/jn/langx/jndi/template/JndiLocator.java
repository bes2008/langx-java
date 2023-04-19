package com.jn.langx.jndi.template;


import java.util.Properties;
import javax.naming.NamingException;

import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

/**
 * Convenient superclass for JNDI accessors, providing "jndiTemplate"
 * and "jndiEnvironment" bean properties.
 *
 * <p>Note that this implementation is an almost exact combined copy of the Spring Framework's 'JndiAccessor' and
 * 'JndiLocatorSupport' classes from their 2.5.4 distribution - we didn't want to re-invent the wheel, but not require
 * a full dependency on the Spring framework, nor does Spring make available only its JNDI classes in a small jar, or
 * we would have used that.
 *
 * @see #setJndiTemplate
 * @see #setJndiEnvironment
 * @see #setResourceRef
 * @since 2.10.0
 */
public class JndiLocator {

    /**
     * JNDI prefix used in a J2EE container
     */
    public static final String CONTAINER_PREFIX = "java:comp/env/";

    private boolean resourceRef = false;

    private JndiTemplate jndiTemplate = new JndiTemplate();


    /**
     * Set the JNDI template to use for JNDI lookups.
     * <p>You can also specify JNDI environment settings via "jndiEnvironment".
     *
     * @see #setJndiEnvironment
     */
    public void setJndiTemplate(JndiTemplate jndiTemplate) {
        this.jndiTemplate = (jndiTemplate != null ? jndiTemplate : new JndiTemplate());
    }

    /**
     * Return the JNDI template to use for JNDI lookups.
     */
    public JndiTemplate getJndiTemplate() {
        return this.jndiTemplate;
    }

    /**
     * Set the JNDI environment to use for JNDI lookups.
     * <p>Creates a JndiTemplate with the given environment settings.
     *
     * @see #setJndiTemplate
     */
    public void setJndiEnvironment(Properties jndiEnvironment) {
        this.jndiTemplate = new JndiTemplate(jndiEnvironment);
    }

    /**
     * Return the JNDI environment to use for JNDI lookups.
     */
    public Properties getJndiEnvironment() {
        return this.jndiTemplate.getEnvironment();
    }

    /**
     * Set whether the lookup occurs in a J2EE container, i.e. if the prefix
     * "java:comp/env/" needs to be added if the JNDI name doesn't already
     * contain it. Default is "false".
     * <p>Note: Will only get applied if no other scheme (e.g. "java:") is given.
     */
    public void setResourceRef(boolean resourceRef) {
        this.resourceRef = resourceRef;
    }

    /**
     * Return whether the lookup occurs in a J2EE container.
     */
    public boolean isResourceRef() {
        return this.resourceRef;
    }


    /**
     * Perform an actual JNDI lookup for the given name via the JndiTemplate.
     * <p>If the name doesn't begin with "java:comp/env/", this prefix is added
     * if "resourceRef" is set to "true".
     *
     * @param jndiName the JNDI name to look up
     * @return the obtained object
     * @throws javax.naming.NamingException if the JNDI lookup failed
     * @see #setResourceRef
     */
    protected Object lookup(String jndiName) throws NamingException {
        return lookup(jndiName, null);
    }

    /**
     * Perform an actual JNDI lookup for the given name via the JndiTemplate.
     * <p>If the name doesn't begin with "java:comp/env/", this prefix is added
     * if "resourceRef" is set to "true".
     *
     * @param jndiName     the JNDI name to look up
     * @param requiredType the required type of the object
     * @return the obtained object
     * @throws NamingException if the JNDI lookup failed
     * @see #setResourceRef
     */
    protected Object lookup(String jndiName, Class requiredType) throws NamingException {
        if (jndiName == null) {
            throw new IllegalArgumentException("jndiName argument must not be null");
        }
        String convertedName = convertJndiName(jndiName);
        Object jndiObject;
        Logger log = Loggers.getLogger(getClass());
        try {
            jndiObject = getJndiTemplate().lookup(convertedName, requiredType);
        }
        catch (NamingException ex) {
            if (!convertedName.equals(jndiName)) {
                // Try fallback to originally specified name...
                if (log.isDebugEnabled()) {
                    log.debug("Converted JNDI name [{}] not found - trying original name [{}]. " ,convertedName,jndiName,ex);
                }
                jndiObject = getJndiTemplate().lookup(jndiName, requiredType);
            } else {
                throw ex;
            }
        }
        log.debug("Located object with JNDI name '{}'", convertedName);
        return jndiObject;
    }

    /**
     * Convert the given JNDI name into the actual JNDI name to use.
     * <p>The default implementation applies the "java:comp/env/" prefix if
     * "resourceRef" is "true" and no other scheme (e.g. "java:") is given.
     *
     * @param jndiName the original JNDI name
     * @return the JNDI name to use
     * @see #CONTAINER_PREFIX
     * @see #setResourceRef
     */
    protected String convertJndiName(String jndiName) {
        // Prepend container prefix if not already specified and no other scheme given.
        if (isResourceRef() && !jndiName.startsWith(CONTAINER_PREFIX) && jndiName.indexOf(':') == -1) {
            jndiName = CONTAINER_PREFIX + jndiName;
        }
        return jndiName;
    }

}
