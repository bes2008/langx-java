package com.jn.langx.test.util.datetime;

import com.jn.langx.util.Dates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.datetime.fast.FastDateFormat;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class DateTimeParseTest {

    @Test
    public void test() {
    //    testParse("2018-01-01T17:12:09.123456789");
        testParse("2018-01-01T12:09:09.123Z");
    }

    private void testParse(String dt){
        List<String> patterns = Collects.newArrayList(

             //   "yyyy-MM-dd'T'HH:mm:ss.nnnnnnnnn",
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss"
        );
        Date d = Dates.parse(true, dt, null, null, patterns);
        System.out.println(d);
        System.out.println(Dates.format(d, "yyyy-MM-dd'T'HH:mm:ss.SSS"));
    }


    @Test
    public void testFastDateTimeParse() throws Throwable{
        Date d= FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ss.SSSX", null, null).parse("2018-01-01T12:09:09.123Z");
        System.out.println(d);
    }

}
