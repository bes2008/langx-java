package com.jn.langx.test.bean;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Objs;

import java.util.List;

public class Person {
    private String id;
    private int age;
    private String name;
    private String desc;
    private List<Address> addresses;

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                id.equals(person.id) &&
                name.equals(person.name);
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public int hashCode() {
        return Objs.hash(id, name, age);
    }

    @Override
    public String toString() {
        return StringTemplates.formatWithIndex("id: {0}, name: {1}, age: {2}", this.id, this.name, this.age);
    }
}
