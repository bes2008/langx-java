package com.jn.langx.util.retry;

import com.jn.langx.util.Maths;
import com.jn.langx.util.Preconditions;

public class ThreadSleepWaitStrategy implements WaitStrategy{
    private long maxMills;
    public ThreadSleepWaitStrategy(){
        this(5*60*1000L);
    }
    public ThreadSleepWaitStrategy(long maxMills){
        Preconditions.checkArgument(maxMills>=0);
        this.maxMills = maxMills;
    }
    @Override
    public void await(long mills) throws InterruptedException{
        Thread.sleep(Maths.maxLong(0, Maths.minLong(mills, this.maxMills)));
    }
}
