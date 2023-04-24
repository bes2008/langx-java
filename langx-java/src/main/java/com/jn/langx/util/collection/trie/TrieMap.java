package com.jn.langx.util.collection.trie;


import java.io.Serializable;
import java.util.*;

/**
 * A trie implementation that can be used as map. This map implementation is
 * especially thought to be used when having char sequence keys. Additionally to
 * the standard methods of the {@link Map} interface, this implementation offers
 * methods to check if a key prefix is contained, get the best matching key from
 * an example key and retrieve prefixed views on the map.
 * <p>
 * This map does not support null keys, instead an empty string should be used.
 * <p>
 * The implementation uses nested {@link HashMap}s with {@link Character} keys
 * internally to ensure <code>O(k)</code> performance where <code>k</code> is
 * the length of an existing key, <code>keyLen</code> is the key which is used
 * for retrieval or modification operations and <code>k &le; keyLen</code>.
 *
 * @param <V> The value type that the trie holds.
 */
public class TrieMap<V extends Serializable> extends AbstractMap<CharSequence, V> implements Serializable, Map<CharSequence, V> {

    private static final long serialVersionUID = 1L;

    private static final class TrieNode<V extends Serializable> implements Serializable {

        private static final long serialVersionUID = 1L;
        private final Map<Character, TrieNode<V>> children;
        private V value;
        private boolean inUse;

        public TrieNode(final V value, final boolean inUse) {
            this.children = new HashMap<Character, TrieNode<V>>();
            this.value = value;
            this.inUse = inUse;
        }

        private TrieNode(final V value, final boolean inUse, int childrenSize) {
            this.children = new HashMap<Character, TrieNode<V>>(childrenSize);
            this.value = value;
            this.inUse = inUse;
        }

        public TrieNode(final boolean inUse) {
            this(null, inUse);
        }

        public TrieNode<V> unset() {
            inUse = false;
            value = null;
            return this;
        }

        public TrieNode<V> cloneDeep() {
            final TrieNode<V> node = new TrieNode<V>(value, inUse, children.size());
            final Map<Character, TrieNode<V>> nodeChildren = node.children;

            for (final Map.Entry<Character, TrieNode<V>> entry : children.entrySet()) {
                nodeChildren.put(entry.getKey(), entry.getValue().cloneDeep());
            }

            return node;
        }
    }

    private final TrieNode<V> root;
    int size;
    /**
     * 修改次数
     */
    transient int modCount;

    /**
     * Constructs an empty TrieMap
     */
    public TrieMap() {
        this(null, true);
    }

    /**
     * Constructs a new TrieMap with the values from the given map.
     *
     * @param map The map from which to construct this TrieMap
     */
    public TrieMap(final Map<CharSequence, ? extends V> map) {
        this(map, false);
        putAll(map);
    }

    /**
     * Constructs a new TrieMap by deep cloning the internally used nodes.
     *
     * @param map The map from which to construct this TrieMap
     */
    public TrieMap(final TrieMap<? extends V> map) {
        this(map, false);
    }

    /**
     * Internally used to encapsulate all initializations.
     *
     * @param map         The map from which to construct this TrieMap or null
     * @param nullAllowed Whether null is allowed or not
     */
    @SuppressWarnings("unchecked")
    private TrieMap(final Map<CharSequence, ? extends V> map, final boolean nullAllowed) {
        if (nullAllowed || !(map instanceof TrieMap<?>)) {
            this.root = new TrieNode<V>(false);
        } else {
            this.root = ((TrieMap<V>) map).root.cloneDeep();
        }

        this.size = 0;
        this.modCount = 0;
    }

    /**
     * This method returns the root element and mainly is for sub map to
     * override.
     *
     * @return The current root node.
     */
    TrieNode<V> getRoot() {
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V put(final CharSequence key, final V value) {
        final CharSequence checkedKey = keyCheck(key);
        final int keyLength = checkedKey.length();
        final V replacedValue;
        TrieNode<V> currentNode = getRoot();
        TrieNode<V> lastNode = null;
        int i = 0;

        while (i < keyLength && currentNode != null) {
            lastNode = currentNode;
            currentNode = currentNode.children.get(checkedKey.charAt(i));
            ++i;
        }

        if (currentNode == null) {
            /* We could not find the node for the given key, so create it */
            currentNode = lastNode;
            final TrieNode<V> newNode = new TrieNode<V>(true);

            addNode(currentNode, checkedKey, --i, newNode);
            modifyData(newNode, value);
            replacedValue = null;
        } else if (currentNode.inUse) {
            /* We found the node and it is in use, so replace the value */
            replacedValue = currentNode.value;

            if (replacedValue != value && (replacedValue == null || !replacedValue.equals(value))) {
                currentNode.value = value;
            }
        } else {
            /* We found a node that is not in use, so just set the value */
            modifyData(currentNode, value);
            currentNode.inUse = true;
            replacedValue = null;
        }

        return replacedValue;
    }

    /**
     * Sets the given value as the new value on the given node, increases size
     * and modCount.
     */
    void modifyData(final TrieNode<V> node, final V value) {
        node.value = value;
        ++modCount;
        ++size;
    }

    /**
     * Adds the given new node to the node with the given key beginning at
     * beginIndex.
     */
    private void addNode(final TrieNode<V> node, final CharSequence key,
                         final int beginIndex, final TrieNode<V> newNode) {
        final int lastKeyIndex = key.length() - 1;
        TrieNode<V> currentNode = node;
        int i = beginIndex;

        for (; i < lastKeyIndex; i++) {
            final TrieNode<V> nextNode = new TrieNode<V>(false);
            currentNode.children.put(key.charAt(i), nextNode);
            currentNode = nextNode;
        }

        currentNode.children.put(key.charAt(i), newNode);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V get(final Object key) {
        final TrieNode<V> node = findNode(keyCheck(key));
        return node == null ? null : node.value;
    }

    private TrieNode<V> findNode(final CharSequence key) {
        final int strLen = key.length();
        TrieNode<V> currentNode = getRoot();

        for (int i = 0; i < strLen && currentNode != null; i++) {
            currentNode = currentNode.children.get(key.charAt(i));
        }

        return currentNode;
    }

    public String getBestMatch(final CharSequence str) {
        final int strLen = str.length();
        TrieNode<V> curNode = getRoot();
        int i = 0;

        for (; i < strLen && curNode != null; i++) {
            curNode = curNode.children.get(str.charAt(i));
        }

        return new StringBuilder(i - 1).append(str, 0, i - 1).toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(final Object key) {
        final TrieNode<V> node = findNode(keyCheck(key));
        return node != null && node.inUse;
    }

    /**
     * Returns true when an entry exists that that has the given prefix.
     *
     * @param prefix The prefix for which to check if an entry is contained.
     * @return True when an entry with the given prefix exists, otherwise false.
     */
    public boolean containsKeyPrefix(final CharSequence prefix) {
        return findNode(keyCheck(prefix)) != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V remove(final Object o) {
        final CharSequence key = keyCheck(o);
        final TrieNode<V> currentNode = findPreviousNode(key);

        if (currentNode == null) {
            /* Node not found for the given key */
            return null;
        }

        final TrieNode<V> node = currentNode.children.get(key.charAt(key
                .length() - 1));

        if (node == null || !node.inUse) {
            /* Node not found for the given key or is not in use */
            return null;
        }

        final V removed = node.value;
        node.unset();
        --size;
        ++modCount;

        if (node.children.isEmpty()) {
            /* Since there are no children left, we can compact the trie */
            compact(key);
        }

        return removed;
    }

    private TrieNode<V> findPreviousNode(final CharSequence key) {
        final int lastKeyIndex = key.length() - 1;
        TrieNode<V> currentNode = getRoot();

        for (int i = 0; i < lastKeyIndex && currentNode != null; i++) {
            currentNode = currentNode.children.get(key.charAt(i));
        }

        return currentNode;
    }

    /**
     * Special version of remove for EntrySet.
     */
    V removeMapping(final Object o) {
        if (!(o instanceof Map.Entry)) {
            throw new IllegalArgumentException();
        }

        @SuppressWarnings("unchecked") final Entry<? extends CharSequence, V> e = (Map.Entry<? extends CharSequence, V>) o;
        final CharSequence key = keyCheck(e.getKey());
        final V value = e.getValue();
        final TrieNode<V> currentNode = findPreviousNode(key);

        if (currentNode == null) {
            /* Node not found for the given key */
            return null;
        }

        final TrieNode<V> node = currentNode.children.get(key.charAt(key
                .length() - 1));

        if (node == null || !node.inUse) {
            /* Node not found for the given key or is not in use */
            return null;
        }

        final V removed = node.value;

        if (removed != value && (removed == null || !removed.equals(value))) {
            /*
             * Value in the map differs from the value given in the entry so do
             * nothing and return null.
             */
            return null;
        }

        node.unset();
        --size;
        ++modCount;

        if (node.children.isEmpty()) {
            /* Since there are no children left, we can compact the trie */
            compact(key);
        }

        return removed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        final TrieNode<V> rootNode = root;
        rootNode.children.clear();
        rootNode.unset();
        ++modCount;
        size = 0;
    }

    /**
     * Compact the trie by removing unused nodes on the path that is specified
     * by the given key.
     */
    private void compact(final CharSequence key) {
        final int keyLength = key.length();
        TrieNode<V> currentNode = getRoot();
        TrieNode<V> lastInUseNode = currentNode;
        int lastInUseIndex = 0;

        if(currentNode!=null) {
            for (int i = 0; i < keyLength && currentNode != null; i++) {
                if (currentNode.inUse) {
                    lastInUseNode = currentNode;
                    lastInUseIndex = i;
                }

                currentNode = currentNode.children.get(key.charAt(i));
            }
        }
        currentNode = lastInUseNode;

        if(currentNode!=null) {
            for (int i = lastInUseIndex; i < keyLength; i++) {
                currentNode = currentNode.children.remove(key.charAt(i)).unset();
            }
        }
    }

    private static CharSequence keyCheck(final Object key) {
        if (key == null) {
            throw new IllegalArgumentException(
                    "This map does not support null keys");
        } else if (!(key instanceof CharSequence)) {
            throw new IllegalArgumentException(
                    "Argument must be instance of CharSequence");
        }

        return (CharSequence) key;
    }

    private static CharSequence keyCheck(final CharSequence key) {
        if (key == null) {
            throw new IllegalArgumentException("This map does not support null keys");
        }

        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /*
     * Iterators
     */

    /**
     * Entry implementation for TrieMap.
     */
    private final class TrieEntry implements Entry<CharSequence, V> {

        private final CharSequence key;
        private final TrieNode<V> node;

        public TrieEntry(final CharSequence key, final TrieNode<V> node) {
            this.key = key;
            this.node = node;
        }

        @Override
        public final CharSequence getKey() {
            return key;
        }

        @Override
        public final V getValue() {
            return node.value;
        }

        @Override
        public final V setValue(final V value) {
            final V oldValue = node.value;
            node.value = value;
            return oldValue;
        }

        @Override
        public int hashCode() {
            final Object k = this.key;
            final Object v = this.node.value;
            int hash = 7;
            hash = 37 * hash + (k != null ? k.hashCode() : 0);
            hash = 37 * hash + (v != null ? v.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Map.Entry<?, ?>)) {
                return false;
            }
            final Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;

            final Object k1 = this.key;
            final Object k2 = other.getKey();

            if (k1 != k2 && (k1 == null || !k1.equals(k2))) {
                return false;
            }

            final Object v1 = this.node.value;
            final Object v2 = other.getValue();

            if (v1 != v2 && (v1 == null || !v1.equals(v2))) {
                return false;
            }

            return true;
        }
    }

    /**
     * Iterator implementation for TrieMap.
     *
     * @param <E> The type of the entry
     */
    private abstract class TrieIterator<E> implements Iterator<E> {

        protected int expectedModCount;
        private final Deque<TrieEntry> deque;
        private Entry<CharSequence, V> next;
        private Entry<CharSequence, V> current;

        public TrieIterator() {
            this(getRoot(), "");
        }

        public TrieIterator(final TrieNode<V> startNode, final CharSequence key) {
            expectedModCount = modCount;
            deque = new ArrayDeque<TrieEntry>();
            deque.add(new TrieEntry(key, startNode));
            fetchEntry();
        }

        private void fetchEntry() {
            final Deque<TrieEntry> localDeque = deque;
            TrieEntry localNext = null;

            while (localNext == null && !localDeque.isEmpty()) {
                final TrieEntry tempEntry = localDeque.removeFirst();
                final CharSequence key = tempEntry.key;
                final TrieNode<V> node = tempEntry.node;
                final StringBuilder sb = new StringBuilder(key.length() + 1);
                sb.append(key).append(' ');

                if (node.inUse) {
                    localNext = tempEntry;
                }

                for (final Entry<Character, TrieNode<V>> entry : node.children
                        .entrySet()) {
                    sb.setCharAt(key.length(), entry.getKey());
                    localDeque.addFirst(new TrieEntry(sb.toString(), entry
                            .getValue()));
                }
            }

            next = localNext;
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        public Entry<CharSequence, V> nextEntry() {
            if (modCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            final Entry<CharSequence, V> entry = next;
            current = entry;

            if (entry == null) {
                throw new NoSuchElementException();
            }

            fetchEntry();

            return entry;
        }

        @Override
        public void remove() {
            final Entry<CharSequence, V> entry = current;

            if (entry == null) {
                throw new IllegalStateException();
            }

            final int localModCount = modCount;

            if (localModCount != expectedModCount) {
                throw new ConcurrentModificationException();
            }

            TrieMap.this.remove(entry.getKey());
            expectedModCount = localModCount;
        }
    }

    private final class KeyIterator extends TrieIterator<CharSequence> {

        @Override
        public CharSequence next() {
            return nextEntry().getKey();
        }
    }

    private final class EntryIterator extends
            TrieIterator<Entry<CharSequence, V>> {

        @Override
        public Entry<CharSequence, V> next() {
            return nextEntry();
        }
    }

    private final class ValueIterator extends TrieIterator<V> {

        @Override
        public V next() {
            return nextEntry().getValue();
        }
    }

    /*
     * Views
     */
    private transient Set<CharSequence> keySet;
    private transient Collection<V> values;
    private transient Set<Entry<CharSequence, V>> entrySet;

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<CharSequence> keySet() {
        final Set<CharSequence> ks = keySet;
        return ks != null ? ks : (keySet = new KeySet());
    }

    private final class KeySet extends AbstractSet<CharSequence> {

        @Override
        public Iterator<CharSequence> iterator() {
            return new KeyIterator();
        }

        @Override
        public int size() {
            return TrieMap.this.size();
        }

        @Override
        public boolean contains(final Object o) {
            return containsKey(o);
        }

        @Override
        public boolean remove(final Object o) {
            return TrieMap.this.remove(o) != null;
        }

        @Override
        public void clear() {
            TrieMap.this.clear();
        }
    }

    @Override
    public Set<Entry<CharSequence, V>> entrySet() {
        final Set<Entry<CharSequence, V>> es = entrySet;
        return es != null ? es : (entrySet = new EntrySet());
    }

    /**
     * {@inheritDoc}
     */
    private final class EntrySet extends AbstractSet<Entry<CharSequence, V>> {

        @Override
        public void clear() {
            TrieMap.this.clear();
        }

        @Override
        public boolean contains(final Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }

            @SuppressWarnings("unchecked") final Map.Entry<CharSequence, V> e = (Map.Entry<CharSequence, V>) (Map.Entry<?, ?>) o;
            final V value = get(e.getKey());
            return value != null && value.equals(e.getValue());
        }

        @Override
        public Iterator<Entry<CharSequence, V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public boolean remove(final Object o) {
            return removeMapping(o) != null;
        }

        @Override
        public int size() {
            return TrieMap.this.size();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<V> values() {
        final Collection<V> v = values;
        return v != null ? v : (values = new Values());
    }

    private final class Values extends AbstractCollection<V> {

        @Override
        public void clear() {
            TrieMap.this.clear();
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean contains(final Object o) {
            return TrieMap.this.containsValue((V) o);
        }

        @Override
        public Iterator<V> iterator() {
            return new ValueIterator();
        }

        @Override
        public int size() {
            return TrieMap.this.size();
        }
    }

    /**
     * A special implementation of TrieMap that gives a prefixed view on an
     * existing TrieMap.
     *
     * @param <V> The value type that the trie holds.
     */
    private static class SubTrieMap<V extends Serializable> extends TrieMap<V> {

        private static final long serialVersionUID = 1;

        private TrieNode<V> subRootNode;
        private TrieMap<V> parent;
        private final CharSequence prefix;

        public SubTrieMap(final TrieMap<V> parent, final CharSequence prefix) {
            this.parent = parent;
            this.prefix = prefix;
            this.modCount = -1;
        }

        private void ensureLatest() {
            final int parentModCount = parent.modCount;

            /*
             * The submap is maybe ahead of the parent, e.g. in remove(), so
             * just update if modCount is lower than parent modCount
             */
            if (modCount < parentModCount) {
                modCount = parentModCount;
                this.size = size(subRootNode = parent.findNode(prefix));
            }
        }

        private int size(final TrieNode<V> node) {
            if (node == null) {
                return 0;
            }

            int newSize = 0;

            if (node.inUse) {
                ++newSize;
            }

            for (final Map.Entry<Character, TrieNode<V>> entry : node.children
                    .entrySet()) {
                newSize += size(entry.getValue());
            }

            return newSize;
        }

        @Override
        TrieNode<V> getRoot() {
            ensureLatest();
            return subRootNode;
        }

        @Override
        public V put(CharSequence key, V value) {
            ensureLatest();

            if (subRootNode == null) {
                final CharSequence localPrefix = prefix;
                return parent.put(
                        new StringBuilder(localPrefix.length() + key.length())
                                .append(localPrefix).append(key).toString(),
                        value);
            } else {
                return super.put(key, value);
            }
        }

        @Override
        void modifyData(final TrieNode<V> curNode, final V value) {
            /*
             * For replace and add we can skip to update subRootNode, so adjust
             * size and modCount
             */
            parent.modifyData(curNode, value);
            ++modCount;
            ++size;
        }

        @Override
        public V remove(final Object o) {
            ensureLatest();
            final CharSequence key = keyCheck(o);

            if (key.length() == 0) {
                final CharSequence localPrefix = prefix;
                /*
                 * We delegate the remove to the parent when we would try to
                 * remove the root of this sub map
                 */
                return parent.remove(new StringBuilder(localPrefix.length()
                        + key.length()).append(localPrefix).append(key)
                        .toString());
            } else {
                final int capturedModCount = modCount;
                final V removed = super.remove(key);

                if (capturedModCount != modCount) {
                    /* If we really removed something, adjust parent */
                    final TrieMap<V> parentMap = parent;
                    ++parentMap.modCount;
                    --parentMap.size;
                }

                return removed;
            }
        }

        @Override
        V removeMapping(final Object o) {
            final CharSequence key = keyCheck(o);

            if (key.length() == 0) {
                final CharSequence localPrefix = prefix;
                /*
                 * We delegate the remove to the parent when we would try to
                 * remove the root of this sub map
                 */
                return parent.removeMapping(new StringBuilder(localPrefix
                        .length() + key.length()).append(localPrefix)
                        .append(key).toString());
            } else {
                final int capturedModCount = modCount;
                final V removed = super.removeMapping(key);

                if (capturedModCount != modCount) {
                    /* If we really removed something, adjust parent */
                    final TrieMap<V> parentMap = parent;
                    ++parentMap.modCount;
                    --parentMap.size;
                }

                return removed;
            }
        }

        @Override
        public void clear() {
            ensureLatest();
            final int oldSize = this.size;

            super.clear();
            subRootNode.unset();

            final TrieMap<V> parentMap = parent;
            parentMap.size -= oldSize;
            ++parentMap.modCount;
        }

        @Override
        public int size() {
            ensureLatest();
            return super.size();
        }

        @Override
        public TrieMap<V> subMap(final CharSequence prefix) {
            ensureLatest();
            final CharSequence localPrefix = this.prefix;
            return parent.subMap(new StringBuilder(localPrefix.length()
                    + prefix.length()).append(localPrefix).append(prefix)
                    .toString());
        }
    }

    /**
     * Returns a view on the current map that acts like if every method call to
     * the current map where a key is involved would be prefixed with the given
     * prefix. Although the implementation behaves like described, it makes the
     * calls in a more performant way.
     *
     * @param prefix The prefix which to use for the sub map.
     * @return A prefixed view on the current map.
     */
    public TrieMap<V> subMap(final CharSequence prefix) {
        return new SubTrieMap<V>(this, keyCheck(prefix));
    }
}
