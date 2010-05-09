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

import java.util.*;
import java.util.Map.Entry;
import com.syncleus.dann.classify.naive.*;

public class SimpleNaiveBayesClassifier<I, F, C> extends SimpleNaiveClassifier<I, F, C> implements TrainableNaiveBayesClassifier<I, F, C>
{
	private final Map<C, Double> categoryThresholds = new HashMap<C, Double>();

	public SimpleNaiveBayesClassifier(final FeatureExtractor<F, I> extractor)
	{
		super(extractor);
	}

	public double getCategoryThreshold(final C category)
	{
		final Double threshold = this.categoryThresholds.get(category);
		if (threshold == null)
			return 0.0;
		return threshold;
	}

	public void setCategoryThreshold(final C category, final double threshold)
	{
		this.categoryThresholds.put(category, threshold);
	}

	public C classification(final I item, final boolean useThreshold)
	{
		final Map<C, Double> categoryProbabilities = new HashMap<C, Double>();
		C topCategory = null;
		double topProbability = 0.0;
		for(final C category : this.getCategories())
		{
			final double currentProbability = this.classificationProbability(item, category);
			categoryProbabilities.put(category, currentProbability);
			if (topProbability < currentProbability)
			{
				topCategory = category;
				topProbability = currentProbability;
			}
		}
		if (useThreshold)
			for(final Entry<C, Double> probability : categoryProbabilities.entrySet())
				if ((probability.getKey() != topCategory) && (probability.getValue() * this.categoryThresholds.get(topCategory) > topProbability))
					return null;
		return topCategory;
	}

	@Override
	public final C classification(final I item)
	{
		return this.classification(item, false);
	}

	@Override
	public Map<C, Double> getCategoryProbabilities(final I item)
	{
		final Map<C, Double> categoryProbabilities = new HashMap<C, Double>();
		for(final C category : this.getCategories())
			categoryProbabilities.put(category, this.classificationProbability(item, category));
		return Collections.unmodifiableMap(categoryProbabilities);
	}

	@Override
	public double classificationProbability(final I item, final C category)
	{
		double probability = ((double) this.getOverallProbability(category)) / ((double) this.getOverallProbabilitySum());
		final Set<F> features = this.getExtractor().getFeatures(item);
		for(final F feature : features)
			probability *= this.featureClassificationWeightedProbability(feature, category);
		return probability;
	}
}
