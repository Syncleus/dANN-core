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

import com.syncleus.dann.classify.SimpleClassifier;
import com.syncleus.dann.classify.FeatureExtractor;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleNaiveBayesClassifier<I,F,C> extends SimpleClassifier<I,F,C> implements TrainableNaiveBayesClassifier<I,F,C>
{
	private final Map<C,Double> categoryThresholds = new HashMap<C,Double>();

	public SimpleNaiveBayesClassifier(FeatureExtractor<F,I> extractor)
	{
		super(extractor);
	}

	public double getCategoryThreshold(C category)
	{
		Double threshold = this.categoryThresholds.get(category);
		if( threshold == null )
			return 0.0;
		return threshold;
	}

	public void setCategoryThreshold(C category, double threshold)
	{
		this.categoryThresholds.put(category, threshold);
	}

	public C classification(I item, boolean useThreshold)
	{
		Map<C,Double> categoryProbabilities = new HashMap<C,Double>();

		C topCategory = null;
		double topProbability = 0.0;
		for(C category : this.getCategories())
		{
			double currentProbability = this.classificationProbability(item, category);
			categoryProbabilities.put(category, currentProbability);
			if(topProbability < currentProbability)
			{
				topCategory = category;
				topProbability = currentProbability;
			}
		}

		if(useThreshold)
			for(Entry<C,Double> probability : categoryProbabilities.entrySet())
				if( (probability.getKey() != topCategory) && (probability.getValue() * this.categoryThresholds.get(topCategory) > topProbability) )
					return null;

		return topCategory;
	}

	public final C classification(I item)
	{
		return this.classification(item, false);
	}

	public Map<C,Double> getCategoryProbabilities(I item)
	{
		Map<C,Double> categoryProbabilities = new HashMap<C,Double>();
		for(C category : this.getCategories())
			categoryProbabilities.put(category, this.classificationProbability(item, category));
		return Collections.unmodifiableMap(categoryProbabilities);
	}

	public double classificationProbability(I item, C category)
	{
		double probability = ((double)this.getOverallProbability(category)) / ((double)this.getOverallProbabilitySum());
		Set<F> features = this.getExtractor().getFeatures(item);
		for(F feature : features)
			probability *= this.featureClassificationWeightedProbability(feature, category);
		return probability;
	}
}
