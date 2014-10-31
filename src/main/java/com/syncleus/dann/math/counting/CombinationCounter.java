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
 * http://www.merriampark.com/comb.htm as of 4/20/2010.
 */
package com.syncleus.dann.math.counting;

import java.math.BigInteger;

public class CombinationCounter implements Counter {
    private final int[] currentCombination;
    private final int setSize;
    private final int combinationSize;
    private final BigInteger total;
    private BigInteger remaining;

    public CombinationCounter(final int setSize, final int combinationSize) {
        if (combinationSize > setSize)
            throw new IllegalArgumentException("combinationSize can not be larger than setSize");
        if (setSize < 0)
            throw new IllegalArgumentException("setSize can not be negative");
        if (combinationSize < 0)
            throw new IllegalArgumentException("combinationSize can not be negative");

        this.setSize = setSize;
        this.combinationSize = combinationSize;
        this.currentCombination = new int[combinationSize];
        final BigInteger nFact = calculateFactorial(setSize);
        final BigInteger rFact = calculateFactorial(combinationSize);
        final BigInteger nminusrFact = calculateFactorial(setSize - combinationSize);
        this.total = (setSize == 0 || combinationSize == 0 ? BigInteger.ZERO : nFact.divide(rFact.multiply(nminusrFact)));
        this.reset();
    }

    static BigInteger calculateFactorial(final int number) {
        BigInteger fact = BigInteger.ONE;
        for (int i = number; i > 1; i--)
            fact = fact.multiply(new BigInteger(Integer.toString(i)));
        return fact;
    }

    @Override
    public final void reset() {
        for (int i = 0; i < this.currentCombination.length; i++)
            this.currentCombination[i] = i;
        this.remaining = new BigInteger(this.total.toString());
    }

    @Override
    public BigInteger getRemaining() {
        return this.remaining;
    }

    @Override
    public BigInteger getTotal() {
        return this.total;
    }

    @Override
    public boolean hasMore() {
        return this.remaining.compareTo(BigInteger.ZERO) == 1;
    }

    //--------------------------------------------------------
    // Generate next combination (algorithm from Rosen p. 286)
    //--------------------------------------------------------
    @Override
    public int[] getNext() {
        if (this.remaining.equals(this.total)) {
            this.remaining = this.remaining.subtract(BigInteger.ONE);
            return this.currentCombination.clone();
        }
        int i = this.combinationSize - 1;
        while (this.currentCombination[i] == (this.setSize - this.combinationSize + i))
            i--;
        this.currentCombination[i] = this.currentCombination[i] + 1;
        for (int j = i + 1; j < this.combinationSize; j++)
            this.currentCombination[j] = this.currentCombination[i] + j - i;
        this.remaining = this.remaining.subtract(BigInteger.ONE);
        return this.currentCombination.clone();
    }
}
