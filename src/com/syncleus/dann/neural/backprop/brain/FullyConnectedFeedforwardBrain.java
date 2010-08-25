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

import java.util.concurrent.ExecutorService;
import com.syncleus.dann.neural.Synapse;
import com.syncleus.dann.neural.activation.ActivationFunction;
import com.syncleus.dann.neural.backprop.*;

public final class FullyConnectedFeedforwardBrain<IN extends InputBackpropNeuron, ON extends OutputBackpropNeuron, N extends BackpropNeuron, S extends Synapse<N>> extends AbstractFullyConnectedFeedforwardBrain<IN,ON,N,S>
{
	private static final long serialVersionUID = 3666884827880527998L;
	private final double learningRate;
	private final ActivationFunction activationFunction;

	/**
	 * Uses the given threadExecutor for executing tasks.
	 *
	 * @param threadExecutor executor to use for executing tasks.
	 * @since 2.0
	 */
	public FullyConnectedFeedforwardBrain(final int[] neuronsPerLayer, final double learningRate, final ActivationFunction activationFunction, final ExecutorService threadExecutor)
	{
		super(threadExecutor);
		this.learningRate = learningRate;
		this.activationFunction = activationFunction;

		this.initalizeNetwork(neuronsPerLayer);
	}

	/**
	 * Default constructor initializes a default threadExecutor based on the number
	 * of processors.
	 *
	 * @since 2.0
	 */
	public FullyConnectedFeedforwardBrain(final int[] neuronsPerLayer, final double learningRate, final ActivationFunction activationFunction)
	{
		super();
		this.learningRate = learningRate;
		this.activationFunction = activationFunction;

		this.initalizeNetwork(neuronsPerLayer);
	}

	/**
	 * Since a specific ActivationFunction or learning rate is needed then this
	 * should be overridden in a child class.
	 *
	 * @param layer the currrent layer index for which we are creating the neuron.
	 * @param index The index of the new neuron within the layer.
	 * @return The new SimpleBackpropNeuron to be added to the current layer.
	 * @since 2.0
	 */
	protected N createNeuron(final int layer, final int index)
	{
		final BackpropNeuron neuron;
		if( layer == 0 )
			neuron = new SimpleInputBackpropNeuron(this);
		else if( layer >= (this.getLayerCount() - 1) )
			neuron = new SimpleOutputBackpropNeuron(this, this.activationFunction, this.learningRate);
		else
			neuron = new SimpleBackpropNeuron(this, this.activationFunction, this.learningRate);

		//TODO fix this typing
		return (N) neuron;
	}
}
