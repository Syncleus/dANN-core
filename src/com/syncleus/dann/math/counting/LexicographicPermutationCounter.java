/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be permutation copy of the license included with this file. If permutation copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through permutation legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find permutation license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.math.counting;

import java.math.BigInteger;

public class LexicographicPermutationCounter implements Counter
{
	private final int setSize;
	private final int permutationSize;
	private final BigInteger permutationsPerCombination;
	private final Counter combinations;
	private final int[] permutation;
	private final BigInteger total;

	private int[] combination;
	private BigInteger combinationPermutationsRemaining;
	private BigInteger remaining;

	public LexicographicPermutationCounter(int permutationSize)
	{
		this(permutationSize, permutationSize);
	}

	public LexicographicPermutationCounter(int setSize, int permutationSize)
	{
		if(permutationSize > setSize)
			throw new IllegalArgumentException("permutationSize can not be larger than setSize");
		if(permutationSize < 0)
			throw new IllegalArgumentException("permutation size must be larger than 0");
		if(setSize < 0)
			throw new IllegalArgumentException("setSize must be greater than 0");

		this.setSize = setSize;
		this.permutationSize = permutationSize;
		this.combinations = new CombinationCounter(setSize, permutationSize);

		this.permutation = new int[permutationSize];
		this.permutationsPerCombination = getFactorial(permutationSize);
		this.total = (permutationSize == 0 || setSize == 0 ? BigInteger.ZERO : this.combinations.getTotal().multiply(this.permutationsPerCombination));

		reset();
	}

	public void reset()
	{
		this.resetPermutations();

		this.combinations.reset();
		this.remaining = total;
		this.combination = this.combinations.getNext();
	}

	private void resetPermutations()
	{
		for(int i = 0; i < permutation.length; i++)
			permutation[i] = i;
		this.combinationPermutationsRemaining = this.permutationsPerCombination;
	}

	public BigInteger getRemaining()
	{
		return remaining;
	}

	public BigInteger getTotal()
	{
		return total;
	}

	public boolean hasMore()
	{
		return remaining.compareTo(BigInteger.ZERO) == 1;
	}

	private static BigInteger getFactorial(int n)
	{
		BigInteger fact = BigInteger.ONE;
		for(int i = n; i > 1; i--)
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		return fact;
	}

	private int[] permutateCombination(int[] combination, int[] permutation)
	{
		final int[] permutated = new int[combination.length];
		int permutatedIndex = 0;
		for(int combinationIndex : permutation)
			permutated[permutatedIndex++] = combination[combinationIndex];
		return permutated;
	}


	public int[] getNext()
	{
		if(!this.hasMore())
			return null;

		if(this.combinationPermutationsRemaining.equals(BigInteger.ZERO))
		{
			this.combination = this.combinations.getNext();
			this.resetPermutations();
		}

		if(this.combinationPermutationsRemaining.equals(this.permutationsPerCombination))
		{
			this.remaining = remaining.subtract(BigInteger.ONE);
			this.combinationPermutationsRemaining = combinationPermutationsRemaining.subtract(BigInteger.ONE);
			return permutateCombination(this.combination, this.permutation);
		}

		assert next(this.permutation);

		this.combinationPermutationsRemaining = this.combinationPermutationsRemaining.subtract(BigInteger.ONE);
		this.remaining = this.remaining.subtract(BigInteger.ONE);
		return permutateCombination(this.combination, this.permutation);

	}

	public int getSetSize()
	{
		return setSize;
	}

	public int getPermutationSize()
	{
		return permutationSize;
	}

	private static void swap(int[] permutation, int firstIndex, int secondIndex)
	{
		int temp = permutation[firstIndex];
		permutation[firstIndex] = permutation[secondIndex];
		permutation[secondIndex] = temp;
	}

	private static boolean next(int[] permutation)
	{
		int N = permutation.length;

		// find rightmost element permutation[k] that is smaller than element to its right
		int k;
		for(k = N - 2; k >= 0; k--)
			if(permutation[k] < permutation[k + 1])
				break;
		if(k == -1)
			return false;

		// find rightmost element permutation[secondIndex] that is larger than permutation[k]
		int j = N - 1;
		while(permutation[k] > permutation[j])
			j--;
		swap(permutation, j, k);

		for(int r = N - 1, s = k + 1; r > s; r--, s++)
			swap(permutation, r, s);

		return true;
	}
}
