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

public class TestByteValueGene
{
	@Test
	public void testConstructors()
	{
		ValueGene test = new ByteValueGene((byte)123);
		Assert.assertTrue("value constructor failed", test.getValue().getNumber().byteValue() == 123);

		test = new ByteValueGene(new MutableByte((byte)57));
		Assert.assertTrue("MutableByte value constructor failed", test.getValue().getNumber().byteValue() == 57);

		test = new ByteValueGene(Byte.valueOf((byte)83));
		Assert.assertTrue("Number value constructor failed", test.getValue().getNumber().byteValue() == 83);

		test = new ByteValueGene();
		Assert.assertTrue("default constructor failed", test.getValue().getNumber().byteValue() == 0);
	}

	@Test
	public void testMutation()
	{
		ValueGene center = new ByteValueGene((byte)0);

		double averageSum = 0;
		double testCount = 0.0;
		for(testCount = 0.0; testCount < 1000; testCount++)
		{
			averageSum += center.mutate(1.0).getValue().byteValue();
		}

		double average = averageSum / testCount;

		Assert.assertTrue("average deviation is more than 1.0", average < 1.0);
	}
}
