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

import java.util.*;

/**
 * A SimpleNaiveClassifier is a simple implementation of a TrainableNaiveClassifier.
 *
 * @param <I> The type of item to classify
 * @param <F> The type of features the item has
 * @param <C> The type of categories to use
 */
public class SimpleNaiveClassifier<I, F, C> implements TrainableNaiveClassifier<I, F, C>
{
	private final ClassificationProbabilities<C> overallCategoryProbability = new ClassificationProbabilities<C>();
	private final FeatureClassificationTree<F, C> featureTree = new FeatureClassificationTree<F, C>();
	private final FeatureExtractor<F, I> extractor;

	/**
	 * Creates a SimpleNaiveClassifier with the given FeatureExtractor.
	 * @param featureExtractor The FeatureExtractor to use.
	 */
	public SimpleNaiveClassifier(final FeatureExtractor<F, I> featureExtractor)
	{
		this.extractor = featureExtractor;
	}

	/**
	 * Gets the FeatureExtractor with the given instance.
	 * @return The FeatureExtractor currently used.
	 */
	protected FeatureExtractor<F, I> getExtractor()
	{
		return this.extractor;
	}

	/**
	 * Gets the most likely classification of the given item.
	 *
	 * @param item The item to classify
	 * @return The most likely classification
	 */
	@Override
	public C classification(final I item)
	{
		final Set<F> features = this.extractor.getFeatures(item);
		final Map<C, Double> categoryProbabilities = new HashMap<C, Double>();
		C topCategory = null;
		double topProbability = 0.0;
		for(final F feature : features)
		{
			final C currentCategory = this.featureClassification(feature);
			Double newProbability = categoryProbabilities.get(currentCategory);
			if( newProbability == null )
				newProbability = 1.0;
			else
				newProbability++;
			categoryProbabilities.put(currentCategory, newProbability);
			if( newProbability >= topProbability )
			{
				topProbability = newProbability;
				topCategory = currentCategory;
			}
		}
		return topCategory;
	}

	/**
	 * Gets the probabilities for all categories of the given item.
	 *
	 * @param item The item to get probabilities for
	 * @return The field of categories for the item
	 */
	@Override
	public Map<C, Double> getCategoryProbabilities(final I item)
	{
		final Set<F> features = this.extractor.getFeatures(item);
		final Map<C, Double> categoryProbabilities = new HashMap<C, Double>();
		for(final F feature : features)
		{
			final C currentCategory = this.featureClassification(feature);
			Double newProbability = categoryProbabilities.get(currentCategory);
			if( newProbability == null )
				newProbability = 1.0;
			else
				newProbability++;
			categoryProbabilities.put(currentCategory, newProbability);
		}
		return categoryProbabilities;
	}

	/**
	 * Gets the probability that an item is in the given category.
	 *
	 * @param item The item to categorize
	 * @param category The category to check
	 * @return The probability that the item is in the category
	 */
	@Override
	public double classificationProbability(final I item, final C category)
	{
		return this.getCategoryProbabilities(item).get(category);
	}

	/**
	 * Gets the most likely category of a given feature.
	 * @param feature The feature to use
	 * @return The category most associated with a given feature
	 */
	@Override
	public C featureClassification(final F feature)
	{
		C topCategory = null;
		double topProbability = 0.0;
		for(final C category : this.getCategories())
		{
			final double currentProbability = this.featureClassificationProbability(feature, category);
			if( topProbability < currentProbability )
			{
				topCategory = category;
				topProbability = currentProbability;
			}
		}
		return topCategory;
	}

	/**
	 * Gets the most likely category for a given feature using a weighted classification.
	 * @param feature The feature to use
	 * @return The most likely classification for the feature
	 */
	@Override
	public C featureClassificationWeighted(final F feature)
	{
		C topCategory = null;
		double topProbability = 0.0;
		for(final C category : this.getCategories())
		{
			final double currentProbability = this.featureClassificationWeightedProbability(feature, category);
			if( topProbability < currentProbability )
			{
				topCategory = category;
				topProbability = currentProbability;
			}
		}
		return topCategory;
	}

	/**
	 * Gets the probability that a given feature is in the given category.
	 *
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The probability that the feature is in the category.
	 */
	@Override
	public double featureClassificationProbability(final F feature, final C category)
	{
		final int overallProb = this.getOverallProbability(category);
		int featureProb = 0;
		if( this.featureTree.containsKey(feature) )
			featureProb = this.featureTree.getFeature(feature).getCategoryProbability(category);

		if( overallProb == 0 )
			return 0.0;
		else
			return ((double) featureProb) / ((double) overallProb);
	}

	/**
	 * Gets the weighted probability that a feature is in the given category.
	 *
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The weighted probability that the feature is in the category
	 */
	@Override
	public double featureClassificationWeightedProbability(final F feature, final C category)
	{
		final double unweightedProb = this.featureClassificationProbability(feature, category);
		double total = 0.0;
		if( this.featureTree.containsKey(feature) )
			total = this.featureTree.getFeature(feature).getProbabilitySum();

		final double additionalProof = 0.5; //UNKNOWN USE
		return ((total * unweightedProb) + additionalProof) / (1.0 + total);
	}

	/**
	 * Gets an unmodifiable set of all given categories.
	 * @return All possible categories.
	 */
	@Override
	public Set<C> getCategories()
	{
		return Collections.unmodifiableSet(this.overallCategoryProbability.getCategoryProbabilityMap().keySet());
	}

	/**
	 * Increases the association of the given item with the given category.
	 *
	 * @param item The item
	 * @param category The category to associate with the item
	 */
	@Override
	public void train(final I item, final C category)
	{
		final Set<F> features = this.extractor.getFeatures(item);
		for(final F feature : features)
			this.featureTree.getFeature(feature).incrementCategory(category);
		this.overallCategoryProbability.incrementCategory(category);
	}

	/**
	 * Gets the overall probability of the given category.
	 * @param category The category to get the probability for
	 * @return The probability of the given category.
	 */
	protected int getOverallProbability(final C category)
	{
		return this.overallCategoryProbability.getCategoryProbability(category);
	}

	/**
	 * Gets the sum of all probabilities of all categories. Used in constructing
	 * a weighted average.
	 * @return The sum of all probabilities of all items.
	 */
	protected int getOverallProbabilitySum()
	{
		return this.overallCategoryProbability.getProbabilitySum();
	}
}
