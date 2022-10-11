package com.jn.langx.io.stream.bandwidthlimit;

import com.jn.langx.util.DataSizes;
import com.jn.langx.util.logging.Loggers;
import org.slf4j.Logger;

/**
 * 字节流限流器
 * 主要思想：
 * 定义一个chunk以及允许的最大速率 maxRate(单位 KB/s)。
 * 通过maxRate我们可以算出，在maxRate的速率下，通过chunk大小的字节流所需要的时间 timeCostPerChunk
 * 在读取/写入字节时，我们维护已经读取/写入的字节量 bytesWillBeSentOrReceive。
 * 当bytesWillBeSentOrReceive达到chunk的大小时，检查期间过去的时间(nowNanoTime-lastPieceSentOrReceiveTick)
 * 如果过去的时间小于 timeCostPerChunk，说明当前的速率已经超过了 maxRate的速率，这时候就需要休眠一会来限制流量
 * 如果速率没超过或者休眠完后 将bytesWillBeSentOrReceive=bytesWillBeSentOrReceive-chunk
 * 之后继续检查
 */
public class BandwidthLimiter {

    private static final Logger LOGGER = Loggers.getLogger(BandwidthLimiter.class);
    //KB代表的字节数
    //一个chunk的大小，单位byte
    private static final Long CHUNK_LENGTH = DataSizes.ONE_MB;

    //已经发送/读取的字节数
    private int bytesWillBeSentOrReceive = 0;
    //上一次接收到字节流的时间戳——单位纳秒
    private long lastPieceSentOrReceiveTick = System.nanoTime();
    //允许的最大速率，默认为 1024KB/s
    private int maxRate;
    //在maxRate的速率下，通过chunk大小的字节流要多少时间（纳秒）
    private long timeCostPerChunk;

    public BandwidthLimiter(int maxRate) {
        this.setMaxRate(maxRate);
    }

    //动态调整最大速率
    public void setMaxRate(int maxRate) {
        if (maxRate < 0) {
            maxRate = 1024;
        }
        if (maxRate < 4) {
            maxRate = 4;
        }
        this.maxRate = maxRate;
        // 纳秒
        this.timeCostPerChunk = (1000000000L * CHUNK_LENGTH) / (this.maxRate * DataSizes.ONE_KB);
    }

    public synchronized void limitNextByte() {
        this.limitNextBytes(1);
    }

    public synchronized void limitNextBytes(int len) {
        this.bytesWillBeSentOrReceive += len;

        while (this.bytesWillBeSentOrReceive > CHUNK_LENGTH) {
            long nowTick = System.nanoTime();
            long passTime = nowTick - this.lastPieceSentOrReceiveTick;
            long missedTime = this.timeCostPerChunk - passTime;
            if (missedTime > 0) {
                try {
                    Thread.sleep(missedTime / 1000000, (int) (missedTime % 1000000));
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
            this.bytesWillBeSentOrReceive -= CHUNK_LENGTH;
            this.lastPieceSentOrReceiveTick = nowTick + (missedTime > 0 ? missedTime : 0);
        }
    }
}