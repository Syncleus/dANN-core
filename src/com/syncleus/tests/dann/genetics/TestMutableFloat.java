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

import com.syncleus.dann.genetics.MutableFloat;
import org.junit.*;

public class TestMutableFloat
{
	@Test
	public void testConstructors()
	{
		MutableFloat test = new MutableFloat(123f);
		Assert.assertTrue("value constructor failed", Math.abs(test.getNumber() - 123f) < (float) 0.000001);
		test = new MutableFloat("456");
		Assert.assertTrue("string value constructor failed", Math.abs(test.getNumber() - 456f) < (float) 0.000001);
		test = new MutableFloat(789f);
		Assert.assertTrue("Number value constructor failed", Math.abs(test.getNumber() - 789f) < (float) 0.000001);
	}

	@Test
	public void testMax()
	{
		final MutableFloat highValue = new MutableFloat(Float.MAX_VALUE);
		for(int testCount = 0; testCount < 1000; testCount++)
		{
			final MutableFloat mutated = highValue.mutate(100.0);
			Assert.assertTrue("mutation caused number to roll over: " + mutated, (mutated.floatValue() != Float.POSITIVE_INFINITY) && (mutated.floatValue() != Float.NEGATIVE_INFINITY));
		}
	}

	@Test
	public void testMin()
	{
		final MutableFloat lowValue = new MutableFloat(Float.MAX_VALUE * -1f);
		for(int testCount = 0; testCount < 1000; testCount++)
		{
			final MutableFloat mutated = lowValue.mutate(100.0);
			Assert.assertTrue("mutation caused number to roll over: " + mutated, (mutated.floatValue() != Float.POSITIVE_INFINITY) && (mutated.floatValue() != Float.NEGATIVE_INFINITY));
		}
	}

	@Test
	public void testDeviation()
	{
		final MutableFloat center = new MutableFloat(0);
		double averageSum = 0;
		double testCount;
		for(testCount = 0.0; testCount < 10000; testCount++)
		{
			averageSum += center.mutate(1.0).floatValue();
		}
		final double average = averageSum / testCount;
		Assert.assertTrue("average deviation is more than 1.0", average < 1.0);
	}
}
