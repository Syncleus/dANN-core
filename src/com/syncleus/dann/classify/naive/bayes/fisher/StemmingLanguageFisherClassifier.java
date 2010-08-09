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
import com.syncleus.dann.dataprocessing.language.WordParser;
import com.syncleus.dann.dataprocessing.language.stem.StemmingWordParser;

/**
 * A LanguageFisherClassifier that classifies according to word stems.
 * @param <C> The type of classification
 * @author Jeffrey Phillips Freeman
 */
public class StemmingLanguageFisherClassifier<C> extends SimpleFisherClassifier<String, String, C> implements TrainableLanguageFisherClassifier<C>
{
	private final Locale locale;

	/**
	 * Creates a new StemmingLanguageFisherClassifier with the default Locale.
	 */
	public StemmingLanguageFisherClassifier()
	{
		this(Locale.getDefault());
	}

	/**
	 * Creates a new StemmingLanguageFisherClassifier with the given Locale.
	 * @param ourLocale The locale to use
	 */
	public StemmingLanguageFisherClassifier(final Locale ourLocale)
	{
		super(new StemmingWordExtractor());
		this.locale = ourLocale;
	}

	/**
	 * Gets the probability that a feature is in the given category.
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The probability the feature is in the category.
	 */
	@Override
	public double featureClassificationProbability(final String feature, final C category)
	{
		return super.featureClassificationProbability(StemmingWordExtractor.PARSER.getUniqueWords(feature).iterator().next(), category);
	}

	/**
	 * Gets the weighted probability that the feature is in the given category.
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The weighted probability that the feature is in the given category
	 * @see SimpleLanguageFisherClassifier#featureClassificationProbability(String, Object)
	 */
	@Override
	public double featureClassificationWeightedProbability(final String feature, final C category)
	{
		return super.featureClassificationWeightedProbability(StemmingWordExtractor.PARSER.getUniqueWords(feature).iterator().next(), category);
	}

	/**
	 * Gets the current Locale in use.
	 * @return The current locale
	 */
	@Override
	public Locale getLocale()
	{
		return this.locale;
	}

	/**
	 * The StemmingWordExtractor, which extract word stems from words.
	 */
	private static class StemmingWordExtractor implements FeatureExtractor<String, String>
	{
		/**
		 * The WordParser used to separate word stems from words.
		 */
		public static final WordParser PARSER = new StemmingWordParser();

		/**
		 * Gets the unique word stems from the supplied string.
		 * @param item The item
		 * @return All features of the given item
		 */
		@Override
		public Set<String> getFeatures(final String item)
		{
			return PARSER.getUniqueWords(item);
		}
	}
}
