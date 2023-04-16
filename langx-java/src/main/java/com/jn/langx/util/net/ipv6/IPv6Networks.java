package com.jn.langx.util.net.ipv6;

import java.util.BitSet;

import static com.jn.langx.util.net.ipv6.BitSetHelpers.bitSetOf;

/**
 * Helper methods used by IPv6Network.
 *
 */
public class IPv6Networks {
    static int longestPrefixLength(IPv6Address first, IPv6Address last) {
        final BitSet firstBits = bitSetOf(first.getLowBits(), first.getHighBits());
        final BitSet lastBits = bitSetOf(last.getLowBits(), last.getHighBits());

        return countLeadingSimilarBits(firstBits, lastBits);
    }

    private static int countLeadingSimilarBits(BitSet a, BitSet b) {
        int result = 0;
        for (int i = 127; i >= 0 && (a.get(i) == b.get(i)); i--) {
            result++;
        }

        return result;
    }
}
