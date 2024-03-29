package com.jn.langx.test.util;

import com.jn.langx.util.Emptys;
import com.jn.langx.util.collection.Collects;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class EmptysTests {

    @Test
    public void isNullTests() {
        Assert.assertTrue(Emptys.isNull(null));
        Assert.assertFalse(Emptys.isNull(3));
        Assert.assertFalse(Emptys.isNull(""));
        Assert.assertFalse(Emptys.isNull(new int[0]));
    }

    @Test
    public void isEmptyTests() {
        Assert.assertTrue(Emptys.isEmpty(null));
        Assert.assertTrue(Emptys.isEmpty(""));
        Assert.assertFalse(Emptys.isEmpty("\t"));
        Assert.assertTrue(Emptys.isEmpty(new int[0]));
        Assert.assertFalse(Emptys.isEmpty(new int[3]));
        Assert.assertTrue(Emptys.isEmpty(0));
        Assert.assertTrue(Emptys.isEmpty(0.0));
        Assert.assertTrue(Emptys.isEmpty(Collects.emptyArrayList()));
        Assert.assertFalse(Emptys.isEmpty(new ArrayList<String>(Collects.asList(new String[]{"a", "b"}))));
    }

    @Test
    public void allMatch(){
        System.out.println(Emptys.isAllEmpty(null,null));
        System.out.println(Emptys.isAllEmpty(null,null,1));
    }

    @Test
    public void anyMatch(){
        System.out.println(Emptys.isAnyEmpty(null,null));
        System.out.println(Emptys.isAnyEmpty(null,null,1));
        System.out.println(Emptys.isAnyEmpty('3',2,1));
    }

    @Test
    public void noneMatch(){
        System.out.println(Emptys.isNoneEmpty(null,null));
        System.out.println(Emptys.isNoneEmpty(null,null,1));
        System.out.println(Emptys.isNoneEmpty('3',2,1));
    }

}
