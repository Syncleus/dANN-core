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
package com.syncleus.tests.dann.genetics;

import com.syncleus.dann.genetics.MutableDouble;
import org.junit.*;

public class TestMutableDouble
{
	@Test
	public void testConstructors()
	{
		MutableDouble test = new MutableDouble(123.0);
		Assert.assertTrue("value constructor failed", Math.abs(test.getNumber().doubleValue() - 123.0) < 0.000001);
		test = new MutableDouble("456");
		Assert.assertTrue("string value constructor failed", Math.abs(test.getNumber().doubleValue() - 456.0) < 0.000001);
		test = new MutableDouble(Double.valueOf(789));
		Assert.assertTrue("Number value constructor failed", Math.abs(test.getNumber().doubleValue() - 789.0) < 0.000001);
	}

	@Test
	public void testMax()
	{
		final MutableDouble highValue = new MutableDouble(Double.MAX_VALUE);
		for(int testCount = 0; testCount < 1000; testCount++)
		{
			final MutableDouble mutated = highValue.mutate(100.0);
			Assert.assertTrue("mutation caused number to roll over: " + mutated, (mutated.doubleValue() != Double.POSITIVE_INFINITY) && (mutated.doubleValue() != Double.NEGATIVE_INFINITY));
		}
	}

	@Test
	public void testMin()
	{
		final MutableDouble lowValue = new MutableDouble(Double.MAX_VALUE * -1.0);
		for(int testCount = 0; testCount < 1000; testCount++)
		{
			final MutableDouble mutated = lowValue.mutate(100.0);
			Assert.assertTrue("mutation caused number to roll over: " + mutated, (mutated.doubleValue() != Double.POSITIVE_INFINITY) && (mutated.doubleValue() != Double.NEGATIVE_INFINITY));
		}
	}

	@Test
	public void testDeviation()
	{
		final MutableDouble center = new MutableDouble(0);
		double averageSum = 0;
		double testCount = 0.0;
		for(testCount = 0.0; testCount < 10000; testCount++)
		{
			averageSum += center.mutate(1.0).doubleValue();
		}
		final double average = averageSum / testCount;
		Assert.assertTrue("average deviation is more than 1.0", average < 1.0);
	}
}
