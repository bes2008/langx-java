package com.jn.langx.test.util.concurrent;

import org.junit.Assert;
import org.junit.Test;

public class InterruptTest {

    @Test
    public void testInterruptDeadLoop() throws Exception{
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        int i = 0;
                        int j = 10;
                        Assert.assertTrue(j > (i + 2));
                    }
                }catch (Throwable e){
                    e.printStackTrace();
                }
            }
        },"watched");

        t.start();
        Thread.sleep(100);
        t.interrupt();
        Thread.sleep(100000000);
    }

}
