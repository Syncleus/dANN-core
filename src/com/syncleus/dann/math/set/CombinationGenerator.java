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
 ** http://www.merriampark.com/comb.htm as of 4/20/2010.
 */
package com.syncleus.dann.math.set;

import java.math.BigInteger;

public class CombinationGenerator
{
	private int[] currentCombination;
	private int setSize;
	private int combinationSize;
	private BigInteger numLeft;
	private BigInteger total;

	//------------
	// Constructor
	//------------
	public CombinationGenerator(int setSize, int combinationSize)
	{
		if(combinationSize > setSize)
			throw new IllegalArgumentException();
		if(setSize < 1)
			throw new IllegalArgumentException();
		this.setSize = setSize;
		this.combinationSize = combinationSize;
		currentCombination = new int[combinationSize];
		BigInteger nFact = getFactorial(setSize);
		BigInteger rFact = getFactorial(combinationSize);
		BigInteger nminusrFact = getFactorial(setSize - combinationSize);
		total = nFact.divide(rFact.multiply(nminusrFact));
		reset();
	}

	//------
	// Reset
	//------
	public void reset()
	{
		for(int i = 0; i < currentCombination.length; i++)
			currentCombination[i] = i;
		numLeft = new BigInteger(total.toString());
	}

	//------------------------------------------------
	// Return number of combinations not yet generated
	//------------------------------------------------
	public BigInteger getRemaining()
	{
		return numLeft;
	}

	//-----------------------------
	// Are there more combinations?
	//-----------------------------
	public boolean hasMore()
	{
		return numLeft.compareTo(BigInteger.ZERO) == 1;
	}

	//------------------------------------
	// Return total number of combinations
	//------------------------------------
	public BigInteger getTotal()
	{
		return total;
	}

	//------------------
	// Compute factorial
	//------------------
	private static BigInteger getFactorial(int n)
	{
		BigInteger fact = BigInteger.ONE;
		for(int i = n; i > 1; i--)
			fact = fact.multiply(new BigInteger(Integer.toString(i)));
		return fact;
	}

	//--------------------------------------------------------
	// Generate next combination (algorithm from Rosen p. 286)
	//--------------------------------------------------------
	public int[] getNext()
	{

		if(numLeft.equals(total))
		{
			numLeft = numLeft.subtract(BigInteger.ONE);
			return currentCombination.clone();
		}

		int i = combinationSize - 1;
		while(currentCombination[i] == setSize - combinationSize + i)
			i--;
		currentCombination[i] = currentCombination[i] + 1;
		for(int j = i + 1; j < combinationSize; j++)
			currentCombination[j] = currentCombination[i] + j - i;

		numLeft = numLeft.subtract(BigInteger.ONE);
		return currentCombination.clone();
	}
}
