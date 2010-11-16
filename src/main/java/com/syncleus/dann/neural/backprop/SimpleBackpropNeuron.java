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
package com.syncleus.dann.neural.backprop;

import java.util.Hashtable;
import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.activation.ActivationFunction;

/**
 * The SimpleBackpropNeuron is the most fundemental component of a backprop
 * network; it is also the proccessor of the system. One neuron will usually
 * connect to many other Neurons through synapses and receive input from many
 * other Neurons in the same way.
 *
 * @author Jeffrey Phillips Freeman
 * @see com.syncleus.dann.neural.SimpleSynapse
 * @since 1.0
 */
public final class SimpleBackpropNeuron extends AbstractBackpropNeuron implements BackpropNeuron
{
	private static final long serialVersionUID = 982375098231423L;

	/**
	 * Creates a new default instance of SimpleBackpropNeuron
	 *
	 * @since 1.0
	 */
	public SimpleBackpropNeuron(final Brain brain)
	{
		super(brain);
	}

	/**
	 * Creates a new instance of SimpleBackpropNeuron with the specified activation
	 * function.
	 *
	 * @param activationFunction The Neuron's activation function.
	 * @since 1.0
	 */
	public SimpleBackpropNeuron(final Brain brain, final ActivationFunction activationFunction)
	{
		super(brain, activationFunction);
	}

	/**
	 * Creates a new instance of a SimpleBackpropNeuron using the default
	 * activation function with the specified learning rate.
	 *
	 * @param learningRate learning rate of this neuron.
	 * @since 1.0
	 */
	public SimpleBackpropNeuron(final Brain brain, final double learningRate)
	{
		super(brain, learningRate);
	}

	/**
	 * Creates a new instance of a SimpleBackpropNeuron with the specified
	 * activtion function and learning rate.
	 *
	 * @param activationFunction Activation function for this neuron.
	 * @param learningRate Learning rate for this neuron.
	 */
	public SimpleBackpropNeuron(final Brain brain, final ActivationFunction activationFunction, final double learningRate)
	{
		super(brain, activationFunction, learningRate);
	}
}
