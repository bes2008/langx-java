package com.jn.langx.util.random;

import com.jn.langx.security.crypto.key.SecureRandoms;
import com.jn.langx.text.StrTokenizer;
import com.jn.langx.util.Strings;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.function.Consumer;
import com.jn.langx.util.function.Function;
import com.jn.langx.util.function.Predicate;
import com.jn.langx.util.id.Nanoids;
import com.jn.langx.util.struct.Pair;

import java.util.List;
import java.util.Random;

public class Randoms {
    public static IRandom of(Random random) {
        return new RandomProxy(random);
    }

    public static IRandom ofSecure() {
        return of(SecureRandoms.getDefault());
    }
    public static String randomString(){
        return randomString("361E6D51-FAEC-444A-9079-341386DA8E2E");
    }

    public static String randomString(String pattern){
        return randomString("useandom26T198340PX75pxJACKVERYMINDBUSHWOLFGQZbfghjklqvwyzrict",pattern);
    }


    public static String randomString(String alphabet, String pattern){
        return randomString(alphabet,pattern,"-");
    }

    public static String randomString(String alphabet, String pattern, final String separator){
        boolean hasSeparator= Strings.isNotEmpty(separator);

        if(!hasSeparator){
            return randomString(alphabet, pattern.length());
        }

        List<String> tokens = new StrTokenizer(pattern, true, false, -1, separator).tokenize();
        // key: token
        // value: whether is separator
        List<Pair<String,Boolean>> segments = Pipeline.of(tokens)
                .map(new Function<String, Pair<String,Boolean>>() {
                    @Override
                    public Pair<String,Boolean> apply(String token) {
                        return new Pair<String, Boolean>(token, Strings.equals(separator, token));
                    }
                }).asList();

        // 过滤出非分隔符的segments，并计算出总的长度
        int expectedLength = Pipeline.of(segments)
                .filter(new Predicate<Pair<String, Boolean>>() {
                    @Override
                    public boolean test(Pair<String, Boolean> segment) {
                        return !segment.getValue();
                    }
                })
                .map(new Function<Pair<String, Boolean>, Integer>() {
                    @Override
                    public Integer apply(Pair<String, Boolean> segment) {
                        return segment.getKey().length();
                    }
                })
                .sum().intValue();
        String str = randomString(alphabet, expectedLength);

        int offset=0;
        final StringBuilder builder = new StringBuilder();
        for(Pair<String, Boolean> segment:segments) {
            // 是分隔符
            if(segment.getValue()){
                builder.append(separator);
            }
            else{
                int segmentLength = segment.getKey().length();
                int endIndex = offset+segmentLength;
                builder.append(Strings.substring(str,offset,endIndex));
                offset = endIndex;
            }
        }
        return builder.toString();
    }

    public static String randomString(String alphabet, int length){
        return Nanoids.nanoid(alphabet, length);
    }
}
