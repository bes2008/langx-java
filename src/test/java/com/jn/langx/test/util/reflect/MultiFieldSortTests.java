package com.jn.langx.test.util.reflect;

import com.jn.langx.test.bean.Person;
import com.jn.langx.util.Maths;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.comparator.ParallelingComparator;
import com.jn.langx.util.comparator.ReverseComparator;
import com.jn.langx.util.comparator.StringComparator;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.reflect.FieldComparator;
import org.junit.Test;

import java.util.*;

public class MultiFieldSortTests {

    @Test
    public void test0() {
        List<Person> persons;
        Random random = new Random(10000);

        persons = new LinkedList<Person>();
        for (int i = 0; i < 100; i++) {
            Person person = new Person();
            person.setId("id_" + Maths.abs(random.nextInt()));
            person.setName("name_" + Maths.abs(random.nextInt()));
            person.setAge(Math.abs(random.nextInt(200)));
            persons.add(person);
        }


        ParallelingComparator comparator = new ParallelingComparator();
        comparator.addComparator(new FieldComparator(Person.class, "id", null));
        comparator.addComparator(new FieldComparator(Person.class, "name", null));
        comparator.addComparator(new FieldComparator(Person.class, "age", null));
        Set<Person> pset = new TreeSet<Person>(comparator);

        pset.addAll(persons);

        Collects.forEach(pset, new Consumer<Person>() {
            @Override
            public void accept(Person person) {
                System.out.println(person);
            }
        });
    }

    @Test
    public void test2() {
        System.out.println("===================test2==================");
        List<Person> persons;
        Random random = new Random(10);

        persons = new LinkedList<Person>();
        for (int i = 0; i < 100; i++) {
            Person person = new Person();
            person.setId("id_" + Math.abs(random.nextInt(10)));
            person.setName("name_" + Math.abs(random.nextInt(10)));
            person.setAge(Math.abs(random.nextInt(100)));
            persons.add(person);
        }

        ParallelingComparator comparator = new ParallelingComparator();
        comparator.addComparator(new FieldComparator(Person.class, "id", null));
        comparator.addComparator(new FieldComparator(Person.class, "name", null));
        comparator.addComparator(new FieldComparator(Person.class, "age", null));
        Set<Person> pset = new TreeSet<Person>(comparator);

        pset.addAll(persons);

        Collects.forEach(pset, new Consumer<Person>() {
            @Override
            public void accept(Person person) {
                System.out.println(person);
            }
        });
    }

    @Test
    public void test3() {
        System.out.println("===================test3==================");
        List<Person> persons;
        Random random = new Random(10);

        persons = new LinkedList<Person>();
        for (int i = 0; i < 100; i++) {
            Person person = new Person();
            person.setId("id_" + 0);
            person.setName("name_" + Math.abs(random.nextInt(10)));
            person.setAge(Math.abs(random.nextInt(100)));
            persons.add(person);
        }

        ParallelingComparator comparator = new ParallelingComparator();
        comparator.addComparator(new FieldComparator(Person.class, "id", null));
        comparator.addComparator(new FieldComparator(Person.class, "name", null));
        comparator.addComparator(new FieldComparator(Person.class, "age", null));
        Set<Person> pset = new TreeSet<Person>(comparator);

        pset.addAll(persons);

        Collects.forEach(pset, new Consumer<Person>() {
            @Override
            public void accept(Person person) {
                System.out.println(person);
            }
        });
    }

    @Test
    public void test4() {
        System.out.println("===================test4==================");
        List<Person> persons;
        Random random = new Random(10);

        persons = new LinkedList<Person>();
        for (int i = 0; i < 100; i++) {
            Person person = new Person();
            person.setId("id_" + 0);
            person.setName("name_" + Math.abs(random.nextInt(10)));
            person.setAge(Math.abs(random.nextInt(100)));
            persons.add(person);
        }

        ParallelingComparator comparator = new ParallelingComparator();
        comparator.addComparator(new FieldComparator(Person.class, "id", null));
        comparator.addComparator(new FieldComparator(Person.class, "name", new ReverseComparator(new StringComparator())));
        comparator.addComparator(new FieldComparator(Person.class, "age", null));
        Set<Person> pset = new TreeSet<Person>(comparator);

        pset.addAll(persons);

        Collects.forEach(pset, new Consumer<Person>() {
            @Override
            public void accept(Person person) {
                System.out.println(person);
            }
        });
    }
}
