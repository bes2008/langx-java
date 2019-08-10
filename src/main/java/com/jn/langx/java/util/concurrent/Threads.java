package com.jn.langx.java.util.concurrent;

import com.jn.langx.java.exception.ExceptionMessage;
import com.jn.langx.java.exception.IllegalParameterException;

public class Threads {

    /**
     * 获取需要创建线程数
     * <p>
     * 线程分工原则：<br>
     * 计算密集型尽量少创建线程，多分段处理;<br>
     * IO密集型尽量多创建线程
     * </p>
     *
     * @param blockRate 阻塞率，小数，取值范围：[0,1) ,即0到1之间包含0，不包含1
     */
    public static int getAvailableThreadNum(float blockRate) {
        if (blockRate >= 1 || blockRate < 0) {
            throw new IllegalParameterException(new ExceptionMessage("The parameter blockRate is invalid,the valid value scope is [{1},{2}), but given : {0}。", new Object[]{blockRate, 0, 1}));
        }
        int cpuCoreNum = Runtime.getRuntime().availableProcessors();
        return (int) (cpuCoreNum / (1 - blockRate));
    }
}
