package com.jn.langx.util.collection;

import java.util.*;

public class LinkedCaseInsensitiveSet extends LinkedHashSet<String> {

    private static final long serialVersionUID = 1L;
    private final Locale locale;
    private Map<String, String> cache = new HashMap<String, String>();

    /**
     * Constructs a new, empty linked hash set with the default initial
     * capacity (16) and load factor (0.75).
     */
    public LinkedCaseInsensitiveSet() {
        super();
        this.locale = Locale.getDefault();
    }

    public LinkedCaseInsensitiveSet(Locale locale) {
        super();
        this.locale = (locale != null ? locale : Locale.getDefault());
    }

    /**
     * Constructor initializing the set with the given collection.
     *
     * @param source The source collection to use for initialization.
     */
    public LinkedCaseInsensitiveSet(Locale locale, Collection<? extends String> source) {
        this(locale);
        addAll(source);
    }

    @Override
    public boolean add(String element) {
        String lowercase = element.toLowerCase(this.locale);
        if (!cache.containsKey(lowercase)) {
            super.add(element);
            cache.put(lowercase, element);
            return true;
        }
        return false;
    }

    /**
     * Verify containment by ignoring case.
     */
    public boolean contains(String element) {
        String lowercase = element.toLowerCase(this.locale);
        return cache.containsKey(lowercase);
    }

    @Override
    public boolean contains(Object o) {
        return contains(o.toString());
    }

    @Override
    public boolean remove(Object o) {
        String lowercase = o.toString().toLowerCase(this.locale);
        String value = cache.remove(lowercase);
        if (value != null) {
            return super.remove(value);
        }
        return false;
    }

    @Override
    public void clear() {
        super.clear();
        cache.clear();
    }
}