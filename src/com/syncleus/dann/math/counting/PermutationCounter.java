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

/*
 ** Derived from Public-Domain source as indicated at
 ** http://www.merriampark.com/perm.htm as of 5/7/2010.
 */
package com.syncleus.dann.math.counting;

import java.math.BigInteger;

public class PermutationCounter implements Counter
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

	public PermutationCounter(int permutationSize)
	{
		this(permutationSize, permutationSize);
	}

	//-----------------------------------------------------------
	// Constructor. WARNING: Don't make permutationSize too large.
	// Recall that the number of permutations is permutationSize!
	// which can be very large, even when permutationSize is as small as 20 --
	// 20! = 2,432,902,008,176,640,000 and
	// 21! is too big to fit into permutation Java long, which is
	// why we use BigInteger instead.
	//----------------------------------------------------------
	public PermutationCounter(int setSize, int permutationSize)
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

	//--------------------------------------------------------
	// Generate next permutation (algorithm from Rosen p. 284)
	//--------------------------------------------------------
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

		int temp;

		// Find largest index j with permutation[j] < permutation[j+1]

		int j = permutation.length - 2;
		while(permutation[j] > permutation[j + 1])
			j--;

		// Find index k such that permutation[k] is smallest integer
		// greater than permutation[j] to the right of permutation[j]

		int k = permutation.length - 1;
		while(permutation[j] > permutation[k])
			k--;

		// Interchange permutation[j] and permutation[k]

		temp = permutation[k];
		permutation[k] = permutation[j];
		permutation[j] = temp;

		// Put tail end of permutation after jth position in increasing order

		int r = permutation.length - 1;
		int s = j + 1;

		while(r > s)
		{
			temp = permutation[s];
			permutation[s] = permutation[r];
			permutation[r] = temp;
			r--;
			s++;
		}

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
}
