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

/**
 * Represents a field of classification probabilities possible from a given category. This represents the relative
 * probability that an item will be classified into a given category.
 *
 * @param <C> The type of category
 * @author Jeffrey Phillips Freeman
 */
public class ClassificationProbabilities<C>
{
	private int probabilitySum;
	private final Map<C, Integer> categoryProbabilityMap = new HashMap<C, Integer>();

	/**
	 * Gets an unmodifiable version of the category's probability map.
	 * @return The category's probability map.
	 */
	public Map<C, Integer> getCategoryProbabilityMap()
	{
		return Collections.unmodifiableMap(this.categoryProbabilityMap);
	}

	/**
	 * Makes a certain category 1 more likely.
	 * @param category The category to change
	 * @see com.syncleus.dann.classify.naive.ClassificationProbabilities#incrementCategory(Object, int)
	 */
	public void incrementCategory(final C category)
	{
		this.incrementCategory(category, 1);
	}

	/**
	 * Makes a given category more likely by a given value.
	 * @param category The category to change
	 * @param value How much to change it by
	 */
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

	/**
	 * Gets the sum of the probability of all possibilities. This is used to normalize the relative probabilities
	 * in the map.
	 * @return The sum of the probabilities
	 */
	public int getProbabilitySum()
	{
		return this.probabilitySum;
	}

	/**
	 * Gets the probability of a given category.
	 * @param category The category to use
	 * @return The probability of that category
	 * @see ClassificationProbabilities#getProbabilitySum()
	 */
	public int getCategoryProbability(final C category)
	{
		final Integer probability = this.categoryProbabilityMap.get(category);
		if( probability == null )
			return 0;
		else
			return probability;
	}
}
