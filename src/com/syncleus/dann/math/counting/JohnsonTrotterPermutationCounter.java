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

import java.math.BigInteger;

public class JohnsonTrotterPermutationCounter implements Counter
{
	private final int setSize;
	private final int permutationSize;
	private final BigInteger permutationsPerCombination;
	private final BigInteger total;
	private final Counter combinations;
	private final int[] permutation;
	// -1: left, 1: right
	private final int[] mobility;
	private int[] combination;
	private BigInteger combinationPermutationsRemaining;
	private BigInteger remaining;

	public JohnsonTrotterPermutationCounter(final int permutationSize)
	{
		this(permutationSize, permutationSize);
	}

	public JohnsonTrotterPermutationCounter(final int setSize, final int permutationSize)
	{
		if (permutationSize > setSize)
			throw new IllegalArgumentException("permutationSize can not be larger than setSize");
		if (permutationSize < 0)
			throw new IllegalArgumentException("permutation size must be larger than 0");
		if (setSize < 0)
			throw new IllegalArgumentException("setSize must be greater than 0");
		this.setSize = setSize;
		this.permutationSize = permutationSize;
		this.combinations = new CombinationCounter(setSize, permutationSize);
		this.permutation = new int[permutationSize];
		this.mobility = new int[permutationSize];
		this.permutationsPerCombination = getFactorial(permutationSize);
		this.total = (permutationSize == 0 || setSize == 0 ? BigInteger.ZERO : this.combinations.getTotal().multiply(this.permutationsPerCombination));
		reset();
	}

	public void reset()
	{
		this.resetPermutations();
		this.combinations.reset();
		this.remaining = this.total;
		this.combination = this.combinations.getNext();
	}

	private void resetPermutations()
	{
		for(int i = 0; i < this.permutationSize; i++)
		{
			this.mobility[i] = -1;
			this.permutation[i] = i;
		}
		this.combinationPermutationsRemaining = this.permutationsPerCombination;
	}

	public BigInteger getRemaining()
	{
		return this.remaining;
	}

	public BigInteger getTotal()
	{
		return this.total;
	}

	public boolean hasMore()
	{
		return remaining.compareTo(BigInteger.ZERO) == 1;
	}

	private static BigInteger getFactorial(final int n)
	{
		BigInteger fact = BigInteger.ONE;
		for(int i = n; i > 1; i--)
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		return fact;
	}

	private static int[] permutateCombination(final int[] combination, final int[] permutation)
	{
		final int[] permutated = new int[combination.length];
		int permutatedIndex = 0;
		for(final int combinationIndex : permutation)
			permutated[permutatedIndex++] = combination[combinationIndex];
		return permutated;
	}

	public int[] getNext()
	{
		if (!this.hasMore())
			return null;
		if (this.combinationPermutationsRemaining.equals(BigInteger.ZERO))
		{
			this.combination = this.combinations.getNext();
			this.resetPermutations();
		}
		if (this.combinationPermutationsRemaining.equals(this.permutationsPerCombination))
		{
			this.remaining = remaining.subtract(BigInteger.ONE);
			this.combinationPermutationsRemaining = combinationPermutationsRemaining.subtract(BigInteger.ONE);
			return permutateCombination(this.combination, this.permutation);
		}
		assert next(this.permutation, this.mobility);
		this.combinationPermutationsRemaining = this.combinationPermutationsRemaining.subtract(BigInteger.ONE);
		this.remaining = this.remaining.subtract(BigInteger.ONE);
		return permutateCombination(this.combination, this.permutation);
	}

	public int getSetSize()
	{
		return this.setSize;
	}

	public int getPermutationSize()
	{
		return this.permutationSize;
	}

	private static boolean isMobile(final int[] permutation, final int[] mobility, final int index)
	{
		final int mobilityValue = mobility[index];
		if ((index == 0) && (mobilityValue < 0))
			return false;
		if ((index == permutation.length - 1) && (mobilityValue > 0))
			return false;
		return (permutation[index] > permutation[index + mobility[index]]);
	}

	private static void swap(final int[] permutation, final int firstIndex, final int secondIndex)
	{
		final int first = permutation[firstIndex];
		permutation[firstIndex] = permutation[secondIndex];
		permutation[secondIndex] = first;
	}

	private static int largestMobileIndex(final int[] permutation, final int[] mobility)
	{
		for(int index = mobility.length - 1; index >= 0; index--)
			if (isMobile(permutation, mobility, index))
				return index;
		return -1;
	}

	private static boolean next(final int[] permutation, final int[] mobility)
	{
		final int largestIndex = largestMobileIndex(permutation, mobility);
		if (largestIndex < 0)
			return false;
		final int swapedValue = permutation[largestIndex];
		swap(permutation, largestIndex, largestIndex + mobility[largestIndex]);
		swap(mobility, largestIndex, largestIndex + mobility[largestIndex]);
		for(int index = 0; index < permutation.length; index++)
			if (permutation[index] > swapedValue)
				mobility[index] *= -1;
		return true;
	}
}
