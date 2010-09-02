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
import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.backprop.*;

public abstract class AbstractFullyConnectedFeedforwardBrain<IN extends InputBackpropNeuron, ON extends OutputBackpropNeuron, N extends BackpropNeuron, S extends Synapse<N>> extends AbstractFeedforwardBrain<IN,ON,N,S>
{
	private final boolean hasBias;

	/**
	 * Uses the given threadExecutor for executing tasks.
	 *
	 * @param threadExecutor executor to use for executing tasks.
	 * @since 2.0
	 */
	protected AbstractFullyConnectedFeedforwardBrain(final ExecutorService threadExecutor)
	{
		super(threadExecutor);
		this.hasBias = true;
	}

	protected AbstractFullyConnectedFeedforwardBrain(final ExecutorService threadExecutor, final boolean hasBias)
	{
		super(threadExecutor);
		this.hasBias = hasBias;
	}

	/**
	 * Default constructor initializes a default threadExecutor based on the number
	 * of processors.
	 *
	 * @since 2.0
	 */
	protected AbstractFullyConnectedFeedforwardBrain()
	{
		super();
		this.hasBias = true;
	}

	protected AbstractFullyConnectedFeedforwardBrain(final boolean hasBias)
	{
		super();
		this.hasBias = hasBias;
	}

	@Override
	protected final void initalizeNetwork(final int[] neuronsPerLayer)
	{
		//makse sure the parent has a chance to create the unconnected network.
		super.initalizeNetwork(neuronsPerLayer);

		//iterate through all layers (except the last) and connect it to the
		//next layer
		for(int layerIndex = 0; layerIndex < (this.getLayerCount() - 1); layerIndex++)
		{
			final NeuronGroup<N> sourceLayer = this.getEditableLayers().get(layerIndex);
			final NeuronGroup<N> destinationLayer = this.getEditableLayers().get(layerIndex + 1);
			for(final N sourceNeuron : sourceLayer.getChildrenNeuronsRecursivly())
				for(final N destinationNeruon : destinationLayer.getChildrenNeuronsRecursivly())
				{
					//TODO this is bad typing fix this!
					Synapse<N> connection = new SimpleSynapse<N>((N)sourceNeuron, destinationNeruon);
					//TODO this is bad typing fix this!
					this.connect((S) connection, true);
				}
		}
		//create and connect biases
		for(int layerIndex = 1; layerIndex < this.getLayerCount(); layerIndex++)
		{
			for(final N destinationNeuron : this.getEditableLayers().get(layerIndex).getChildrenNeuronsRecursivly())
			{
				if( this.hasBias )
				{
					//create the bias neuron and add it
					final BackpropNeuron biasNeuron = new BackpropStaticNeuron(this, 1.0);
					//TODO this is bad typing fix this!
					this.getEditableLayers().get(layerIndex - 1).add((N)biasNeuron);
					//TODO this is bad typing fix this!
					this.add((N)biasNeuron);
					//connect the new bias neuron to its destination neuron
					//TODO this is bad typing fix this!
					Synapse<N> connection = new SimpleSynapse<N>((N)biasNeuron, destinationNeuron);
					//TODO this is bad typing fix this!
					this.connect((S) connection, true);
				}
			}
		}
	}
}
