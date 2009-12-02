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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SimpleNaiveClassifier<I,F,C> implements TrainableNaiveClassifier<I,F,C>
{
	private ClassificationProbabilities<C> overallCategoryProbability = new ClassificationProbabilities<C>();
	private FeatureClassificationTree<F,C> featureTree = new FeatureClassificationTree<F,C>();
	private FeatureExtractor<F,I> extractor;

	public SimpleNaiveClassifier(FeatureExtractor<F,I> extractor)
	{
		this.extractor = extractor;
	}

	protected FeatureExtractor<F,I> getExtractor()
	{
		return this.extractor;
	}

	public C classification(I item)
	{
		Set<F> features = this.extractor.getFeatures(item);
		Map<C,Double> categoryProbabilities = new HashMap<C,Double>();
		C topCategory = null;
		double topProbability = 0.0;
		for(F feature : features)
		{
			C currentCategory = this.featureClassification(feature);
			Double newProbability = categoryProbabilities.get(currentCategory);
			if( newProbability == null )
				newProbability = Double.valueOf(1.0);
			else
				newProbability++;
			categoryProbabilities.put(currentCategory, newProbability);

			if(newProbability >= topProbability)
			{
				topProbability = newProbability;
				topCategory = currentCategory;
			}
		}

		return topCategory;
	}

	public Map<C,Double> getCategoryProbabilities(I item)
	{
		Set<F> features = this.extractor.getFeatures(item);
		Map<C,Double> categoryProbabilities = new HashMap<C,Double>();
		for(F feature : features)
		{
			C currentCategory = this.featureClassification(feature);
			Double newProbability = categoryProbabilities.get(currentCategory);
			if( newProbability == null )
				newProbability = Double.valueOf(1.0);
			else
				newProbability++;
			categoryProbabilities.put(currentCategory, newProbability);
		}

		return categoryProbabilities;
	}

	public double classificationProbability(I item, C category)
	{
		return this.getCategoryProbabilities(item).get(category).doubleValue();
	}

	public C featureClassification(F feature)
	{
		C topCategory = null;
		double topProbability = 0.0;
		for(C category : this.getCategories())
		{
			double currentProbability = this.featureClassificationProbability(feature, category);
			if( topProbability < currentProbability)
			{
				topCategory = category;
				topProbability = currentProbability;
			}
		}
		return topCategory;
	}

	public C featureClassificationWeighted(F feature)
	{
		C topCategory = null;
		double topProbability = 0.0;
		for(C category : this.getCategories())
		{
			double currentProbability = this.featureClassificationWeightedProbability(feature, category);
			if( topProbability < currentProbability)
			{
				topCategory = category;
				topProbability = currentProbability;
			}
		}
		return topCategory;
	}


	public double featureClassificationProbability(F feature, C category)
	{
		int overallProb = this.getOverallProbability(category);
		int featureProb = 0;
		if( this.featureTree.containsKey(feature) )
			featureProb = this.featureTree.getFeature(feature).getCategoryProbability(category);

		if(overallProb == 0)
			return 0.0;
		else
			return ((double)featureProb) / ((double)overallProb);
	}

	public double featureClassificationWeightedProbability(F feature, C category)
	{
		double unweightedProb = this.featureClassificationProbability(feature, category);
		double total = 0.0;
		if( this.featureTree.containsKey(feature) )
			total = this.featureTree.getFeature(feature).getProbabilitySum();
		
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
			this.featureTree.getFeature(feature).incrementCategory(category);
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
