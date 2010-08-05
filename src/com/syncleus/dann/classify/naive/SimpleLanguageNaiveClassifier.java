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
import com.syncleus.dann.dataprocessing.language.*;

/**
 * This is an implementation of a SimpleNaiveClassifier for language.
 * @param <C> The categories to use
 */
public class SimpleLanguageNaiveClassifier<C> extends SimpleNaiveClassifier<String, String, C> implements TrainableLanguageNaiveClassifier<C>
{
	private final Locale locale;

	/**
	 * Creates a SimpleLanguageNaiveClassifier using the default locale.
	 */
	public SimpleLanguageNaiveClassifier()
	{
		this(Locale.getDefault());
	}

	/**
	 * Creates a SimpleLanguageNaiveClassifier using the supplied locale.
	 * @param ourLocale The locale to use
	 */
	public SimpleLanguageNaiveClassifier(final Locale ourLocale)
	{
		super(new WordExtractor());
		this.locale = ourLocale;
	}

	@Override
	public double featureClassificationProbability(final String feature, final C category)
	{
		return super.featureClassificationProbability(feature.toLowerCase(this.locale), category);
	}

	@Override
	public double featureClassificationWeightedProbability(final String feature, final C category)
	{
		return super.featureClassificationWeightedProbability(feature.toLowerCase(this.locale), category);
	}

	/**
	 * Gets the current locale.
	 * @return
	 */
	public Locale getLocale()
	{
		return this.locale;
	}

	private static class WordExtractor implements FeatureExtractor<String, String>
	{
		private static final WordParser PARSER = new BasicWordParser();

		public Set<String> getFeatures(final String item)
		{
			return PARSER.getUniqueWords(item);
		}
	}
}
