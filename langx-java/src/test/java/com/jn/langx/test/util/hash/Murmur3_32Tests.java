package com.jn.langx.test.util.hash;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.jn.langx.util.hash.Hasher;
import com.jn.langx.util.hash.Murmur3_32Hasher;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

public class Murmur3_32Tests {

    @Test
    public void test() {
        byte[] bytes = "helloworld".getBytes(Charsets.UTF_8);
        long hash1 = new Murmur3_32Hasher().hash(bytes, bytes.length, -1);
        long hash2 = Hashing.murmur3_32(-1).hashBytes(bytes, 0, bytes.length).asInt();
        System.out.println(hash1);
        System.out.println(hash2);
    }

    @Test
    public void test1() {
        String text = "hello,langx hasher";
        Hasher hasher1 = new Murmur3_32Hasher();
        testWithLangx(text, hasher1);

    }

    @Test
    public void test2() {
        String text = "hello,langx hasher";
        HashFunction hashFunction2 = Hashing.murmur3_32(-1);
        //   hashFunction2.hashBytes(bytes1, 0, bytes1.length);
        com.google.common.hash.Hasher hasher2 = hashFunction2.newHasher();
        testWithGuava(text, hasher2);

    }

    private void testWithLangx(String text, Hasher hasher) {
        hasher.setSeed(-1);
        byte[] bytes2 = text.getBytes(Charsets.UTF_8);
        hasher.update(bytes2, 0, bytes2.length);
        long hash = hasher.get();
        System.out.println(hash);
    }


    private void testWithGuava(String text, com.google.common.hash.Hasher hasher) {
        byte[] bytes2 = text.getBytes(Charsets.UTF_8);
        hasher.putBytes(bytes2, 0, bytes2.length);
        long hash2 = hasher.hash().asInt();
        System.out.println(hash2);
    }
}
