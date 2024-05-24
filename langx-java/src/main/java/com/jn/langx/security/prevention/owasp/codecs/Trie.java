package com.jn.langx.security.prevention.owasp.codecs;


import com.jn.langx.util.Objs;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

interface Trie<T> extends Map<CharSequence, T> {
    Entry<CharSequence, T> getLongestMatch(CharSequence key);

    Entry<CharSequence, T> getLongestMatch(PushbackReader keyIn) throws IOException;

    int getMaxKeyLength();

    class TrieProxy<T> implements Trie<T> {
        private Trie<T> wrapped;

        TrieProxy(Trie<T> toWrap) {
            wrapped = toWrap;
        }

        protected Trie<T> getWrapped() {
            return wrapped;
        }

        public Entry<CharSequence, T> getLongestMatch(CharSequence key) {
            return wrapped.getLongestMatch(key);
        }

        public Entry<CharSequence, T> getLongestMatch(PushbackReader keyIn) throws IOException {
            return wrapped.getLongestMatch(keyIn);
        }

        public int getMaxKeyLength() {
            return wrapped.getMaxKeyLength();
        }

        /* java.util.Map: */

        public int size() {
            return wrapped.size();
        }

        public boolean isEmpty() {
            return wrapped.isEmpty();
        }

        public boolean containsKey(Object key) {
            return wrapped.containsKey(key);
        }

        public boolean containsValue(Object val) {
            return wrapped.containsValue(val);
        }

        public T get(Object key) {
            return wrapped.get(key);
        }

        public T put(CharSequence key, T value) {
            return wrapped.put(key, value);
        }

        public T remove(Object key) {
            return wrapped.remove(key);
        }

        public void putAll(Map<? extends CharSequence, ? extends T> t) {
            wrapped.putAll(t);
        }

        public void clear() {
            wrapped.clear();
        }

        public Set<CharSequence> keySet() {
            return wrapped.keySet();
        }

        public Collection<T> values() {
            return wrapped.values();
        }

        public Set<Entry<CharSequence, T>> entrySet() {
            return wrapped.entrySet();
        }

        public boolean equals(Object other) {
            if(!(other instanceof Trie)){
                return false;
            }
            return Objs.equals(wrapped,other);
        }

        public int hashCode() {
            return wrapped.hashCode();
        }
    }

    class Unmodifiable<T> extends TrieProxy<T> {
        Unmodifiable(Trie<T> toWrap) {
            super(toWrap);
        }

        @Override
        public T put(CharSequence key, T value) {
            throw new UnsupportedOperationException("Unmodifiable Trie");
        }

        public T remove(CharSequence key) {
            throw new UnsupportedOperationException("Unmodifiable Trie");
        }

        @Override
        public void putAll(Map<? extends CharSequence, ? extends T> t) {
            throw new UnsupportedOperationException("Unmodifiable Trie");
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException("Unmodifiable Trie");
        }

        @Override
        public Set<CharSequence> keySet() {
            return Collections.unmodifiableSet(super.keySet());
        }

        @Override
        public Collection<T> values() {
            return Collections.unmodifiableCollection(super.values());
        }

        @Override
        public Set<Entry<CharSequence, T>> entrySet() {
            return Collections.unmodifiableSet(super.entrySet());
        }
    }

    class Util {
        private Util() {
        }

        public static <T> Trie<T> unmodifiable(Trie<T> toWrap) {
            return new Unmodifiable<T>(toWrap);
        }
    }
}
