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
package com.syncleus.tests.dann.math.counting;

import org.junit.*;
import java.util.*;
import com.syncleus.dann.math.counting.Counters;
import org.apache.log4j.Logger;

public class TestCounters
{
	private final static Logger LOGGER = Logger.getLogger(TestCounters.class);
	private final static String SUPER_SET = "1234";
	private final static int COMBINATION_COUNT = 15;

	@Test
	public void testStringCombinations()
	{
		LOGGER.info("Generating combinations for: " + SUPER_SET);
		final char[] lettersArray = SUPER_SET.toCharArray();
		final List<Character> letters = new ArrayList<Character>();
		for(final char letter : lettersArray)
			letters.add(Character.valueOf(letter));

		final Set<List<Character>> combinations = Counters.everyCombination(letters);
		for(final List<Character> combination : combinations)
		{
			final StringBuilder combinationString = new StringBuilder(combination.size());
			for(final Character combinationChar : combination)
				combinationString.append(combinationChar);
			LOGGER.info("Combination Generated: " + combinationString);
		}

		Assert.assertTrue("Wrong number of combinations: " + combinations.size(), combinations.size() == COMBINATION_COUNT);
	}
}
