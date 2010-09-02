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
package com.syncleus.dann.neural.som.brain;

import java.util.concurrent.ExecutorService;
import com.syncleus.dann.neural.Synapse;
import com.syncleus.dann.neural.som.*;

/**
 * A SomBrain which uses exponential decay over time for the neighboorhood
 * radius, neighboorhood function, and learning rate.
 *
 * @author Jeffrey Phillips Freeman
 * @since 2.0
 */
public final class ExponentialDecaySomBrain<IN extends SomInputNeuron, ON extends SomOutputNeuron, N extends SomNeuron, S extends Synapse<N>> extends AbstractExponentialDecaySomBrain<IN,ON,N,S>
{
	private static final long serialVersionUID = 4523396585666912034L;

	public ExponentialDecaySomBrain(final int inputCount, final int dimentionality, final int iterationsToConverge, final double initialLearningRate, final ExecutorService executor)
	{
		super(inputCount, dimentionality, iterationsToConverge, initialLearningRate, executor);
	}

	public ExponentialDecaySomBrain(final int inputCount, final int dimentionality, final int iterationsToConverge, final double initialLearningRate)
	{
		this(inputCount, dimentionality, iterationsToConverge, initialLearningRate, null);
	}
}
