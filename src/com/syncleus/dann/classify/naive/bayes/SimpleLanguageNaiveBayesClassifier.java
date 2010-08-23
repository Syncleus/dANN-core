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
import com.syncleus.dann.classify.naive.FeatureExtractor;
import com.syncleus.dann.dataprocessing.language.parsing.BasicWordParser;
import com.syncleus.dann.dataprocessing.language.parsing.WordParser;

/**
 * A SimpleLanguageNaiveBayesClassifier classifies Strings by breaking them into Words.
 * @param <C> The category to use.
 * @author Jeffrey Phillips Freeman
 */
public class SimpleLanguageNaiveBayesClassifier<C> extends SimpleNaiveBayesClassifier<String, String, C> implements TrainableLanguageNaiveBayesClassifier<C>
{
	private final Locale locale;

	/**
	 * Creates a SimpleLanguageNaiveBayesClassifier using the default Locale.
	 */
	public SimpleLanguageNaiveBayesClassifier()
	{
		this(Locale.getDefault());
	}

	/**
	 * Creates an object with the given locale.
	 * @param ourLocale The locale to use
	 */
	public SimpleLanguageNaiveBayesClassifier(final Locale ourLocale)
	{
		super(new WordExtractor());
		this.locale = ourLocale;
	}

	/**
	 * Gets the probability that the feature is in the given category.
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The probability that the feature is in the given category
	 */
	@Override
	public double featureClassificationProbability(final String feature, final C category)
	{
		return super.featureClassificationProbability(feature.toLowerCase(this.locale), category);
	}

	/**
	 * Gets the weighted probability that the feature is in the given category.
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The weighted probability that the feature is in the given category
	 */
	@Override
	public double featureClassificationWeightedProbability(final String feature, final C category)
	{
		return super.featureClassificationWeightedProbability(feature.toLowerCase(this.locale), category);
	}

	/**
	 * Gets the currently used locale.
	 * @return The current locale
	 */
	@Override
	public Locale getLocale()
	{
		return this.locale;
	}

	/**
	 * A WordExtractor extracts the unique words from a given string.
	 */
	private static class WordExtractor implements FeatureExtractor<String, String>
	{
		/**
		 * The WordParser used to extract words.
		 */
		private static final WordParser PARSER = new BasicWordParser();

		/**
		 * Gets the unique words that make up a given string.
		 * @param item The item
		 * @return The features from the given item
		 */
		@Override
		public Set<String> getFeatures(final String item)
		{
			return PARSER.getUniqueWords(item);
		}
	}
}
