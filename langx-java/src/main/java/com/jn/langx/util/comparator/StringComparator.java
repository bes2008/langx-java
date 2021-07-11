package com.jn.langx.util.comparator;

import java.io.Serializable;
import java.util.Comparator;

public class StringComparator implements Comparator<String>, Serializable {

    private static final long serialVersionUID = 1L;
    private boolean ignoreCase;

    public StringComparator() {
    }

    public StringComparator(boolean ignoreCase) {
        setIgnoreCase(ignoreCase);
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    @Override
    public int compare(String str1, String str2) {
        return ignoreCase ? str1.compareToIgnoreCase(str2) : str1.compareTo(str2);
    }
}
