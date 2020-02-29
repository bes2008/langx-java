package com.jn.langx.test.util.collection;

import com.jn.langx.test.bean.Person;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.diff.CollectionDiffResult;
import com.jn.langx.util.collection.diff.KeyBuilder;
import com.jn.langx.util.comparator.ComparableComparator;
import com.jn.langx.util.comparator.Comparators;
import com.jn.langx.util.function.Function;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class CollectsTest {
    @Test
    public void emptyCollectionTests() {
        List list = Collects.emptyArrayList();
        Assert.assertTrue(list instanceof ArrayList && list.isEmpty());
        list = Collects.emptyLinkedList();
        Assert.assertTrue(list instanceof LinkedList && list.isEmpty());
        Map map = Collects.emptyHashMap();
        Assert.assertTrue(map.getClass() == HashMap.class && map.isEmpty());
        map = Collects.emptyHashMap(false);
        Assert.assertTrue(map.getClass() == HashMap.class && map.isEmpty());
        map = Collects.emptyHashMap(true);
        Assert.assertTrue(map.getClass() == LinkedHashMap.class && map.isEmpty());
        Set set = Collects.emptyHashSet();
        Assert.assertTrue(set.getClass() == HashSet.class && set.isEmpty());
        set = Collects.emptyHashSet(false);
        Assert.assertTrue(set.getClass() == HashSet.class && set.isEmpty());
        set = Collects.emptyHashSet(true);
        Assert.assertTrue(set.getClass() == LinkedHashSet.class && set.isEmpty());
        set = Collects.emptyTreeSet();
        Assert.assertTrue(set.getClass() == TreeSet.class && set.isEmpty());

        String[] arr = Collects.emptyArray(String.class);
        Assert.assertArrayEquals(Arrs.createArray(String.class, 0), arr);
    }

    @Test
    public void listToArrayAndBackTest() {
        Integer[] array = Arrs.range(10);
        List<Integer> list = Collects.asList(array, true, Collects.ListType.ArrayList);
        Integer[] array1 = Collects.toArray(list, Integer[].class);
        Assert.assertArrayEquals(array, array1);

        Object[] array2 = Collects.toArray(list);
        Assert.assertArrayEquals(array1, array2);
    }

    @Test
    public void testStreamApiForCollection() {
        Collection<String> list = Pipeline.of(new String[]{"Hello", "Java8", "Stream", "API", "for", "java 6"})
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(String string) {
                        return Collects.asList(Strings.split(string, ""));
                    }
                }).flatMap(new Function<String, String>() {
                    @Override
                    public String apply(String input) {
                        return input;
                    }
                }).distinct().sorted(Comparators.STRING_COMPARATOR_IGNORE_CASE)
                .getAll();
        System.out.println(list);
    }

    @Test
    public void testPage() {
        Integer[] paged = Collects.limit(Collects.skip(Collects.asList(Arrs.range(100)), 20), 10).toArray(new Integer[0]);
        Assert.assertArrayEquals(Arrs.range(20, 30, 1), paged);
    }

    @Test
    public void testMaxAndMin() {
        List<Integer> list = Collects.asList(Arrs.range(20, 30, 1));
        Collections.shuffle(list);
        Integer[] array = Collects.toArray(list, Integer[].class);
        Assert.assertTrue(29 == Collects.<Integer>max(array, new ComparableComparator<Integer>()));
        Assert.assertTrue(20 == Collects.<Integer>min(array, new ComparableComparator<Integer>()));
    }

    @Test
    public void testCollect() {
        Collection<Integer> collection = Collects.collect(Arrs.range(10), new ArrayList<Integer>());
        Assert.assertTrue(collection.size() == 10);
    }

    @Test
    public void testDiff() {
        // simple compare with equals
        List<Integer> oldList = Collects.asList(Arrs.range(10));
        List<Integer> newList = Collects.asList(Arrs.range(5, 15));
        CollectionDiffResult<Integer> dr = Collects.diff(oldList, newList);
        Assert.assertArrayEquals(dr.getRemoves().toArray(new Integer[0]), Arrs.range(5));
        Assert.assertArrayEquals(dr.getAdds().toArray(new Integer[0]), Arrs.range(10, 15));
        Assert.assertArrayEquals(dr.getEquals().toArray(new Integer[0]), Arrs.range(5, 10));


        Function<Integer, Person> mapper = new Function<Integer, Person>() {
            @Override
            public Person apply(Integer id) {
                Person p = new Person();
                p.setId("id_" + id);
                p.setAge(id);
                p.setName("name_" + id);
                return p;
            }
        };
        Collection<Person> oldPersonList = Collects.<Integer, Person>map(oldList, mapper);
        Collection<Person> newPersonList = Collects.<Integer, Person>map(newList, mapper);

        CollectionDiffResult<Person> dr2 = Collects.diff(oldPersonList, newPersonList, null, new KeyBuilder<String, Person>() {
            @Override
            public String getKey(Person p) {
                return p.getId();
            }
        });

        Collection<Person> newPersonList2 = Collects.<Integer, Person>map(newList, new Function<Integer, Person>() {
            @Override
            public Person apply(Integer id) {
                Person p = new Person();
                p.setId("id_" + id);
                if (id % 2 == 0) {
                    p.setAge(id);
                } else {
                    p.setAge(id + 1);
                }
                p.setName("name_" + id);
                return p;
            }
        });
        dr2 = Collects.diff(oldPersonList, newPersonList2, null, new KeyBuilder<String, Person>() {
            @Override
            public String getKey(Person p) {
                return p.getId();
            }
        });
        Assert.assertTrue(dr2.getUpdates().size() == 3);
    }

    @Test
    public void testUnion() {
        List<Integer> list1 = Collects.asList(1, 3, 5, 6, 7, 9, 8, 8);
        List<Integer> list2 = Collects.asList(2, 4, 6, 8);
        System.out.println(Collects.intersection(list1, list2));
        System.out.println(Collects.union(list1, list2));
    }
}
