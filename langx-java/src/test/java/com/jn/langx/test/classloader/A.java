package com.jn.langx.test.classloader;

public class A {
    public static final A INSTANCE = new A();

    public A() {

    }

    private boolean isOk = false;

}
