/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.classify.naive.bayes;

import com.syncleus.dann.classify.naive.NaiveClassifier;
import java.util.Map;
import java.util.Set;

public interface NaiveBayesClassifier<I,F,C> extends NaiveClassifier<I,F,C>
{
	C classification(I item, boolean useThreshold);
	double getCategoryThreshold(C category);
	void setCategoryThreshold(C category, double threshold);

	//NaiveClassifier methods
	C featureClassification(F feature);
	C featureClassificationWeighted(F feature);
	double featureClassificationProbability(F feature, C category);
	double featureClassificationWeightedProbability(F feature, C category);
	Map<C,Double> getCategoryProbabilities(I item);
	double classificationProbability(I item, C category);
	C classification(I item);
	Set<C> getCategories();
}
