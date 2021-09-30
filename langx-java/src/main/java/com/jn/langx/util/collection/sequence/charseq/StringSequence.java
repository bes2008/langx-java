package com.jn.langx.util.collection.sequence.charseq;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.PrimitiveArrays;
import com.jn.langx.util.collection.sequence.AbstractCharSequence;
import com.jn.langx.util.function.Consumer2;

import java.util.Collection;
import java.util.List;

public class StringSequence extends AbstractCharSequence<String> {
    @Override
    public boolean add(Character character) {
        this.charSequence = this.charSequence + character;
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends Character> c) {
        this.charSequence = this.charSequence + toString(c);
        return true;
    }

    private String toString(Collection<? extends Character> c) {
        final StringBuilder builder = new StringBuilder(c.size());
        Collects.forEach(c, new Consumer2<Integer, Character>() {
            @Override
            public void accept(Integer key, Character ch) {
                builder.append(ch);
            }
        });
        return builder.toString();
    }


    @Override
    public boolean removeAll(Collection<?> c) {
        StringBuilder builder = new StringBuilder(size());
        for (int i = 0; i < size(); i++) {
            Character ch = this.charSequence.charAt(i);
            if (!c.contains(ch)) {
                builder.append(ch);
            }
        }
        this.charSequence = builder.toString();
        return true;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        StringBuilder builder = new StringBuilder(size());
        for (int i = 0; i < size(); i++) {
            Character ch = this.charSequence.charAt(i);
            if (c.contains(ch)) {
                builder.append(ch);
            }
        }
        this.charSequence = builder.toString();
        return true;
    }

    @Override
    public void clear() {
        this.charSequence = "";
    }

    @Override
    public Character set(int index, Character element) {
        String before = Strings.substring(this.charSequence, 0, index);
        String after = index+1 >= size()? "": Strings.substring(this.charSequence, index+1, size());
        StringBuilder builder = new StringBuilder(size());
        Character ch = this.charSequence.charAt(index);
        builder.append(before)
                .append(element)
                .append(after);
        this.charSequence = builder.toString();
        return ch;
    }

    @Override
    public void add(int index, Character element) {
        String before = Strings.substring(this.charSequence, 0, index);
        String after = Strings.substring(this.charSequence, index, size());
        StringBuilder builder = new StringBuilder(size() +1);
        builder.append(before)
                .append(element)
                .append(after);
        this.charSequence = builder.toString();
    }

    @Override
    public boolean addAll(int index, Collection<? extends Character> c) {
        String before = Strings.substring(this.charSequence, 0, index);
        String after = Strings.substring(this.charSequence, index, size());
        StringBuilder builder = new StringBuilder(size() + c.size());
        builder.append(before)
                .append(toString(c))
                .append(after);
        this.charSequence = builder.toString();
        return true;
    }

    @Override
    public Character remove(int index) {
        String before = Strings.substring(this.charSequence, 0, index);
        String after = Strings.substring(this.charSequence, index, size());
        Character ch = this.charSequence.charAt(index);
        StringBuilder builder = new StringBuilder(size());
        builder.append(before)
                .append(after);
        this.charSequence = builder.toString();
        return ch;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public List<Character> asList() {
        return Collects.asList(PrimitiveArrays.<Character>wrap(charSequence.toCharArray()));
    }

    @Override
    public String toString() {
        return charSequence;
    }
}
