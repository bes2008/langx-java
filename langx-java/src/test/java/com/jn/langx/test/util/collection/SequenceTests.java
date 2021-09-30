package com.jn.langx.test.util.collection;


import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.collection.Pipeline;
import com.jn.langx.util.collection.sequence.Sequence;
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
        showSequenceInfo(stringSequence);

        stringSequence.set(2, 'a');
        showSequenceInfo(stringSequence);

        stringSequence.addAll(Collects.newArrayList('字', '符', '串'));
        showSequenceInfo(stringSequence);

        stringSequence = stringSequence.subSequence(2, -2);
        showSequenceInfo(stringSequence);
    }

    private void showSequenceInfo(Sequence sequence) {
        Consoles.log("content:", sequence);
        Consoles.log("size:", sequence.size());
        Consoles.log("first:", sequence.first());
        Consoles.log("last:", sequence.last());
    }
}
