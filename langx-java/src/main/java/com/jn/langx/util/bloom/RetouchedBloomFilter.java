package com.jn.langx.util.bloom;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.hash.StreamingHasher;
import com.jn.langx.util.random.IRandom;
import com.jn.langx.util.random.Randoms;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.*;

/**
 * Implements a <i>retouched Bloom filter</i>, as defined in the CoNEXT 2006 paper.
 * <p>
 * It allows the removal of selected false positives at the cost of introducing
 * random false negatives, and with the benefit of eliminating some random false
 * positives at the same time.
 * <p>
 * <p>
 * Originally created by
 * <a href="http://www.one-lab.org">European Commission One-Lab Project 034819</a>.
 *
 * @see Filter The general behavior of a filter
 * @see BloomFilter A Bloom filter
 * @see RemoveScheme The different selective clearing algorithms
 * @see <a href="http://www-rp.lip6.fr/site_npa/site_rp/_publications/740-rbf_cameraready.pdf">Retouched Bloom Filters: Allowing Networked Applications to Trade Off Selected False Positives Against False Negatives</a>
 */
public final class RetouchedBloomFilter extends BloomFilter
        implements RemoveScheme {
    /**
     * KeyList vector (or ElementList Vector, as defined in the paper) of false positives.
     */
    List<Key>[] fpVector;

    /**
     * KeyList vector of keys recorded in the filter.
     */
    List<Key>[] keyVector;

    /**
     * Ratio vector.
     */
    double[] ratio;

    private IRandom rand;

    /**
     * Default constructor - use with readFields
     */
    public RetouchedBloomFilter() {
    }

    /**
     * Constructor
     *
     * @param vectorSize The vector size of <i>this</i> filter.
     * @param nbHash     The number of hash function to consider.
     * @param hasherName   type of the hashing function (see
     *                   {@link StreamingHasher}).
     */
    public RetouchedBloomFilter(int vectorSize, int nbHash, String hasherName) {
        super(vectorSize, nbHash, hasherName);

        this.rand = null;
        createVector();
    }

    @Override
    public void add(@NonNull Key key) {
        Preconditions.checkNotNull(key, "key can not be null");

        int[] h = hash.hash(key);
        hash.clear();

        for (int i = 0; i < nbHash; i++) {
            bits.set(h[i]);
            keyVector[h[i]].add(key);
        }
    }

    /**
     * Adds a false positive information to <i>this</i> retouched Bloom filter.
     * <p>
     * <b>Invariant</b>: if the false positive is <code>null</code>, nothing happens.
     *
     * @param key The false positive key to add.
     */
    public void addFalsePositive(@NonNull Key key) {
        Preconditions.checkNotNull(key, "key can not be null");

        int[] h = hash.hash(key);
        hash.clear();

        for (int i = 0; i < nbHash; i++) {
            fpVector[h[i]].add(key);
        }
    }

    /**
     * Adds a collection of false positive information to <i>this</i> retouched Bloom filter.
     *
     * @param coll The collection of false positive.
     */
    public void addFalsePositive(@NonNull Collection<Key> coll) {
        Preconditions.checkNotNull(coll, "\"Collection<Key> can not be null");

        for (Key k : coll) {
            addFalsePositive(k);
        }
    }

    /**
     * Adds a list of false positive information to <i>this</i> retouched Bloom filter.
     *
     * @param keys The list of false positive.
     */
    public void addFalsePositive(@NonNull List<Key> keys) {
        Preconditions.checkNotNull(keys, "\"ArrayList<Key> can not be null");

        for (Key k : keys) {
            addFalsePositive(k);
        }
    }

    /**
     * Adds an array of false positive information to <i>this</i> retouched Bloom filter.
     *
     * @param keys The array of false positive.
     */
    public void addFalsePositive(@NonNull Key[] keys) {
        Preconditions.checkNotNull(keys, "\"Key[] can not be null");


        for (Key key : keys) {
            addFalsePositive(key);
        }
    }

    /**
     * Performs the selective clearing for a given key.
     *
     * @param k      The false positive key to remove from <i>this</i> retouched Bloom filter.
     * @param scheme The selective clearing scheme to apply.
     */
    public void selectiveClearing(@NonNull Key k, short scheme) {
        Preconditions.checkNotNull(k, "\"Key can not be null");

        if (!membershipTest(k)) {
            throw new IllegalArgumentException("Key is not a member");
        }

        int index;
        int[] h = hash.hash(k);

        switch (scheme) {

            case RANDOM:
                index = randomRemove();
                break;

            case MINIMUM_FN:
                index = minimumFnRemove(h);
                break;

            case MAXIMUM_FP:
                index = maximumFpRemove(h);
                break;

            case RATIO:
                index = ratioRemove(h);
                break;

            default:
                throw new AssertionError("Undefined selective clearing scheme");

        }

        clearBit(index);
    }

    private int randomRemove() {
        if (rand == null) {
            rand = Randoms.ofSecure();
        }

        return rand.nextInt(nbHash);
    }

    /**
     * Chooses the bit position that minimizes the number of false negative generated.
     *
     * @param h The different bit positions.
     * @return The position that minimizes the number of false negative generated.
     */
    private int minimumFnRemove(int[] h) {
        int minIndex = Integer.MAX_VALUE;
        double minValue = Double.MAX_VALUE;

        for (int i = 0; i < nbHash; i++) {
            double keyWeight = getWeight(keyVector[h[i]]);

            if (keyWeight < minValue) {
                minIndex = h[i];
                minValue = keyWeight;
            }

        }

        return minIndex;
    }

    /**
     * Chooses the bit position that maximizes the number of false positive removed.
     *
     * @param h The different bit positions.
     * @return The position that maximizes the number of false positive removed.
     */
    private int maximumFpRemove(int[] h) {
        int maxIndex = Integer.MIN_VALUE;
        double maxValue = Double.MIN_VALUE;

        for (int i = 0; i < nbHash; i++) {
            double fpWeight = getWeight(fpVector[h[i]]);

            if (fpWeight > maxValue) {
                maxValue = fpWeight;
                maxIndex = h[i];
            }
        }

        return maxIndex;
    }

    /**
     * Chooses the bit position that minimizes the number of false negative generated while maximizing.
     * the number of false positive removed.
     *
     * @param h The different bit positions.
     * @return The position that minimizes the number of false negative generated while maximizing.
     */
    private int ratioRemove(int[] h) {
        computeRatio();
        int minIndex = Integer.MAX_VALUE;
        double minValue = Double.MAX_VALUE;

        for (int i = 0; i < nbHash; i++) {
            if (ratio[h[i]] < minValue) {
                minValue = ratio[h[i]];
                minIndex = h[i];
            }
        }

        return minIndex;
    }

    /**
     * Clears a specified bit in the bit vector and keeps up-to-date the KeyList vectors.
     *
     * @param index The position of the bit to clear.
     */
    private void clearBit(int index) {
        if (index < 0 || index >= vectorSize) {
            throw new ArrayIndexOutOfBoundsException(index);
        }

        List<Key> kl = keyVector[index];
        List<Key> fpl = fpVector[index];

        // update key list
        int listSize = kl.size();
        for (int i = 0; i < listSize && !kl.isEmpty(); i++) {
            removeKey(kl.get(0), keyVector);
        }

        kl.clear();
        keyVector[index].clear();

        //update false positive list
        listSize = fpl.size();
        for (int i = 0; i < listSize && !fpl.isEmpty(); i++) {
            removeKey(fpl.get(0), fpVector);
        }

        fpl.clear();
        fpVector[index].clear();

        //update ratio
        ratio[index] = 0.0;

        //update bit vector
        bits.clear(index);
    }

    /**
     * Removes a given key from <i>this</i> filer.
     *
     * @param k      The key to remove.
     * @param vector The counting vector associated to the key.
     */
    private void removeKey(@NonNull Key k, @NonNull List<Key>[] vector) {
        Preconditions.checkNotNull(k, "Key can not be null");
        Preconditions.checkNotNull(vector, "ArrayList<Key>[] can not be null");
        int[] h = hash.hash(k);
        hash.clear();

        for (int i = 0; i < nbHash; i++) {
            vector[h[i]].remove(k);
        }
    }

    /**
     * Computes the ratio A/FP.
     */
    private void computeRatio() {
        for (int i = 0; i < vectorSize; i++) {
            double keyWeight = getWeight(keyVector[i]);
            double fpWeight = getWeight(fpVector[i]);

            if (keyWeight > 0 && fpWeight > 0) {
                ratio[i] = keyWeight / fpWeight;
            }
        }
    }

    private double getWeight(List<Key> keyList) {
        double weight = 0.0;
        for (Key k : keyList) {
            weight += k.getWeight();
        }
        return weight;
    }

    /**
     * Creates and initialises the various vectors.
     */
    @SuppressWarnings("unchecked")
    private void createVector() {
        fpVector = new List[vectorSize];
        keyVector = new List[vectorSize];
        ratio = new double[vectorSize];

        for (int i = 0; i < vectorSize; i++) {
            fpVector[i] = Collections.synchronizedList(new ArrayList<Key>());
            keyVector[i] = Collections.synchronizedList(new ArrayList<Key>());
            ratio[i] = 0.0;
        }
    }

    // Writable

    @Override
    public void write(DataOutput out) throws IOException {
        super.write(out);
        for (List<Key> list : fpVector) {
            out.writeInt(list.size());
            for (Key k : list) {
                k.write(out);
            }
        }
        for (List<Key> list : keyVector) {
            out.writeInt(list.size());
            for (Key k : list) {
                k.write(out);
            }
        }
        for (double v : ratio) {
            out.writeDouble(v);
        }
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        super.readFields(in);
        createVector();
        for (List<Key> list : fpVector) {
            int size = in.readInt();
            for (int j = 0; j < size; j++) {
                Key k = new Key();
                k.readFields(in);
                list.add(k);
            }
        }
        for (List<Key> list : keyVector) {
            int size = in.readInt();
            for (int j = 0; j < size; j++) {
                Key k = new Key();
                k.readFields(in);
                list.add(k);
            }
        }
        for (int i = 0; i < ratio.length; i++) {
            ratio[i] = in.readDouble();
        }
    }
}
