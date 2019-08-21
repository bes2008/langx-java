package com.jn.langx.test.util.collection;

import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Arrs;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
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
    public void testStreamApiForCollection(){
        Collection<String> list = Pipeline.of(new String[]{"Hello", "Java8", "Stream", "API", "for", "java 6"})
                .map(new Function<String, List<String>>() {
                    @Override
                    public List<String> apply(String string) {
                        return Collects.asList(Strings.split(string,""));
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


}
