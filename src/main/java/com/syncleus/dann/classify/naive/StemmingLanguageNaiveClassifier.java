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
import com.syncleus.dann.dataprocessing.language.parsing.stem.StemmingWordParser;

/**
 * A StemmingLanguageNaiveClassifier separates words into items based on their word stems.
 * @param <C> The categories associated with an item
 * @author Jeffrey Phillips Freeman
 */
public class StemmingLanguageNaiveClassifier<C> extends SimpleNaiveClassifier<String, String, C> implements TrainableLanguageNaiveClassifier<C>
{
	private final Locale locale;

	/**
	 * Creates a StemmingLanguageNaiveClassifier based on the default locale.
	 */
	public StemmingLanguageNaiveClassifier()
	{
		this(Locale.getDefault());
	}

	/**
	 * Creates a StemmingLanguageNaiveClassifier with the given locale.
	 * @param ourLocale The locale to use
	 */
	public StemmingLanguageNaiveClassifier(final Locale ourLocale)
	{
		super(new StemmingWordExtractor());
		this.locale = ourLocale;
	}

	/**
	 * Gets the probability that the feature is in the given category.
	 *
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The probability that the feature is in the given category.
	 */
	@Override
	public double featureClassificationProbability(final String feature, final C category)
	{
		return super.featureClassificationProbability(StemmingWordExtractor.PARSER.getUniqueWords(feature).iterator().next(), category);
	}

	/**
	 * Gets the weighted probability that the feature is in the given category.
	 *
	 * @param feature The feature to check
	 * @param category The category to check
	 * @return The weighted probability that the feature is in the given category.
	 */
	@Override
	public double featureClassificationWeightedProbability(final String feature, final C category)
	{
		return super.featureClassificationWeightedProbability(StemmingWordExtractor.PARSER.getUniqueWords(feature).iterator().next(), category);
	}

	/**
	 * Gets the current locale.
	 * @return The locale this object is using.
	 */
	@Override
	public Locale getLocale()
	{
		return this.locale;
	}

	/**
	 * A StemmingWordExtractor extracts Features by words by extracting their stems.
	 */
	private static class StemmingWordExtractor implements FeatureExtractor<String, String>
	{
		/**
		 * The StemmingWordParser used to extract stems from words.
		 */
		public static final StemmingWordParser PARSER = new StemmingWordParser();

		/**
		 * Gets all stems from the given item.
		 * @param item The item
		 * @return A set of features
		 */
		@Override
		public Set<String> getFeatures(final String item)
		{
			return PARSER.getUniqueWords(item);
		}
	}
}
