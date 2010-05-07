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
 ** Derived from Public-Domain source as indicated at
 ** http://www.merriampark.com/perm.htm as of 5/7/2010.
 */
package com.syncleus.dann.math.counting;

import java.math.BigInteger;

public class PermutationCounter implements Counter
{
	private int[] a;
	private BigInteger remaining;
	private BigInteger total;

	//-----------------------------------------------------------
	// Constructor. WARNING: Don't make length too large.
	// Recall that the number of permutations is length!
	// which can be very large, even when length is as small as 20 --
	// 20! = 2,432,902,008,176,640,000 and
	// 21! is too big to fit into a Java long, which is
	// why we use BigInteger instead.
	//----------------------------------------------------------
	public PermutationCounter(int length)
	{
		if(length < 1)
			throw new IllegalArgumentException("Min 1");
		a = new int[length];
		total = getFactorial(length);
		reset();
	}

	public void reset()
	{
		for(int i = 0; i < a.length; i++)
			a[i] = i;
		remaining = new BigInteger(total.toString());
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

	//--------------------------------------------------------
	// Generate next permutation (algorithm from Rosen p. 284)
	//--------------------------------------------------------
	public int[] getNext()
	{

		if(remaining.equals(total))
		{
			remaining = remaining.subtract(BigInteger.ONE);
			return a;
		}

		int temp;

		// Find largest index j with a[j] < a[j+1]

		int j = a.length - 2;
		while(a[j] > a[j + 1])
			j--;

		// Find index k such that a[k] is smallest integer
		// greater than a[j] to the right of a[j]

		int k = a.length - 1;
		while(a[j] > a[k])
			k--;

		// Interchange a[j] and a[k]

		temp = a[k];
		a[k] = a[j];
		a[j] = temp;

		// Put tail end of permutation after jth position in increasing order

		int r = a.length - 1;
		int s = j + 1;

		while(r > s)
		{
			temp = a[s];
			a[s] = a[r];
			a[r] = temp;
			r--;
			s++;
		}

		remaining = remaining.subtract(BigInteger.ONE);
		return a;

	}
}
