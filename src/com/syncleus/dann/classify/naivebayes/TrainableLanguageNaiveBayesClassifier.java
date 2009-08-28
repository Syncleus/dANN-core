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

import java.util.Map;
import java.util.Set;

public interface TrainableLanguageNaiveBayesClassifier<C> extends TrainableNaiveBayesClassifier<String,String,C>, LanguageNaiveBayesClassifier<C>
{
	//Trainable methods
	void train(String item, C category);
	
	//NaiveBayesClassifier methods
	C getClassification(String item, boolean useThreshold);
	C getClassification(String item);
	Map<C,Double> getCategoryProbabilities(String item);
	double getCategoryProbability(String item, C category);
	double getCategoryThreshold(C category);
	void setCategoryThreashold(C category, double threshold);

	//Classifier methods
	C classification(String feature);
	C classificationWeighted(String feature);
	double classificationProbability(String feature, C category);
	double classificationWeightedProbability(String feature, C category);
	Set<C> getCategories();
}
