package com.jn.langx.test.util.datetime;

import com.jn.langx.util.Dates;
import com.jn.langx.util.collection.Collects;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class DateTimeParseTest {

    @Test
    public void test() {
        List<String> patterns = Collects.newArrayList(

                "yyyy-MM-dd'T'HH:mm:ss.nnnnnnnnn","yyyy-MM-dd'T'HH:mm:ss"
        );
        Date d = Dates.parse("2018-01-01T17:09:09.123456789", null, null, patterns);
        System.out.println(d);
        System.out.println(Dates.format(d, "yyyy-MM-dd'T'HH:mm:ss.SSS"));
    }

}
