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
package com.syncleus.dann.genetics;

import org.junit.*;

public class TestDoubleValueGene
{
	@Test
	public void testConstructors()
	{
		ValueGene test = new DoubleValueGene(93947810231.0);
		Assert.assertTrue("value constructor failed", Math.abs(test.getValue().getNumber().doubleValue() - 93947810231.0) < 1000);
		test = new DoubleValueGene(new MutableDouble(20237420342.0));
		Assert.assertTrue("MutableDouble value constructor failed", Math.abs(test.getValue().getNumber().doubleValue() - 20237420342.0) < 1000);
		test = new DoubleValueGene(82649173937.0);
		Assert.assertTrue("Number value constructor failed", Math.abs(test.getValue().getNumber().doubleValue() - 82649173937.0) < 1000);
		test = new DoubleValueGene();
		Assert.assertTrue("default constructor failed", test.getValue().getNumber().doubleValue() == 0.0);
	}

	@Test
	public void testMutation()
	{
		final ValueGene center = new DoubleValueGene(0.0);

		double averageSum = 0.0;
		double testCount;
		for(testCount = 0.0; testCount < 1000; testCount++)
		{
			averageSum += center.mutate(1.0).getValue().doubleValue();
		}

		final double average = averageSum / testCount;

		Assert.assertTrue("average deviation is more than 1.0", average < 1.0);
	}
}
