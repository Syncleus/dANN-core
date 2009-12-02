/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.tests.dann.genetics;

import com.syncleus.dann.genetics.MutableLong;
import org.junit.*;

public class TestMutableLong
{
	@Test
	public void testConstructors()
	{
		MutableLong test = new MutableLong(123);
		Assert.assertTrue("value constructor failed", test.getNumber().longValue() == 123);

		test = new MutableLong("456");
		Assert.assertTrue("string value constructor failed", test.getNumber().longValue() == 456);

		test = new MutableLong(Long.valueOf(789));
		Assert.assertTrue("Number value constructor failed", test.getNumber().longValue() == 789);
	}

	@Test
	public void testMax()
	{
		MutableLong highValue = new MutableLong(Long.MAX_VALUE);

		for(int testCount = 0; testCount < 1000; testCount++)
		{
			MutableLong mutated = highValue.mutate(100.0);

			Assert.assertTrue("mutation caused number to roll over: " + mutated, mutated.longValue() >= -1);
		}
	}

	@Test
	public void testMin()
	{
		MutableLong lowValue = new MutableLong(Long.MIN_VALUE);

		for(int testCount = 0; testCount < 1000; testCount++)
		{
			MutableLong mutated = lowValue.mutate(100.0);

			Assert.assertTrue("mutation caused number to roll over: " + mutated, mutated.longValue() <= 1);
		}
	}

	@Test
	public void testDeviation()
	{
		MutableLong center = new MutableLong(0);

		double averageSum = 0;
		double testCount = 0.0;
		for(testCount = 0.0; testCount < 10000; testCount++)
		{
			averageSum += center.mutate(1.0).longValue();
		}

		double average = averageSum / testCount;

		Assert.assertTrue("average deviation is more than 1.0", average < 1.0);
	}
}
