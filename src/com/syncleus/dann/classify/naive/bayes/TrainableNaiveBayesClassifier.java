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

import com.syncleus.dann.classify.TrainableClassifier;
import java.util.Map;
import java.util.Set;

public interface TrainableNaiveBayesClassifier<I,F,C> extends NaiveBayesClassifier<I,F,C>, TrainableClassifier<I,C>
{
	//Trainable methods
	void train(I item, C category);

	//NaiveBayesClassifier methods
	C classification(I item, boolean useThreshold);
	C classification(I item);
	Map<C,Double> getCategoryProbabilities(I item);
	double classificationProbability(I item, C category);
	double getCategoryThreshold(C category);
	void setCategoryThreshold(C category, double threshold);

	//Classifier methods
	C featureClassification(F feature);
	C featureClassificationWeighted(F feature);
	double featureClassificationProbability(F feature, C category);
	double featureClassificationWeightedProbability(F feature, C category);
	Set<C> getCategories();
}
