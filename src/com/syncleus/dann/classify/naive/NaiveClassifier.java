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
import com.syncleus.dann.classify.Classifier;

/**
 * A NaiveClassifier classifies items into categories by virtue of their features.
 * @param <I> The type of Item to use
 * @param <F> The type of feature to use
 * @param <C> The type of category to use
 * @author Jeffrey Phillips Freeman
 */
public interface NaiveClassifier<I, F, C> extends Classifier<I, C>
{
	/**
	 * Gets the category that a given feature is categorized in.
	 * @param feature The feature to use
	 * @return The category of the given feature
	 */
	C featureClassification(F feature);

	/**
	 * Gets the weighted classification of a given feature.
	 * @param feature The feature to use
	 * @return The weighted classification of the feature
	 * @see com.syncleus.dann.classify.naive.NaiveClassifier#featureClassification(Object)
	 */
	C featureClassificationWeighted(F feature);

	/**
	 * Gets the probability that a feature is in a given category.
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The probability that the feature is in the given category
	 */
	double featureClassificationProbability(F feature, C category);

	/**
	 * Gets the weighted probability that a feature is in a given category.
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The weighted probability that the feature is in the given category
	 */
	//DOC how does this differ from the method directly above it?
	double featureClassificationWeightedProbability(F feature, C category);

	//parent methods - currently commented out to avoid redundant javadoc. They're inherited.
	/*C classification(I item);
	Map<C, Double> getCategoryProbabilities(I item);
	double classificationProbability(I item, C category);
	Set<C> getCategories();*/
}
