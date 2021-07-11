package com.jn.langx.jndi;

import javax.naming.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings({"rawtypes", "unchecked"})
public class JavaBeanContext implements Context {

    private Hashtable env = null;

    public JavaBeanContext() {
        this.env = new Hashtable();
    }

    public JavaBeanContext(Hashtable hashtable) {
        if (hashtable == null) {
            new JavaBeanContext();
        } else {
            this.env = new Hashtable(hashtable);
        }
    }

    private ConcurrentHashMap<Name, Object> map = new ConcurrentHashMap<Name, Object>(
            10);

    @Override
    public Object lookup(Name name) throws NamingException {
        if (name == null)
            throw new NamingException("name is null.");
        return map.get(name);
    }

    @Override
    public Object lookup(String name) throws NamingException {
        return lookup(new CompositeName(name));
    }

    @Override
    public void bind(Name name, Object obj) throws NamingException {
        judgeName(name);
        if (map.containsKey(name)) {
            throw new NamingException("name is exist.");
        }
        map.put(name, obj);
    }

    @Override
    public void bind(String name, Object obj) throws NamingException {
        bind(new CompositeName(name), obj);
    }

    @Override
    public void rebind(Name name, Object obj) throws NamingException {
        judgeName(name);
        if (!map.containsKey(name)) {
            bind(name, obj);
        } else {
            map.put(name, obj);
        }
    }

    @Override
    public void rebind(String name, Object obj) throws NamingException {
        rebind(new CompositeName(name), obj);
    }

    @Override
    public void unbind(Name name) throws NamingException {
        judgeName(name);
        if (map.containsKey(name)) {
            map.remove(name);
            // map.put(name, null);
        }
    }

    @Override
    public void unbind(String name) throws NamingException {
        unbind(new CompositeName(name));
    }

    @Override
    public void rename(Name oldName, Name newName) throws NamingException {
        judgeName(oldName);
        Object value = map.remove(oldName);
        bind(newName, value);
    }

    @Override
    public void rename(String oldName, String newName) throws NamingException {
        rename(new CompositeName(oldName), new CompositeName(newName));
    }

    @Override
    public NamingEnumeration<NameClassPair> list(Name name)
            throws NamingException {
        return new NamingEnumeration<NameClassPair>() {
            private Enumeration<Name> enumer = map.keys();

            @Override
            public boolean hasMoreElements() {
                return enumer.hasMoreElements();
            }

            @Override
            public NameClassPair nextElement() {
                Name name = enumer.nextElement();
                Object value = map.get(name);
                return new NameClassPair(name.toString(), value.getClass()
                        .getName());
            }

            @Override
            public NameClassPair next() throws NamingException {
                return nextElement();
            }

            @Override
            public boolean hasMore() throws NamingException {
                return hasMoreElements();
            }

            @Override
            public void close() throws NamingException {
            }
        };
    }

    @Override
    public NamingEnumeration<NameClassPair> list(String name)
            throws NamingException {
        return list(new CompositeName());
    }

    @Override
    public NamingEnumeration<Binding> listBindings(Name name)
            throws NamingException {
        return new NamingEnumeration<Binding>() {
            private Enumeration<Name> enumer = map.keys();

            @Override
            public boolean hasMoreElements() {
                return enumer.hasMoreElements();
            }

            @Override
            public Binding nextElement() {
                Name name = enumer.nextElement();
                return new Binding(name.toString(), map.get(name));
            }

            @Override
            public Binding next() throws NamingException {
                return nextElement();
            }

            @Override
            public boolean hasMore() throws NamingException {
                return hasMoreElements();
            }

            @Override
            public void close() throws NamingException {
            }
        };
    }

    @Override
    public NamingEnumeration<Binding> listBindings(String name)
            throws NamingException {
        return listBindings(new CompositeName(name));
    }

    @Override
    public void destroySubcontext(Name name) throws NamingException {
    }

    @Override
    public void destroySubcontext(String name) throws NamingException {
    }

    @Override
    public Context createSubcontext(Name name) throws NamingException {
        return null;
    }

    @Override
    public Context createSubcontext(String name) throws NamingException {
        return null;
    }

    @Override
    public Object lookupLink(Name name) throws NamingException {
        return null;
    }

    @Override
    public Object lookupLink(String name) throws NamingException {
        return null;
    }

    @Override
    public NameParser getNameParser(Name name) throws NamingException {
        return null;
    }

    @Override
    public NameParser getNameParser(String name) throws NamingException {
        return null;
    }

    @Override
    public Name composeName(Name name, Name prefix) throws NamingException {
        return null;
    }

    @Override
    public String composeName(String name, String prefix)
            throws NamingException {
        return null;
    }

    @Override
    public Object addToEnvironment(String propName, Object propVal)
            throws NamingException {
        env.put(propName, propVal);
        return propVal;
    }

    @Override
    public Object removeFromEnvironment(String propName) throws NamingException {
        return env.remove(propName);
    }

    @Override
    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return new Hashtable<Object, Object>(env);
    }

    @Override
    public void close() throws NamingException {
        map.clear();
    }

    @Override
    public String getNameInNamespace() throws NamingException {
        return null;
    }

    public void judgeName(Object name) throws NamingException {
        if (name == null)
            throw new NamingException("name is null.");
    }
}
