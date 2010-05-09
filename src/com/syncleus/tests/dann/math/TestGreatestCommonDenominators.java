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
package com.syncleus.tests.dann.math;
import com.syncleus.dann.math.GreatestCommonDenominators;
import org.junit.Assert;
import org.junit.Test;
public class TestGreatestCommonDenominators
{
	private static final long[][] solutions = {	{20l, 90l, 10l},
											{19l, 187l, 1l},
											{5l, 1867l, 1l},
											{20l, 100l, 20l},
											{5l, 0l, 5l},
											{6l, 21l, 3l},
											{4l, 42l, 2l},
											{7l, 11l, 1l},
											{1l, 2l, 1l},
											{1l, 1l, 1l},
											{1l, 0l, 1l},
											{0l, 1l, 1l} };

	@Test
	public void testBinaryGCD()
	{
		long firstMultiply = 1;
		long secondMultiply = 1;
		long gcd;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.binaryGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]);
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}

		firstMultiply = -1;
		secondMultiply = 1;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.binaryGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]);
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}

		firstMultiply = 1;
		secondMultiply = -1;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.binaryGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]);
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}

		firstMultiply = -1;
		secondMultiply = -1;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.binaryGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]);
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}
	}

	@Test
	public void testEuclideanGCD()
	{
		long firstMultiply = 1;
		long secondMultiply = 1;
		long gcd;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.euclideanGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]);
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}

		firstMultiply = -1;
		secondMultiply = 1;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.euclideanGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]);
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}

		firstMultiply = 1;
		secondMultiply = -1;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.euclideanGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]);
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}

		firstMultiply = -1;
		secondMultiply = -1;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.euclideanGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]);
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}
	}

	@Test
	public void testExtendedEuclideanGCD()
	{
		long firstMultiply = 1;
		long secondMultiply = 1;
		long gcd;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.extendedEuclideanGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]).getGreatestCommonDenominator();
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}

		firstMultiply = -1;
		secondMultiply = 1;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.extendedEuclideanGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]).getGreatestCommonDenominator();
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}

		firstMultiply = 1;
		secondMultiply = -1;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.extendedEuclideanGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]).getGreatestCommonDenominator();
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}

		firstMultiply = -1;
		secondMultiply = -1;
		for(int solutionIndex = 0; solutionIndex < solutions.length; solutionIndex++)
		{
			gcd = GreatestCommonDenominators.extendedEuclideanGCD(firstMultiply*solutions[solutionIndex][0],secondMultiply*solutions[solutionIndex][1]).getGreatestCommonDenominator();
			Assert.assertTrue("first:" + firstMultiply*solutions[solutionIndex][0] + " second:" + secondMultiply*solutions[solutionIndex][1] + " GCD:" + gcd + " expected:" + solutions[solutionIndex][2], gcd == solutions[solutionIndex][2]);
		}
	}
}
