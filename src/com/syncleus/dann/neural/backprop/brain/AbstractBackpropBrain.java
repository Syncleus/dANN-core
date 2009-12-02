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
package com.syncleus.dann.neural.backprop.brain;

import com.syncleus.dann.neural.AbstractLocalBrain;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractBackpropBrain extends AbstractLocalBrain
{
	/**
	 * Uses the given threadExecutor for executing tasks.
	 *
	 * @param threadExecutor executor to use for executing tasks.
	 * @since 2.0
	 */
	public AbstractBackpropBrain(ThreadPoolExecutor threadExecutor)
	{
		super(threadExecutor);
	}

	/**
	 * Default constructor initializes a default threadExecutor based on the
	 * number of processors.
	 *
	 * @since 2.0
	 */
	public AbstractBackpropBrain()
	{
		super();
	}

	public abstract void propagate();
	public abstract void backPropagate();
}
