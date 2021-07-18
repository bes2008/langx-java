package com.jn.langx.test.codec;

import com.jn.langx.util.io.Charsets;
import org.junit.Test;

/**
 * 计算机是用二进制表示的，那么怎么用二进制来表示键盘上的字符呢？
 * 美国人发明了ASCII表，将键盘上的所有按键从0到127进行编号。
 * 这样每按下一个键，键盘就将每一个按键对应的数字编码的二进制信号发给主机。
 * 这样计算机在根据ASCII表就知道输入了什么，就可以显示到显示器上。
 *
 * ASCII码表中每一个字母或者键盘控制，都对应
 */
public class ByteTests {
    @Test
    public void test() {
        printBytes("a".getBytes(Charsets.UTF_8));
        printBytes("a".getBytes(Charsets.US_ASCII));
    }

    private void printBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            System.out.println("," + bytes[i]);
        }
        System.out.println();
    }
}
