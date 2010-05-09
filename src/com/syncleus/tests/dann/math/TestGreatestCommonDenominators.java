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

import org.junit.Test;

public class TestGreatestCommonDenominators
{
	private static final long[][] SOLUTIONS = {{20L, 90L, 10L},
			{19L, 187L, 1L},
			{5L, 1867L, 1L},
			{20L, 100L, 20L},
			{5L, 0L, 5L},
			{6L, 21L, 3L},
			{4L, 42L, 2L},
			{7L, 11L, 1L},
			{1L, 2L, 1L},
			{1L, 1L, 1L},
			{1L, 0L, 1L},
			{0L, 1L, 1L}};

	@Test
	public void testBinaryGCD()
	{
		long firstMultiply = 1;
		long secondMultiply = 1;
		long gcd;
		for(long[] solution3 : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.binaryGCD(firstMultiply * solution3[0], secondMultiply * solution3[1]);
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution3[0] + " second:" + secondMultiply * solution3[1] + " GCD:" + gcd + " expected:" + solution3[2], gcd == solution3[2]);
		}
		firstMultiply = -1;
		secondMultiply = 1;
		for(long[] solution2 : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.binaryGCD(firstMultiply * solution2[0], secondMultiply * solution2[1]);
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution2[0] + " second:" + secondMultiply * solution2[1] + " GCD:" + gcd + " expected:" + solution2[2], gcd == solution2[2]);
		}
		firstMultiply = 1;
		secondMultiply = -1;
		for(long[] solution1 : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.binaryGCD(firstMultiply * solution1[0], secondMultiply * solution1[1]);
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution1[0] + " second:" + secondMultiply * solution1[1] + " GCD:" + gcd + " expected:" + solution1[2], gcd == solution1[2]);
		}
		firstMultiply = -1;
		secondMultiply = -1;
		for(long[] solution : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.binaryGCD(firstMultiply * solution[0], secondMultiply * solution[1]);
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution[0] + " second:" + secondMultiply * solution[1] + " GCD:" + gcd + " expected:" + solution[2], gcd == solution[2]);
		}
	}

	@Test
	public void testEuclideanGCD()
	{
		long firstMultiply = 1;
		long secondMultiply = 1;
		long gcd;
		for(long[] solution3 : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.euclideanGCD(firstMultiply * solution3[0], secondMultiply * solution3[1]);
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution3[0] + " second:" + secondMultiply * solution3[1] + " GCD:" + gcd + " expected:" + solution3[2], gcd == solution3[2]);
		}
		firstMultiply = -1;
		secondMultiply = 1;
		for(long[] solution2 : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.euclideanGCD(firstMultiply * solution2[0], secondMultiply * solution2[1]);
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution2[0] + " second:" + secondMultiply * solution2[1] + " GCD:" + gcd + " expected:" + solution2[2], gcd == solution2[2]);
		}
		firstMultiply = 1;
		secondMultiply = -1;
		for(long[] solution1 : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.euclideanGCD(firstMultiply * solution1[0], secondMultiply * solution1[1]);
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution1[0] + " second:" + secondMultiply * solution1[1] + " GCD:" + gcd + " expected:" + solution1[2], gcd == solution1[2]);
		}
		firstMultiply = -1;
		secondMultiply = -1;
		for(long[] solution : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.euclideanGCD(firstMultiply * solution[0], secondMultiply * solution[1]);
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution[0] + " second:" + secondMultiply * solution[1] + " GCD:" + gcd + " expected:" + solution[2], gcd == solution[2]);
		}
	}

	@Test
	public void testExtendedEuclideanGCD()
	{
		long firstMultiply = 1;
		long secondMultiply = 1;
		long gcd;
		for(long[] solution3 : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.extendedEuclideanGCD(firstMultiply * solution3[0], secondMultiply * solution3[1]).getGreatestCommonDenominator();
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution3[0] + " second:" + secondMultiply * solution3[1] + " GCD:" + gcd + " expected:" + solution3[2], gcd == solution3[2]);
		}
		firstMultiply = -1;
		secondMultiply = 1;
		for(long[] solution2 : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.extendedEuclideanGCD(firstMultiply * solution2[0], secondMultiply * solution2[1]).getGreatestCommonDenominator();
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution2[0] + " second:" + secondMultiply * solution2[1] + " GCD:" + gcd + " expected:" + solution2[2], gcd == solution2[2]);
		}
		firstMultiply = 1;
		secondMultiply = -1;
		for(long[] solution1 : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.extendedEuclideanGCD(firstMultiply * solution1[0], secondMultiply * solution1[1]).getGreatestCommonDenominator();
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution1[0] + " second:" + secondMultiply * solution1[1] + " GCD:" + gcd + " expected:" + solution1[2], gcd == solution1[2]);
		}
		firstMultiply = -1;
		secondMultiply = -1;
		for(long[] solution : SOLUTIONS)
		{
			gcd = com.syncleus.dann.math.GreatestCommonDenominators.extendedEuclideanGCD(firstMultiply * solution[0], secondMultiply * solution[1]).getGreatestCommonDenominator();
			org.junit.Assert.assertTrue("first:" + firstMultiply * solution[0] + " second:" + secondMultiply * solution[1] + " GCD:" + gcd + " expected:" + solution[2], gcd == solution[2]);
		}
	}
}
