package com.jn.langx.jndi;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

public class JavaBeanContextFactory implements InitialContextFactory {

    @Override
    public Context getInitialContext(Hashtable<?, ?> environment)
            throws NamingException {
        return new JavaBeanContext(environment);
    }

}
