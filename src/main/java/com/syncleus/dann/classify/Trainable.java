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
package com.syncleus.dann.classify;

/**
 * If a class is Trainable, it means that it learns to put certain items in certain categories.
 * @param <I> The type of item to train
 * @param <C> The type of category to train
 * @author Jeffrey Phillips Freeman
 */
public interface Trainable<I, C>
{
	/**
	 * Causes the Trainable to associate the item with the given category.
	 * @param item The item
	 * @param category The category to associate with the item
	 */
	void train(I item, C category);
}
