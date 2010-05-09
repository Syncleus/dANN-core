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
import com.syncleus.dann.dataprocessing.language.WordParser;
import com.syncleus.dann.dataprocessing.language.stem.StemmingWordParser;

public class StemmingLanguageNaiveBayesClassifier<C> extends SimpleNaiveBayesClassifier<String, String, C> implements TrainableLanguageNaiveBayesClassifier<C>
{
	private final Locale locale;

	public StemmingLanguageNaiveBayesClassifier()
	{
		this(Locale.getDefault());
	}

	public StemmingLanguageNaiveBayesClassifier(final Locale locale)
	{
		super(new StemmingWordExtractor());
		this.locale = locale;
	}

	@Override
	public double featureClassificationProbability(final String feature, final C category)
	{
		return super.featureClassificationProbability(StemmingWordExtractor.PARSER.getUniqueWords(feature).iterator().next(), category);
	}

	@Override
	public double featureClassificationWeightedProbability(final String feature, final C category)
	{
		return super.featureClassificationWeightedProbability(StemmingWordExtractor.PARSER.getUniqueWords(feature).iterator().next(), category);
	}

	public Locale getLocale()
	{
		return locale;
	}

	private static class StemmingWordExtractor implements FeatureExtractor<String, String>
	{
		public static final WordParser PARSER = new StemmingWordParser();

		public Set<String> getFeatures(final String item)
		{
			return PARSER.getUniqueWords(item);
		}
	}
}
