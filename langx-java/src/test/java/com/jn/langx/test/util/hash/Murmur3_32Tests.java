package com.jn.langx.test.util.hash;

import com.jn.langx.util.hash.StreamingHasher;
import com.jn.langx.util.hash.Hashs;
import com.jn.langx.util.hash.impl.Murmur3_32Hasher;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

public class Murmur3_32Tests {

    @Test
    public void test() {
        byte[] bytes = "helloworld".getBytes(Charsets.UTF_8);
        long hash1 = Hashs.hash( new Murmur3_32Hasher(),bytes, bytes.length, -1);
        System.out.println(hash1);
    }

    @Test
    public void test1() {
        String text = "hello,langx hasher";
        StreamingHasher hasher1 = new Murmur3_32Hasher();
        testWithLangx(text, hasher1);

    }

    private void testWithLangx(String text, StreamingHasher hasher) {
        hasher.setSeed(-1);
        byte[] bytes2 = text.getBytes(Charsets.UTF_8);
        hasher.update(bytes2, 0, bytes2.length);
        long hash = hasher.getHash();
        System.out.println(hash);
    }
}
