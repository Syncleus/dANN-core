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
package com.syncleus.dann.classify.naivebayes.fisher;

import com.syncleus.dann.classify.FeatureExtractor;
import com.syncleus.dann.classify.naivebayes.SimpleNaiveBayesClassifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class SimpleFisherClassifier<I,F,C> extends SimpleNaiveBayesClassifier<I,F,C> implements FisherClassifier<I,F,C>
{
	private Map<C,Double> categoryMinimums = new HashMap<C,Double>();

	public SimpleFisherClassifier(FeatureExtractor<F,I> extractor)
	{
		super(extractor);
	}

	public void setMinimum(C category, double minimum)
	{
		this.categoryMinimums.put(category, minimum);
	}

	public double getMinimum(C category)
	{
		if(this.categoryMinimums.containsKey(category))
			return this.categoryMinimums.get(category);
		return 0.0;
	}

	@Override
	public C getClassification(I item, boolean useThreshold)
	{
		Map<C,Double> categoryProbabilities = new HashMap<C,Double>();

		C topCategory = null;
		double topProbability = 0.0;
		for(C category : this.getCategories())
		{
			double currentProbability = this.getCategoryProbability(item, category);
			categoryProbabilities.put(category, currentProbability);
			if(topProbability < currentProbability)
			{
				topCategory = category;
				topProbability = currentProbability;
			}
		}

		if(useThreshold)
		{
			for(Entry<C,Double> probability : categoryProbabilities.entrySet())
				if( probability.getValue()  > this.getMinimum(probability.getKey()) )
					return probability.getKey();
			return null;
		}
		else
			return topCategory;
	}

	@Override
	public double classificationProbability(F feature, C category)
	{
		double probability = super.classificationProbability(feature, category);
		if( probability == 0.0 )
			return 0.0;

		double probabilitySum = 0.0;
		for(C currentCategory : this.getCategories())
			probabilitySum += super.classificationProbability(feature, currentCategory);

		return probability / probabilitySum;
	}

	@Override
	public double getCategoryProbability(I item, C category)
	{
		Set<F> features = this.getExtractor().getFeatures(item);
		double probability = 1.0;
		for(F feature : features)
			probability *= this.classificationWeightedProbability(feature, category);
		probability = (-2.0 * Math.log(probability)) / 2.0;

		double term = Math.exp(-probability);
		double sum = term;
		for(int i = 1; i < (features.size()*2) / 2;i++)
		{
			term *= probability / i;
			sum += term;
		}

		return Math.min(sum, 1.0);
	}
}
