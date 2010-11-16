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
import com.syncleus.dann.dataprocessing.language.parsing.WordParser;
import com.syncleus.dann.dataprocessing.language.parsing.stem.StemmingWordParser;

/**
 * A StemmingLanguageNaiveBayesClassifier uses Bayes' theorem to classify word stems into categories.
 * @param <C> The categories to classify things into
 * @author Jeffrey Phillips Freeman
 */
public class StemmingLanguageNaiveBayesClassifier<C> extends SimpleNaiveBayesClassifier<String, String, C> implements TrainableLanguageNaiveBayesClassifier<C>
{
	private final Locale locale;

	/**
	 * Creates a StemmingLanguageNaiveBayesClassifier with the default locale.
	 */
	public StemmingLanguageNaiveBayesClassifier()
	{
		this(Locale.getDefault());
	}

	/**
	 * Creates a StemmingLanguageNaiveBayesClassifier with the given Locale.
	 * @param ourLocale The locale to use
	 */
	public StemmingLanguageNaiveBayesClassifier(final Locale ourLocale)
	{
		super(new StemmingWordExtractor());
		this.locale = ourLocale;
	}

	/**
	 * Gets the probability that a feature is in the given category.
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The probability that a feature is in the supplied category
	 */
	@Override
	public double featureClassificationProbability(final String feature, final C category)
	{
		return super.featureClassificationProbability(StemmingWordExtractor.PARSER.getUniqueWords(feature).iterator().next(), category);
	}

	/**
	 * Gets the weighted probability that a given feature is in the supplied category.
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The weighted probability that a given feature is in the supplied category
	 */
	@Override
	public double featureClassificationWeightedProbability(final String feature, final C category)
	{
		return super.featureClassificationWeightedProbability(StemmingWordExtractor.PARSER.getUniqueWords(feature).iterator().next(), category);
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
	 * A StemmingWordExtractor is a FeatureExtractor that extracts stems from words.
	 */
	private static class StemmingWordExtractor implements FeatureExtractor<String, String>
	{
		/**
		 * The WordParser used to separate words into stems.
		 */
		public static final WordParser PARSER = new StemmingWordParser();

		/**
		 * Gets the word stems from a given word.
		 * @param item The item to extract features from
		 * @return The set of features
		 */
		@Override
		public Set<String> getFeatures(final String item)
		{
			return PARSER.getUniqueWords(item);
		}
	}
}
