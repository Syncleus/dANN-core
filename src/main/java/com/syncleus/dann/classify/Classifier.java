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
package com.syncleus.dann.classify;

import java.util.*;

/**
 * Groups a set of items, I, into members of group C.
 * @param <I> The type of item to classify
 * @param <C> The type of group to classify them into
 * @author Jeffrey Phillips Freeman
 */
public interface Classifier<I, C>
{
	/**
	 * Gets the classification of a given item.
	 * @param item The item to classify
	 * @return The classification of the item.
	 */
	C classification(I item);

	/**
	 * Gets the possible categories for a given item, and the probability that it will
	 * be assigned to a given one.
	 * @param item The item to get probabilities for
	 * @return A map of category to probability for the item's classification
	 */
	Map<C, Double> getCategoryProbabilities(I item);

	/**
	 * Gets the probability that the item will be assigned a given category.
	 * @param item The item to categorize
	 * @param category The category to check
	 * @return The probability that <code>item</code> is in <code>category</code>.
	 * @see Classifier#getCategoryProbabilities(Object)
	 */
	double classificationProbability(I item, C category);

	/**
	 * Gets all possible categories.
	 * @return The set of all possible categories
	 */
	Set<C> getCategories();
}
