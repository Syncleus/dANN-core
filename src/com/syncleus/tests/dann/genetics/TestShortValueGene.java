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
import org.junit.*;

public class TestShortValueGene
{
	@Test
	public void testConstructors()
	{
		ValueGene test = new ShortValueGene((short) 123);
		Assert.assertTrue("value constructor failed", test.getValue().getNumber().shortValue() == (short) 123);
		test = new ShortValueGene(new MutableShort((short) 57));
		Assert.assertTrue("MutableByte value constructor failed", test.getValue().getNumber().shortValue() == (short) 57);
		test = new ShortValueGene((short) 83);
		Assert.assertTrue("Number value constructor failed", test.getValue().getNumber().shortValue() == (short) 83);
		test = new ShortValueGene();
		Assert.assertTrue("default constructor failed", test.getValue().getNumber().shortValue() == (short) 0);
	}

	@Test
	public void testMutation()
	{
		final ValueGene center = new ShortValueGene((short) 0);
		short averageSum = (short) 0;
		int testCount;
		for(testCount = 0; testCount < 1000; testCount++)
		{
			averageSum += center.mutate(10).getValue().shortValue();
		}
		final double average = ((double) averageSum) / ((double) testCount);
		Assert.assertTrue("average deviation is more than 10.0", average < 10.0);
	}
}
