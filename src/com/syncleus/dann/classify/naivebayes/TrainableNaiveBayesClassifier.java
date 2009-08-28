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
package com.syncleus.dann.classify.naivebayes;

import com.syncleus.dann.classify.Trainable;
import java.util.Map;
import java.util.Set;

public interface TrainableNaiveBayesClassifier<I,F,C> extends NaiveBayesClassifier<I,F,C>, Trainable<I,C>
{
	//Trainable methods
	void train(I item, C category);

	//NaiveBayesClassifier methods
	C getClassification(I item, boolean useThreshold);
	C getClassification(I item);
	Map<C,Double> getCategoryProbabilities(I item);
	double getCategoryProbability(I item, C category);
	double getCategoryThreshold(C category);
	void setCategoryThreashold(C category, double threshold);

	//Classifier methods
	C classification(F feature);
	C classificationWeighted(F feature);
	double classificationProbability(F feature, C category);
	double classificationWeightedProbability(F feature, C category);
	Set<C> getCategories();
}
