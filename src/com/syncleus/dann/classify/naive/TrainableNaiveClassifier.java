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

import com.syncleus.dann.classify.TrainableClassifier;

/**
 * A TrainableNaiveClassifier is a NaiveClassifier that's also a TrainableClassifier.
 * @param <I> The item to classify
 * @param <F> The features of the item
 * @param <C> The categories of the item
 */
public interface TrainableNaiveClassifier<I, F, C> extends NaiveClassifier<I, F, C>, TrainableClassifier<I, C>
{
}
