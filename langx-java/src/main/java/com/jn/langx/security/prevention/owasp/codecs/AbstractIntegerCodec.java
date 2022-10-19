package com.jn.langx.security.prevention.owasp.codecs;


import com.jn.langx.util.collection.pushback.PushBackSequenceImpl;
import com.jn.langx.util.collection.pushback.PushbackSequence;

public class AbstractIntegerCodec extends AbstractCodec<Integer> {

    /**
     * {@inheritDoc}
     */
    @Override
    public String decode(String input) {
        StringBuilder sb = new StringBuilder();
        PushbackSequence<Integer> pbs = new PushBackSequenceImpl(input);
        while (pbs.hasNext()) {
            Integer c = decodeCharacter(pbs);
            if (c != null && Character.isValidCodePoint(c)) {
                sb.appendCodePoint(c);
            } else {
                sb.appendCodePoint(pbs.next());
            }
        }
        return sb.toString();
    }
}
