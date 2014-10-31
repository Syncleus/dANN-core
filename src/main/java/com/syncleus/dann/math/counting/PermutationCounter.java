/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/

/*
 * Derived from Public-Domain source as indicated at
 * http://www.merriampark.com/perm.htm as of 5/7/2010.
 */
package com.syncleus.dann.math.counting;

public class PermutationCounter extends AbstractPermutationCounter {
    public PermutationCounter(final int permutationSize) {
        this(permutationSize, permutationSize);
    }

    /**
     * Constructor. WARNING: Don't make permutationSize too large.
     * Recall that the number of permutations is permutationSize!
     * which can be very large, even when permutationSize is as small as 20 --
     * 20! = 2,432,902,008,176,640,000 and
     * 21! is too big to fit into permutation Java long, which is
     * why we use BigInteger instead.
     */
    public PermutationCounter(final int setSize, final int permutationSize) {
        super(setSize, permutationSize);

        this.reset();
    }

    /**
     * Generates the next permutation (algorithm from Rosen p. 284)
     */
    @Override
    protected boolean next() {
        int temp;
        final int[] perm = this.getPermutation();
        // Find largest index j with perm[j] < perm[j+1]
        int j = perm.length - 2;
        while (perm[j] > perm[j + 1])
            j--;
        // Find index k such that permutation[k] is smallest integer
        // greater than permutation[j] to the right of permutation[j]
        int k = this.getPermutation().length - 1;
        while (perm[j] > perm[k])
            k--;
        // Interchange permutation[j] and permutation[k]
        temp = perm[k];
        perm[k] = perm[j];
        perm[j] = temp;
        // Put tail end of permutation after jth position in increasing order
        int r = this.getPermutation().length - 1;
        int s = j + 1;
        while (r > s) {
            temp = perm[s];
            perm[s] = perm[r];
            perm[r] = temp;
            r--;
            s++;
        }

        return true;
    }
}
