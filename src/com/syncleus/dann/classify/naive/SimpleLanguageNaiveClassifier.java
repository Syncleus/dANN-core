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

public class SimpleLanguageNaiveClassifier<C> extends SimpleNaiveClassifier<String, String, C> implements TrainableLanguageNaiveClassifier<C>
{
	private static class WordExtractor implements FeatureExtractor<String, String>
	{
		private static final WordParser PARSER = new BasicWordParser();

		public Set<String> getFeatures(final String item)
		{
			return PARSER.getUniqueWords(item);
		}
	}

	public SimpleLanguageNaiveClassifier()
	{
		super(new WordExtractor());
	}

	@Override
	public double featureClassificationProbability(final String feature, final C category)
	{
		return super.featureClassificationProbability(feature.toLowerCase(Locale.ENGLISH), category);
	}

	@Override
	public double featureClassificationWeightedProbability(final String feature, final C category)
	{
		return super.featureClassificationWeightedProbability(feature.toLowerCase(Locale.ENGLISH), category);
	}
}
