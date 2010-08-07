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
package com.syncleus.dann.classify.naive.bayes.fisher;

import java.util.*;
import java.util.Map.Entry;
import com.syncleus.dann.classify.naive.FeatureExtractor;
import com.syncleus.dann.classify.naive.bayes.SimpleNaiveBayesClassifier;

/**
 * A SimpleFisherClassifier is a basic implementation of a FisherClassifier.
 * @param <I> The type of item to classify
 * @param <F> The type of factors to classify them by
 * @param <C> The type of class to classify them into
 */
public class SimpleFisherClassifier<I, F, C> extends SimpleNaiveBayesClassifier<I, F, C> implements FisherClassifier<I, F, C>
{
	private final Map<C, Double> categoryMinimums = new HashMap<C, Double>();

	/**
	 * Creates a new SimpleFisherClassifier with the given FeatureExtractor.
	 * @param extractor The FeatureExtractor to use
	 */
	public SimpleFisherClassifier(final FeatureExtractor<F, I> extractor)
	{
		super(extractor);
	}

	/**
	 * Sets the minimum value for a given category.
	 * @param category The category
	 * @param minimum The minimum value.
	 */
	@Override
	public void setMinimum(final C category, final double minimum)
	{
		this.categoryMinimums.put(category, minimum);
	}

	/**
	 * Gets the minimum value for the given category.
	 * @param category The category
	 * @return The minimum value
	 */
	@Override
	public double getMinimum(final C category)
	{
		if( this.categoryMinimums.containsKey(category) )
			return this.categoryMinimums.get(category);
		return 0.0;
	}

	/**
	 * Gets the most likely classification for the given item. May return null
	 * if <code>useThreshold = true</code> and no category is above the required threshold.
	 *
	 * @param item The item to classify
	 * @param useThreshold Whether to use the threshold
	 * @return The most likely classification
	 */
	@Override
	public C classification(final I item, final boolean useThreshold)
	{
		final Map<C, Double> categoryProbabilities = new HashMap<C, Double>();

		C topCategory = null;
		double topProbability = 0.0;
		for(final C category : this.getCategories())
		{
			final double currentProbability = this.classificationProbability(item, category);
			categoryProbabilities.put(category, currentProbability);
			if( topProbability < currentProbability )
			{
				topCategory = category;
				topProbability = currentProbability;
			}
		}

		if( useThreshold )
		{
			for(final Entry<C, Double> probability : categoryProbabilities.entrySet())
				if( probability.getValue() > this.getMinimum(probability.getKey()) )
					return probability.getKey();
			return null;
		}
		else
			return topCategory;
	}

	@Override
	public double featureClassificationProbability(final F feature, final C category)
	{
		final double probability = super.featureClassificationProbability(feature, category);
		if( probability == 0.0 )
			return 0.0;

		double probabilitySum = 0.0;
		for(final C currentCategory : this.getCategories())
			probabilitySum += super.featureClassificationProbability(feature, currentCategory);

		return probability / probabilitySum;
	}

	@Override
	public double classificationProbability(final I item, final C category)
	{
		final Set<F> features = this.getExtractor().getFeatures(item);
		double probability = 1.0;
		for(final F feature : features)
			probability *= this.featureClassificationWeightedProbability(feature, category);
		probability = -Math.log(probability); //originally (-2.0 * Math.log(probability)) / 2.0;

		double term = Math.exp(-probability);
		double sum = term;
		for(int i = 1; i < (features.size() * 2) / 2; i++)
		{
			term *= probability / i;
			sum += term;
		}

		return Math.min(sum, 1.0);
	}
}
