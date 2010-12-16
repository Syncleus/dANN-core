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

import java.util.HashMap;

/**
 * Classifies features into a tree of probability fields, based on category.
 * @param <F> The type of feature to classify
 * @param <C> The type of category to classify the feature into
 * @author Jeffrey Phillips Freeman
 */
public class FeatureClassificationTree<F, C> extends HashMap<F, ClassificationProbabilities<C>>
{
	private static final long serialVersionUID = 4301941319736756428L;

	/**
	 * Gets the probability field for a given feature.
	 * @param feature The feature to get the field for
	 * @return The probability field for the given feature. Will never be null.
	 */
	public ClassificationProbabilities<C> getFeature(final F feature)
	{
		ClassificationProbabilities<C> classification = super.get(feature);
		if( classification == null )
		{
			classification = new ClassificationProbabilities<C>();
			this.put(feature, classification);
		}
		return classification;
	}
}
