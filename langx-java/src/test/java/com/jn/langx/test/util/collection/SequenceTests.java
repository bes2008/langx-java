package com.jn.langx.test.util.collection;


import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.sequence.charseq.StringSequence;
import com.jn.langx.util.function.Functions;
import com.jn.langx.util.function.Operator2;
import com.jn.langx.util.io.Consoles;
import org.junit.Test;

public class SequenceTests {
    private int[] sequence = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

    @Test
    public void testStringSequence() {
        String a = Pipeline.of(sequence)
                .map(Functions.toStringFunction())
                .reduce(new Operator2<String>() {
                    @Override
                    public String apply(String input1, String input2) {
                        return input1 + input2;
                    }
                });

        StringSequence stringSequence = new StringSequence(a);
        Consoles.println("content:", stringSequence);
        Consoles.println("size:", stringSequence.size());

        stringSequence.set(2, 'a');
        Consoles.println("content:", stringSequence);
        Consoles.println("size:", stringSequence.size());

        stringSequence.addAll(Collects.newArrayList( '字','符','串'));
        Consoles.println("content:", stringSequence);
        Consoles.println("size:", stringSequence.size());

        StringSequence sequence = stringSequence.subSequence(6,-6);
    }
}
