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

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class SimpleClassifier<I,F,C> implements TrainableClassifier<I,F,C>
{
	private ClassificationProbability<C> overallCategoryProbability = new ClassificationProbability<C>();
	private Map<F, ClassificationProbability<C>> featureTree = new FeatureClassificationTree<F,C>();
	private FeatureExtractor<F,I> extractor;

	public SimpleClassifier(FeatureExtractor<F,I> extractor)
	{
		this.extractor = extractor;
	}

	protected FeatureExtractor<F,I> getExtractor()
	{
		return this.extractor;
	}

	public double classificationProbability(F feature, C category)
	{
		//return fcount(feature, category) / overallProb
		int overallProb = this.getOverallProbability(category);
		int featureProb = 0;
		if( this.featureTree.containsKey(feature) )
			featureProb = this.featureTree.get(feature).getCategoryProbability(category);

		if(overallProb == 0)
			return 0.0;
		else
			return ((double)featureProb) / ((double)overallProb);
	}

	public double classificationWeightedProbability(F feature, C category)
	{
		double unweightedProb = this.classificationProbability(feature, category);
		double total = 0.0;
		if( this.featureTree.containsKey(feature) )
			total = this.featureTree.get(feature).getProbabilitySum();
		
		return ((total * unweightedProb) + 0.5) / (1.0 + total);
	}

	public Set<C> getCategories()
	{
		return Collections.unmodifiableSet(this.overallCategoryProbability.getCategoryProbabilityMap().keySet());
	}

	public void train(I item, C category)
	{
		Set<F> features = this.extractor.getFeatures(item);
		for(F feature : features)
			this.featureTree.get(feature).incrementCategory(category);
		this.overallCategoryProbability.incrementCategory(category);
	}

	protected int getOverallProbability(C category)
	{
		return this.overallCategoryProbability.getCategoryProbability(category);
	}

	protected int getOverallProbabilitySum()
	{
		return this.overallCategoryProbability.getProbabilitySum();
	}
}
