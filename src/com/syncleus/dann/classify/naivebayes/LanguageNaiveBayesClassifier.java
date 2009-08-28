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

public interface LanguageNaiveBayesClassifier<C> extends NaiveBayesClassifier<String,String,C>
{
	//NaiveBayesClassifier methods
	C classification(String item, boolean useThreshold);
	C classification(String item);
	Map<C,Double> getCategoryProbabilities(String item);
	double classificationProbability(String item, C category);
	double getCategoryThreshold(C category);
	void setCategoryThreshold(C category, double threshold);

	//Classifier methods
	C featureClassification(String feature);
	C featureClassificationWeighted(String feature);
	double featureClassificationProbability(String feature, C category);
	double featureClassificationWeightedProbability(String feature, C category);
	Set<C> getCategories();
}
