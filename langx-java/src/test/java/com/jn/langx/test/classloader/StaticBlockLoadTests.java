package com.jn.langx.test.classloader;

public class StaticBlockLoadTests {
    public static void main(String[] args){
        System.out.println(A.INSTANCE.toString());
    }
}
