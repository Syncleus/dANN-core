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
package com.syncleus.dann.math.counting;

import org.junit.*;

public class TestLexicographicPermutationCounter
{
	@Test
	public void testPermutationCount()
	{
		Counter generator = new LexicographicPermutationCounter(3, 2);
		Assert.assertTrue("Generator produced incorrect number of permutations, expected 6, received: " + generator.getTotal(), generator.getTotal().intValue() == 6);
		int count = 0;
		while( generator.hasMore() )
		{
			final int[] next = generator.getNext();
			if( next != null )
				count++;
		}
		Assert.assertTrue("Generator produced incorrect number of permutations, expected 6, received: " + count, count == 6);

		generator = new LexicographicPermutationCounter(3, 3);
		Assert.assertTrue("Generator produced incorrect number of permutations, expected 6, received: " + generator.getTotal(), generator.getTotal().intValue() == 6);
		count = 0;
		while( generator.hasMore() )
		{
			final int[] next = generator.getNext();
			if( next != null )
				count++;
		}
		Assert.assertTrue("Generator produced incorrect number of permutations, expected 6, received: " + count, count == 6);

		generator = new LexicographicPermutationCounter(3, 0);
		Assert.assertTrue("Generator produced incorrect number of permutations, expected 0, received: " + generator.getTotal(), generator.getTotal().intValue() == 0);
		count = 0;
		while( generator.hasMore() )
		{
			final int[] next = generator.getNext();
			if( next != null )
				count++;
		}
		Assert.assertTrue("Generator produced incorrect number of permutations, expected 0, received: " + count, count == 0);

		generator = new LexicographicPermutationCounter(0, 0);
		Assert.assertTrue("Generator produced incorrect number of permutations, expected 0, received: " + generator.getTotal(), generator.getTotal().intValue() == 0);
		count = 0;
		while( generator.hasMore() )
		{
			final int[] next = generator.getNext();
			if( next != null )
				count++;
		}
		Assert.assertTrue("Generator produced incorrect number of permutations, expected 0, received: " + count, count == 0);
	}
}
