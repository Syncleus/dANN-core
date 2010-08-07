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
import com.syncleus.dann.classify.naive.FeatureExtractor;
import com.syncleus.dann.dataprocessing.language.*;

/**
 * A SimpleLanguageFisherClassifier classifies words as a SimpleFisherClassifier.
 * @param <C> The classification to use
 */
public class SimpleLanguageFisherClassifier<C> extends SimpleFisherClassifier<String, String, C> implements TrainableLanguageFisherClassifier<C>
{
	private final Locale locale;

	/**
	 * Creates a new SimpleLanguageFisherClassifier using the default locale.
	 */
	public SimpleLanguageFisherClassifier()
	{
		this(Locale.getDefault());
	}

	/**
	 * Creates a new SimpleLanguageFisherClassifier using the provided locale.
	 * @param ourLocale The locale to use
	 */
	public SimpleLanguageFisherClassifier(final Locale ourLocale)
	{
		super(new WordExtractor());
		this.locale = ourLocale;
	}

	/**
	 * Gets the current locale in use.
	 * @return The locale in use
	 */
	@Override
	public Locale getLocale()
	{
		return this.locale;
	}

	/**
	 * Gets the probability that the given feature is in the given category.
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The probability that a feature is in the given category
	 */
	@Override
	public double featureClassificationProbability(final String feature, final C category)
	{
		return super.featureClassificationProbability(feature.toLowerCase(this.locale), category);
	}

	/**
	 * Gets the weighted probability that the given feature is in the given category.
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The weighted probability that a feature is in the given category
	 * @see com.syncleus.dann.classify.naive.bayes.fisher.SimpleLanguageFisherClassifier#featureClassificationProbability(String, Object)
	 */
	@Override
	public double featureClassificationWeightedProbability(final String feature, final C category)
	{
		return super.featureClassificationWeightedProbability(feature.toLowerCase(this.locale), category);
	}

	/**
	 * A WordExtractor to extract words from a String.
	 */
	private static class WordExtractor implements FeatureExtractor<String, String>
	{
		/**
		 * The parser to use to get unique words.
		 */
		private static final WordParser PARSER = new BasicWordParser();

		/**
		 * Gets the unique words in a String.
		 * @param item The item
		 * @return The words from a String
		 */
		public Set<String> getFeatures(final String item)
		{
			return PARSER.getUniqueWords(item);
		}
	}
}
