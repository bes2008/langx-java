package com.jn.langx.util.comparator;

import com.jn.langx.util.Chars;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author jinuo.fang
 */
public class CharComparator implements Comparator<Character>, Serializable {
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(Character c1, Character c2) {
        return Chars.compare(c1, c2);
    }
}
