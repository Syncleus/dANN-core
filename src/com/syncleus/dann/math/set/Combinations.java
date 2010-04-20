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
package com.syncleus.dann.math.set;

import java.util.*;

public class Combinations
{
	public static <O> Set<List<O>> everyCombination(List<O> superSet)
	{
		final Set<List<O>> combinations = new HashSet<List<O>>();
		for(int currentSequenceLength = 1; currentSequenceLength < superSet.size();currentSequenceLength++)
		{
			CombinationGenerator generator = new CombinationGenerator(superSet.size(), currentSequenceLength);
			while(generator.hasMore())
			{
				List<O> combination = new ArrayList<O>();
				int[] combinationIndexes = generator.getNext();
				for(int combinationIndex : combinationIndexes)
					combination.add(superSet.get(combinationIndex));
				combinations.add(Collections.unmodifiableList(combination));
			}
		}
		return Collections.unmodifiableSet(combinations);
	}

	public static <O> Set<Set<O>> everyCombination(Set<O> superSet)
	{
		List<O> superSetList = new ArrayList<O>(superSet);

		final Set<Set<O>> combinations = new HashSet<Set<O>>();
		for(int currentSequenceLength = 1; currentSequenceLength < superSet.size();currentSequenceLength++)
		{
			CombinationGenerator generator = new CombinationGenerator(superSet.size(), currentSequenceLength);
			while(generator.hasMore())
			{
				Set<O> combination = new HashSet<O>();
				int[] combinationIndexes = generator.getNext();
				for(int combinationIndex : combinationIndexes)
					combination.add(superSetList.get(combinationIndex));
				combinations.add(Collections.unmodifiableSet(combination));
			}
		}
		return Collections.unmodifiableSet(combinations);
	}
}
