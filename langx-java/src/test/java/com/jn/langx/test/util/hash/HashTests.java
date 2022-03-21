package com.jn.langx.test.util.hash;

import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.hash.Hashs;
import com.jn.langx.util.io.Charsets;
import org.junit.Test;

public class HashTests {
    @Test
    public void test() {
        String text = "Java Basic Types: String, Long, Integer, Float, Double";
        byte[] bytes = text.getBytes(Charsets.UTF_8);
        printHash(bytes, "murmur", "murmur2", "murmur3_32", "murmur3_128", "jenkins", "crc-32", "crc-32c", "adler32", "fnv1_32", "fnv1_64", "fnv1a_32", "fnv1a_64", "hmacmd5", "md5");
    }

    private void printHash(final byte[] bytes, String... algorithms) {
        Collects.forEach(algorithms, new Consumer<String>() {
            @Override
            public void accept(String algorithm) {
                System.out.println(StringTemplates.formatWithPlaceholder("{}: {}", algorithm, Hashs.hash(algorithm,null, bytes, bytes.length, 0)));
            }
        });

    }
}
