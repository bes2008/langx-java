package com.jn.langx.test.bean;

import com.jn.langx.util.bean.Beans;
import com.jn.langx.util.bean.JavaBeanModelMapper;
import com.jn.langx.util.collection.Collects;
import org.junit.Test;

public class BeansCopyTest {
    private static final Person p;

    static {
        p = new Person();
        p.setId("p-id-0");
        p.setName("p-name-0");
        p.setDesc("p-desc-0");
        p.setAge(0);

        Address address = new Address();
        address.setA("address-a");
        address.setB("address-b");

        p.setAddresses(Collects.asList(address));
    }

    @Test
    public void test() {
        PersonDto pt = JavaBeanModelMapper.map(p, PersonDto.class);
        System.out.println(pt);
    }
}
