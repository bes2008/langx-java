package com.jn.langx.propertyset;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Listable;
import com.jn.langx.util.function.Predicate;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MultiplePropertySet extends AbstractPropertySet implements Listable<PropertySet> {

    private final List<PropertySet> propertySets = new CopyOnWriteArrayList<PropertySet>();

    public MultiplePropertySet() {
        this("multiple-property-sets");
    }

    public MultiplePropertySet(String name) {
        this(name, null);
    }

    public MultiplePropertySet(String name, List<PropertySet> propertySets) {
        super(name, new Object());
        Collects.addTo(propertySets, this.propertySets);
    }

    @Override
    public Object getProperty(String key) {
        return getProperty(key,null);
    }

    public Object getProperty(String key, String valueIfAbsent) {
        for (PropertySet set : propertySets){
            if(set.containsProperty(key)){
                return set.getProperty(key);
            }
        }
        return valueIfAbsent;
    }

    @Override
    public boolean containsProperty(final String name) {
        return Collects.anyMatch(this.propertySets, new Predicate<PropertySet>() {
            @Override
            public boolean test(PropertySet set) {
                return set.containsProperty(name);
            }
        });
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
