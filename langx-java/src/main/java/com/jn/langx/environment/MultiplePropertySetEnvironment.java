package com.jn.langx.environment;

import com.jn.langx.Named;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Listable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiplePropertySetEnvironment implements Environment, Listable<PropertySet>, Named {
    private String name;
    private final List<PropertySet> propertySets = new CopyOnWriteArrayList<PropertySet>();

    public MultiplePropertySetEnvironment() {
        this("temp");
    }

    public MultiplePropertySetEnvironment(String name) {
        this(name, null);
    }

    public MultiplePropertySetEnvironment(String name, List<PropertySet> propertySets) {
        this.name = name;
        Collects.addTo(propertySets, this.propertySets);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getProperty(String key) {
        return null;
    }

    @Override
    public String getProperty(String key, String valueIfAbsent) {
        return null;
    }

    public boolean addFirst(PropertySet propertySet) {
        propertySets.add(0, propertySet);
        return true;
    }

    @Override
    public boolean add(PropertySet propertySet) {
        propertySets.add(propertySet);
        return true;
    }

    @Override
    public boolean remove(Object e) {
        return propertySets.remove(e);
    }

    public void addBefore(String refPropertySetName, PropertySet propertySet) {
        synchronized (this.propertySets) {
            remove(propertySet);
            int index = findRelativePropertySetIndex(refPropertySetName, propertySet);
            this.propertySets.add(index, propertySet);
        }
    }

    public void addAfter(String refPropertySetName, PropertySet propertySet) {
        synchronized (this.propertySets) {
            remove(propertySet);
            int index = findRelativePropertySetIndex(refPropertySetName, propertySet);
            this.propertySets.add(index + 1, propertySet);
        }
    }

    private int findRelativePropertySetIndex(String refPropertySetName, final PropertySet propertySet) {
        Preconditions.checkNotEmpty(refPropertySetName, "the property set name is empty");
        if (Strings.equals(propertySet.getName(), refPropertySetName)) {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("duplicated property set name: {}", refPropertySetName));
        }
        int index = Collects.firstOccurrence(this.propertySets, propertySet);
        if (index < 0) {
            throw new IllegalArgumentException(StringTemplates.formatWithPlaceholder("non exists property set: {}", refPropertySetName));
        }
        return index;
    }

    @Override
    public boolean removeAll(Collection<?> collection) {
        return propertySets.removeAll(collection);
    }

    @Override
    public void clear() {
        propertySets.clear();
    }

    @Override
    public boolean addAll(Collection<? extends PropertySet> elements) {
        propertySets.addAll(elements);
        return true;
    }

    @Override
    public Iterator<PropertySet> iterator() {
        return propertySets.iterator();
    }

    @Override
    public boolean isEmpty() {
        return propertySets.isEmpty();
    }

    @Override
    public boolean isNull() {
        return isEmpty();
    }
}
