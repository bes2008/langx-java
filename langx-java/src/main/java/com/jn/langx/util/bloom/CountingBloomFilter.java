package com.jn.langx.util.bloom;


import com.jn.langx.util.hash.Hasher;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


/**
 * Implements a <i>counting Bloom filter</i>, as defined by Fan et al. in a ToN
 * 2000 paper.
 * <p>
 * A counting Bloom filter is an improvement to standard a Bloom filter as it
 * allows dynamic additions and deletions of set membership information.  This
 * is achieved through the use of a counting vector instead of a bit vector.
 * <p>
 * Originally created by
 * <a href="http://www.one-lab.org">European Commission One-Lab Project 034819</a>.
 *
 * @see Filter The general behavior of a filter
 * @see <a href="http://portal.acm.org/citation.cfm?id=343571.343572">Summary cache: a scalable wide-area web cache sharing protocol</a>
 */
public final class CountingBloomFilter extends Filter {
    /**
     * Storage for the counting buckets
     */
    private long[] buckets;

    /**
     * We are using 4bit buckets, so each bucket can count to 15
     */
    private final static long BUCKET_MAX_VALUE = 15;

    /**
     * Default constructor - use with readFields
     */
    public CountingBloomFilter() {
    }

    /**
     * Constructor
     *
     * @param vectorSize The vector size of <i>this</i> filter.
     * @param nbHash     The number of hash function to consider.
     * @param hashType   type of the hashing function (see
     *                   {@link Hasher}).
     */
    public CountingBloomFilter(int vectorSize, int nbHash, int hashType) {
        super(vectorSize, nbHash, hashType);
        buckets = new long[buckets2words(vectorSize)];
    }

    /**
     * returns the number of 64 bit words it would take to hold vectorSize buckets
     */
    private static int buckets2words(int vectorSize) {
        return ((vectorSize - 1) >>> 4) + 1;
    }


    @Override
    public void add(Key key) {
        if (key == null) {
            throw new NullPointerException("key can not be null");
        }

        int[] h = hash.hash(key);
        hash.clear();

        for (int i = 0; i < nbHash; i++) {
            // find the bucket
            int wordNum = h[i] >> 4;          // div 16
            int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

            long bucketMask = 15L << bucketShift;
            long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;

            // only increment if the count in the bucket is less than BUCKET_MAX_VALUE
            if (bucketValue < BUCKET_MAX_VALUE) {
                // increment by 1
                buckets[wordNum] = (buckets[wordNum] & ~bucketMask) | ((bucketValue + 1) << bucketShift);
            }
        }
    }

    /**
     * Removes a specified key from <i>this</i> counting Bloom filter.
     * <p>
     * <b>Invariant</b>: nothing happens if the specified key does not belong to <i>this</i> counter Bloom filter.
     *
     * @param key The key to remove.
     */
    public void delete(Key key) {
        if (key == null) {
            throw new NullPointerException("Key may not be null");
        }
        if (!membershipTest(key)) {
            throw new IllegalArgumentException("Key is not a member");
        }

        int[] h = hash.hash(key);
        hash.clear();

        for (int i = 0; i < nbHash; i++) {
            // find the bucket
            int wordNum = h[i] >> 4;          // div 16
            int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

            long bucketMask = 15L << bucketShift;
            long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;

            // only decrement if the count in the bucket is between 0 and BUCKET_MAX_VALUE
            if (bucketValue >= 1 && bucketValue < BUCKET_MAX_VALUE) {
                // decrement by 1
                buckets[wordNum] = (buckets[wordNum] & ~bucketMask) | ((bucketValue - 1) << bucketShift);
            }
        }
    }

    @Override
    public void and(Filter filter) {
        if (filter == null
                || !(filter instanceof CountingBloomFilter)
                || filter.vectorSize != this.vectorSize
                || filter.nbHash != this.nbHash) {
            throw new IllegalArgumentException("filters cannot be and-ed");
        }
        CountingBloomFilter cbf = (CountingBloomFilter) filter;

        int sizeInWords = buckets2words(vectorSize);
        for (int i = 0; i < sizeInWords; i++) {
            this.buckets[i] &= cbf.buckets[i];
        }
    }

    @Override
    public boolean membershipTest(Key key) {
        if (key == null) {
            throw new NullPointerException("Key may not be null");
        }

        int[] h = hash.hash(key);
        hash.clear();

        for (int i = 0; i < nbHash; i++) {
            // find the bucket
            int wordNum = h[i] >> 4;          // div 16
            int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

            long bucketMask = 15L << bucketShift;

            if ((buckets[wordNum] & bucketMask) == 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * This method calculates an approximate count of the key, i.e. how many
     * times the key was added to the filter. This allows the filter to be
     * used as an approximate <code>key -&gt; count</code> map.
     * <p>NOTE: due to the bucket size of this filter, inserting the same
     * key more than 15 times will cause an overflow at all filter positions
     * associated with this key, and it will significantly increase the error
     * rate for this and other keys. For this reason the filter can only be
     * used to store small count values <code>0 &lt;= N &lt;&lt; 15</code>.
     *
     * @param key key to be tested
     * @return 0 if the key is not present. Otherwise, a positive value v will
     * be returned such that <code>v == count</code> with probability equal to the
     * error rate of this filter, and <code>v &gt; count</code> otherwise.
     * Additionally, if the filter experienced an underflow as a result of
     * {@link #delete(Key)} operation, the return value may be lower than the
     * <code>count</code> with the probability of the false negative rate of such
     * filter.
     */
    public int approximateCount(Key key) {
        int res = Integer.MAX_VALUE;
        int[] h = hash.hash(key);
        hash.clear();
        for (int i = 0; i < nbHash; i++) {
            // find the bucket
            int wordNum = h[i] >> 4;          // div 16
            int bucketShift = (h[i] & 0x0f) << 2;  // (mod 16) * 4

            long bucketMask = 15L << bucketShift;
            long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;
            if (bucketValue < res) res = (int) bucketValue;
        }
        if (res != Integer.MAX_VALUE) {
            return res;
        } else {
            return 0;
        }
    }

    @Override
    public void not() {
        throw new UnsupportedOperationException("not() is undefined for "
                + this.getClass().getName());
    }

    @Override
    public void or(Filter filter) {
        if (filter == null
                || !(filter instanceof CountingBloomFilter)
                || filter.vectorSize != this.vectorSize
                || filter.nbHash != this.nbHash) {
            throw new IllegalArgumentException("filters cannot be or-ed");
        }

        CountingBloomFilter cbf = (CountingBloomFilter) filter;

        int sizeInWords = buckets2words(vectorSize);
        for (int i = 0; i < sizeInWords; i++) {
            this.buckets[i] |= cbf.buckets[i];
        }
    }

    @Override
    public void xor(Filter filter) {
        throw new UnsupportedOperationException("xor() is undefined for "
                + this.getClass().getName());
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < vectorSize; i++) {
            if (i > 0) {
                res.append(" ");
            }

            int wordNum = i >> 4;          // div 16
            int bucketShift = (i & 0x0f) << 2;  // (mod 16) * 4

            long bucketMask = 15L << bucketShift;
            long bucketValue = (buckets[wordNum] & bucketMask) >>> bucketShift;

            res.append(bucketValue);
        }

        return res.toString();
    }

    // Writable

    @Override
    public void write(DataOutput out) throws IOException {
        super.write(out);
        int sizeInWords = buckets2words(vectorSize);
        for (int i = 0; i < sizeInWords; i++) {
            out.writeLong(buckets[i]);
        }
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        super.readFields(in);
        int sizeInWords = buckets2words(vectorSize);
        buckets = new long[sizeInWords];
        for (int i = 0; i < sizeInWords; i++) {
            buckets[i] = in.readLong();
        }
    }
}