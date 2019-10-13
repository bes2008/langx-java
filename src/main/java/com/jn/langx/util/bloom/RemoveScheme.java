package com.jn.langx.util.bloom;


/**
 * Defines the different remove scheme for retouched Bloom filters.
 * <p>
 * Originally created by
 * <a href="http://www.one-lab.org">European Commission One-Lab Project 034819</a>.
 */
public interface RemoveScheme {
    /**
     * Random selection.
     * <p>
     * The idea is to randomly select a bit to reset.
     */
    public final static short RANDOM = 0;

    /**
     * MinimumFN Selection.
     * <p>
     * The idea is to select the bit to reset that will generate the minimum
     * number of false negative.
     */
    public final static short MINIMUM_FN = 1;

    /**
     * MaximumFP Selection.
     * <p>
     * The idea is to select the bit to reset that will remove the maximum number
     * of false positive.
     */
    public final static short MAXIMUM_FP = 2;

    /**
     * Ratio Selection.
     * <p>
     * The idea is to select the bit to reset that will, at the same time, remove
     * the maximum number of false positve while minimizing the amount of false
     * negative generated.
     */
    public final static short RATIO = 3;
}
