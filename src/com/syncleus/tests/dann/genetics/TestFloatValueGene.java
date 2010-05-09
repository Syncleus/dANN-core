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

import com.syncleus.dann.genetics.*;
import org.junit.Assert;
import org.junit.Test;

public class TestFloatValueGene
{
	@Test
	public void testConstructors()
	{
		ValueGene test = new FloatValueGene(939810231.0f);
		Assert.assertTrue("value constructor failed", Math.abs(test.getValue().getNumber().floatValue() - 939810231.0f) < 1000);

		test = new FloatValueGene(new MutableFloat(202320342.0f));
		Assert.assertTrue("MutableFloat value constructor failed", Math.abs(test.getValue().getNumber().floatValue() - 202320342.0f) < 1000);

		test = new FloatValueGene(Float.valueOf(826493937.0f));
		Assert.assertTrue("Number value constructor failed", Math.abs(test.getValue().getNumber().floatValue() - 826493937.0f) < 1000);

		test = new FloatValueGene();
		Assert.assertTrue("default constructor failed", test.getValue().getNumber().floatValue() == 0.0);
	}

	@Test
	public void testMutation()
	{
		final ValueGene center = new FloatValueGene(0.0f);

		float averageSum = 0.0f;
		float testCount;
		for(testCount = 0.0f; testCount < 1000; testCount++)
		{
			averageSum += center.mutate(1.0).getValue().floatValue();
		}

		final float average = averageSum / testCount;

		Assert.assertTrue("average deviation is more than 1.0", average < 1.0f);
	}
}
