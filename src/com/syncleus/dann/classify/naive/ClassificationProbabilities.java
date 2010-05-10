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
package com.syncleus.dann.classify.naive;

import java.util.*;

public class ClassificationProbabilities<C>
{
	private int probabilitySum;
	private final Map<C, Integer> categoryProbabilityMap = new HashMap<C, Integer>();

	public Map<C, Integer> getCategoryProbabilityMap()
	{
		return Collections.unmodifiableMap(this.categoryProbabilityMap);
	}

	public void incrementCategory(final C category)
	{
		this.incrementCategory(category, 1);
	}

	public void incrementCategory(final C category, final int value)
	{
		Integer currentProbability = this.categoryProbabilityMap.get(category);
		if( currentProbability == null )
			currentProbability = value;
		else
			currentProbability = currentProbability + value;
		this.categoryProbabilityMap.put(category, currentProbability);
		this.probabilitySum += value;
	}

	public int getProbabilitySum()
	{
		return this.probabilitySum;
	}

	public int getCategoryProbability(final C category)
	{
		final Integer probability = this.categoryProbabilityMap.get(category);
		if( probability == null )
			return 0;
		else
			return probability;
	}
}
