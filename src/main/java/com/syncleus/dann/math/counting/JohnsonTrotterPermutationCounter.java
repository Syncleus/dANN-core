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
package com.syncleus.dann.math.counting;

public class JohnsonTrotterPermutationCounter extends AbstractPermutationCounter {
    /**
     * -1: left, 1: right
     */
    private final int[] mobility;

    public JohnsonTrotterPermutationCounter(final int permutationSize) {
        this(permutationSize, permutationSize);
    }

    public JohnsonTrotterPermutationCounter(final int setSize, final int permutationSize) {
        super(setSize, permutationSize);

        this.mobility = new int[permutationSize];

        this.reset();
    }

    private static boolean isMobile(final int[] permutation, final int[] mobility, final int index) {
        final int mobilityValue = mobility[index];

        if ((index == 0) && (mobilityValue < 0))
            return false;
        if ((index == permutation.length - 1) && (mobilityValue > 0))
            return false;

        return (permutation[index] > permutation[index + mobility[index]]);
    }

    private static void swap(final int[] permutation, final int firstIndex, final int secondIndex) {
        final int first = permutation[firstIndex];
        permutation[firstIndex] = permutation[secondIndex];
        permutation[secondIndex] = first;
    }

    private static int largestMobileIndex(final int[] permutation, final int[] mobility) {
        for (int index = mobility.length - 1; index >= 0; index--)
            if (isMobile(permutation, mobility, index))
                return index;
        return -1;
    }

    @Override
    protected void resetPermutations() {
        super.resetPermutations();
        for (int i = 0; i < this.getPermutationSize(); i++) {
            this.mobility[i] = -1;
        }
    }

    @Override
    protected boolean next() {
        final int[] perm = this.getPermutation();
        final int largestIndex = largestMobileIndex(perm, this.mobility);
        if (largestIndex < 0)
            return false;

        final int swapedValue = perm[largestIndex];
        swap(perm, largestIndex, largestIndex + this.mobility[largestIndex]);
        swap(this.mobility, largestIndex, largestIndex + this.mobility[largestIndex]);

        for (int index = 0; index < this.getPermutationSize(); index++)
            if (perm[index] > swapedValue)
                this.mobility[index] *= -1;

        return true;
    }
}
