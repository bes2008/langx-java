package com.jn.langx.test.accessor;

import com.jn.langx.accessor.Accessors;
import com.jn.langx.navigation.object.JavaObjectNavigator;
import com.jn.langx.test.bean.Address;
import com.jn.langx.test.bean.PersonDto;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class AccessorFactoryTests {

    @Test
    public void test() {
        PersonDto personDto = new PersonDto();
        personDto.setAge(12);
        personDto.setDesc("12");
        personDto.setId("id12");
        personDto.setName("name12");

        List<Address> addresses = new ArrayList<Address>();
        Address address = new Address();
        address.setA("a");
        address.setB("b");
        addresses.add(address);

        personDto.setAddresses(addresses);

        System.out.println(Accessors.of(personDto).getString("id"));
        System.out.println(Accessors.of(personDto).getInteger("age"));

        System.out.println(new JavaObjectNavigator().getAccessor(personDto,"addresses/0").get("a"));

    }

}
