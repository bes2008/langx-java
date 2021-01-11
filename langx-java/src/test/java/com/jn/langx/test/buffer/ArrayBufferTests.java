package com.jn.langx.test.buffer;

import com.jn.langx.util.collection.buffer.ArrayBuffer;
import org.junit.Test;

import java.util.List;

public class ArrayBufferTests {
    @Test
    public void test() {
        int maxLength = 100;
        ArrayBuffer<Integer> buffer = new ArrayBuffer<Integer>(maxLength);
        for (int i = 0; i < maxLength; i++) {
            buffer.put(i);
        }
        printWithIndex(buffer);

        System.out.println("\n=====================");

        buffer.put(78, 781);

        print(buffer);
        System.out.println("\n=====================");

        List<Integer> integerList = buffer.get(70, 10);
        System.out.println(integerList);

    }

    private void printWithIndex(ArrayBuffer<Integer> buffer) {
        int k = -1;
        for (int i = 0; i < 32; i++) {
            k++;
            if (k >= buffer.limit()) {
                break;
            }
            System.out.print(buffer.get(k));
            if (k % 32 == 31) {
                i = -1;
                System.out.println();
            }
        }
    }

    private void print(ArrayBuffer<Integer> buffer) {
        buffer.rewind();
        for (int i = 0; i < 32; i++) {
            if (buffer.hasRemaining()) {
                System.out.print(buffer.get());
                if (i % 32 == 31) {
                    i = -1;
                    System.out.println();
                }
            }
        }
    }
}
