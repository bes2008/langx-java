package com.jn.langx.util.collection;

import com.jn.langx.Ordered;
import com.jn.langx.util.Maths;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.multivalue.CommonMultiValueMap;
import com.jn.langx.util.collection.multivalue.MultiValueMap;
import com.jn.langx.util.function.*;
import com.jn.langx.util.struct.Holder;
import com.jn.langx.util.struct.counter.SimpleIntegerCounter;

import java.util.*;

public class OrderedList<E extends Ordered> extends AbstractList<E> implements Cloneable {
    /**
     * key: order
     */
    private MultiValueMap<Integer, E> map;

    public OrderedList() {
        this(new Supplier<Integer, Collection<E>>() {
            @Override
            public Collection<E> get(Integer input) {
                return new ArrayList<E>();
            }
        });
    }

    public OrderedList(Supplier<Integer, Collection<E>> valuesSupplier) {
        map = new CommonMultiValueMap<Integer, E>(new TreeMap<Integer, Collection<E>>(), valuesSupplier);
    }

    @Override
    public E get(final int index) {
        if (index < 0 || index >= map.total()) {
            throw new IndexOutOfBoundsException();
        }
        com.jn.langx.util.struct.Entry<Integer, Integer> coordinate = getCoordinate(index);
        return Collects.asList(map.get(coordinate.getKey())).get(coordinate.getValue());
    }

    private com.jn.langx.util.struct.Entry<Integer, Integer> getCoordinate(final int index) {
        if (index < 0 || index >= map.total()) {
            throw new IndexOutOfBoundsException();
        }
        final SimpleIntegerCounter counter = new SimpleIntegerCounter();
        final Holder<com.jn.langx.util.struct.Entry<Integer, Integer>> holder = new Holder<com.jn.langx.util.struct.Entry<Integer, Integer>>();
        Collects.forEach(map, new Predicate2<Integer, Collection<E>>() {
            @Override
            public boolean test(Integer order, Collection<E> values) {
                int sum = counter.get() + values.size();
                if (sum < index) {
                    counter.increment(values.size());
                    return false;
                }
                return true;
            }
        }, new Consumer2<Integer, Collection<E>>() {
            @Override
            public void accept(Integer order, Collection<E> values) {
                // 找到了
                int offset = index - counter.get();
                holder.set(new com.jn.langx.util.struct.Entry<Integer, Integer>(order, offset));
            }
        }, new Predicate2<Integer, Collection<E>>() {
            @Override
            public boolean test(Integer order, Collection<E> values) {
                return !holder.isNull();
            }
        });
        return holder.get();
    }

    @Override
    public E set(int index, E element) {
        E e1 = remove(index);
        add(element);
        return e1;
    }

    @Override
    public boolean add(E e) {
        if (e == null) {
            return false;
        }
        this.map.add(e.getOrder(), e);
        return true;
    }

    @Override
    public void add(int index, E element) {
        add(element);
    }

    @Override
    public E remove(int index) {
        com.jn.langx.util.struct.Entry<Integer, Integer> coordinate = getCoordinate(index);
        Collection<E> values = this.map.get(coordinate.getKey());
        E e1 = Collects.asList(values).get(coordinate.getValue());
        values.remove(e1);
        return e1;
    }

    @Override
    public boolean remove(Object o) {
        if (!(o instanceof Ordered)) {
            return false;
        }
        E e = (E) o;
        Collection<E> values = this.map.get(e.getOrder());
        return values.remove(o);
    }

    @Override
    public void clear() {
        this.map.clear();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Collects.forEach(c, new Consumer<E>() {
            @Override
            public void accept(E e) {
                add(e);
            }
        });
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Collects.forEach(c, new Consumer<E>() {
            @Override
            public void accept(E e) {
                remove(e);
            }
        });
        return true;
    }

    @Override
    public boolean retainAll(final Collection c) {
        // 找出有效的 orders
        final Set<Integer> orderSet = Pipeline.of(c).filter(new Predicate<Object>() {
            @Override
            public boolean test(Object value) {
                return (value instanceof Ordered);
            }
        }).map(new Function<Ordered, Integer>() {
            @Override
            public Integer apply(Ordered e) {
                return e.getOrder();
            }
        }).asSet(false);


        // 清理
        Collects.forEach(new ArrayList<Integer>(this.map.keySet()),
                new Consumer<Integer>() {
                    @Override
                    public void accept(Integer order) {

                        // 移除无效的 orders
                        if (!orderSet.contains(order)) {
                            map.remove(order);
                        } else {

                            // 清理不存在的
                            Collects.forEach(new ArrayList<E>(map.get(order)), new Consumer<E>() {
                                @Override
                                public void accept(E e) {
                                    if (!c.contains(e)) {
                                        map.remove(e);
                                    }
                                }
                            });
                        }

                    }
                });

        return true;
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        com.jn.langx.util.struct.Entry<Integer, Integer> coordinate = getCoordinate(index);

        final int order = coordinate.getKey();
        final int offset = coordinate.getValue();
        // 找出所有的大于 order的 orders
        List<Integer> orders = Pipeline.of(this.map.keySet()).filter(new Predicate<Integer>() {
            @Override
            public boolean test(Integer order2) {
                if (offset == 0) {
                    return order2 >= order;
                } else {
                    return order2 > order;
                }
            }
        }).asList();

        final CompositeListIterator listIterator = new CompositeListIterator();
        if (offset > 0) {
            Collection<E> firstCollection = this.map.get(order);
            Iterator<E> firstIter = Collects.newArrayList(firstCollection).subList(offset, firstCollection.size()).iterator();
            listIterator.addIterator(firstIter);
        }
        Collects.forEach(orders, new Consumer<Integer>() {
            @Override
            public void accept(Integer order) {
                listIterator.addIterator(map.get(order).iterator());
            }
        });
        return listIterator;
    }

    private class CompositeListIterator implements ListIterator<E> {
        private List<Iterator<E>> iters = Collects.emptyArrayList();
        private int current = 0;

        public void addIterator(Iterator<E> iterator) {
            iters.add(iterator);
        }

        @Override
        public boolean hasNext() {
            if (current < iters.size()-1) {
                return true;
            }
            return iters.get(current).hasNext();
        }

        @Override
        public E next() {
            if (!hasNext()) {
                return null;
            }

            if (!iters.get(current).hasNext()) {
                current++;
            }
            return iters.get(current).next();
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public E previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {
            iters.get(current).remove();
        }

        @Override
        public void set(E e) {
            // ignore it
        }

        @Override
        public void add(E e) {
            // ignore it
        }
    }

    @Override
    public ListIterator<E> listIterator() {
        return listIterator(0);
    }

    @Override
    public Iterator<E> iterator() {
        return listIterator();
    }

    @Override
    public List<E> subList(final int fromIndex, int toIndex) {
        int size = size();
        Preconditions.checkIndex(fromIndex, size);
        toIndex = Maths.min(this.map.total(), toIndex);
        Preconditions.checkIndex(toIndex, size);

        final com.jn.langx.util.struct.Entry<Integer, Integer> fromCoordinate = getCoordinate(fromIndex);
        final int fromOrder = fromCoordinate.getKey();

        final com.jn.langx.util.struct.Entry<Integer, Integer> toCoordinate = getCoordinate(fromIndex);
        final int toOrder = toCoordinate.getKey();

        final List<E> list = new ArrayList<E>();
        Collects.forEach(map.entrySet(), new Consumer2<Integer, Map.Entry<Integer, Collection<E>>>() {
            @Override
            public void accept(Integer od, Map.Entry<Integer, Collection<E>> values) {
                if (od == fromOrder) {
                    list.addAll(Collects.asList(values.getValue()).subList(fromCoordinate.getValue(), values.getValue().size()));
                } else if (od == toOrder) {
                    list.addAll(Collects.asList(values.getValue()).subList(0, toCoordinate.getValue()));
                } else {
                    list.addAll(values.getValue());
                }
            }
        });
        return list;
    }

    @Override
    public boolean equals(Object o) {
        return Objs.equals(map,o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean containsAll(Collection c) {
        return Collects.allMatch(c, new Predicate() {
            @Override
            public boolean test(Object obj) {
                return contains(obj);
            }
        });
    }

    @Override
    public String toString() {
        return "[" + Strings.join(",", toList()) + "]";
    }

    @Override
    public int size() {
        return map.total();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object obj) {
        if (!(obj instanceof Ordered)) {
            return false;
        }
        E value = (E) obj;
        int order = value.getOrder();
        Collection<E> values = map.get(order);
        if (values == null) {
            return false;
        }
        return values.contains(value);
    }

    @Override
    public int indexOf(final Object o) {
        if (!(o instanceof Ordered)) {
            return -1;
        }
        final int order = ((Ordered) o).getOrder();
        if (!this.map.containsKey(order)) {
            return -1;
        }
        final SimpleIntegerCounter counter = new SimpleIntegerCounter();
        Collects.forEach(this.map.keySet(), new Consumer<Integer>() {
            @Override
            public void accept(Integer od) {
                if (od < order) {
                    counter.increment(map.get(od).size());
                } else {
                    counter.increment(Collects.asList(map.get(od)).indexOf(o));
                }
            }
        }, new Predicate<Integer>() {
            @Override
            public boolean test(Integer od) {
                return od > order;
            }
        });
        return counter.get();
    }

    @Override
    public int lastIndexOf(final Object o) {
        return indexOf(o);
    }

    @Override
    public Object clone() {
        final OrderedList<E> other = new OrderedList<E>();
        Collects.forEach(this.map.entrySet(), new Consumer2<Integer, Collection<E>>() {
            @Override
            public void accept(Integer order, Collection<E> values) {
                other.addAll(values);
            }
        });
        return other;
    }

    @Override
    public Object[] toArray() {
        return toList().toArray();
    }

    private List<E> toList() {
        final List<E> list = new ArrayList<E>();
        Collects.forEach(this.map.entrySet(), new Consumer2<Integer, Collection<E>>() {
            @Override
            public void accept(Integer order, Collection<E> values) {
                list.addAll(values);
            }
        });
        return list;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return toList().toArray(a);
    }

    public void trimToSize() {
        // ignore it
    }

    public void ensureCapacity(int minCapacity) {
    }
}
