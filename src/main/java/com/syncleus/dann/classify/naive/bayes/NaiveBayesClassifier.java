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
package com.syncleus.dann.classify.naive.bayes;

import com.syncleus.dann.classify.naive.NaiveClassifier;

/**
 * A NaiveBayesClassifier classifies items based on Bayes' Theorum.
 * @param <I> The item to classify
 * @param <F> The type of feature from the item
 * @param <C> The type of category
 * @author Jeffrey Phillips Freeman
 */
public interface NaiveBayesClassifier<I, F, C> extends NaiveClassifier<I, F, C>
{
	/**
	 * Gets the classification of an item, possibly using the threshold.
	 * If all possible categories are below the threshold, null is returned.
	 *
	 * @param item The item to classify
	 * @param useThreshold Whether to use the threshold
	 * @return The category of a given item
	 */
	C classification(I item, boolean useThreshold);

	/**
	 * Gets the category threshold for a given category.
	 * @param category The category to check
	 * @return The threshold for the given category
	 */
	double getCategoryThreshold(C category);

	/**
	 * Sets the threshold for a given category.
	 * @param category The category to set a threshold for
	 * @param threshold The threshold for the category
	 */
	void setCategoryThreshold(C category, double threshold);
}
