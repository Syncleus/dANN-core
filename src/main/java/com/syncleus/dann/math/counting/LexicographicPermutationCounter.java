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

public class LexicographicPermutationCounter extends AbstractPermutationCounter
{
	public LexicographicPermutationCounter(final int permutationSize)
	{
		this(permutationSize, permutationSize);
	}

	public LexicographicPermutationCounter(final int setSize, final int permutationSize)
	{
		super(setSize, permutationSize);

		reset();
	}

	private static void swap(final int[] permutation, final int firstIndex, final int secondIndex)
	{
		final int first = permutation[firstIndex];
		permutation[firstIndex] = permutation[secondIndex];
		permutation[secondIndex] = first;
	}

	@Override
	protected boolean next()
	{
		final int[] perm = this.getPermutation();

		if( perm.length == 1 )
			return false;

		int permutationIndex;
		for(permutationIndex = perm.length - 2; permutationIndex >= 0; permutationIndex--)
			if( perm[permutationIndex] < perm[permutationIndex + 1] )
				break;

		int swapIndex = perm.length - 1;
		while( perm[permutationIndex] > perm[swapIndex] )
			swapIndex--;
		swap(perm,swapIndex, permutationIndex);

		for(int firstSwap = perm.length - 1, secondSwap = permutationIndex + 1; firstSwap > secondSwap; firstSwap--, secondSwap++)
			swap(perm,firstSwap, secondSwap);

		return true;
	}
}
