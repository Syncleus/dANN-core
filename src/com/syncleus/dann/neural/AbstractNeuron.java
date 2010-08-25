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
package com.syncleus.dann.neural;

import java.util.Random;
import org.apache.log4j.Logger;

public abstract class AbstractNeuron implements Neuron
{
	/**
	 * Random number generator used toproduce any needed RANDOM values
	 *
	 * @since 1.0
	 */
	protected static final Random RANDOM = new Random();
	private final Brain<InputNeuron, OutputNeuron, Neuron, Synapse<Neuron>> brain;
	private static final Logger LOGGER = Logger.getLogger(AbstractNeuron.class);

	/**
	 * Creates a new instance of NeuronImpl with a RANDOM bias weight and
	 * HyperbolicTangentActivationFunction as the activation function.
	 *
	 * @since 1.0
	 */
	protected AbstractNeuron(final Brain<InputNeuron, OutputNeuron, Neuron, Synapse<Neuron>> brain)
	{
		if( brain == null )
			throw new IllegalArgumentException("brain can not be null");
		this.brain = brain;
	}

	protected Brain<InputNeuron, OutputNeuron, Neuron, Synapse<Neuron>> getBrain()
	{
		return this.brain;
	}
	// TODO consider making this public and moving it to the neuron interface

	protected abstract double getOutput();
}
