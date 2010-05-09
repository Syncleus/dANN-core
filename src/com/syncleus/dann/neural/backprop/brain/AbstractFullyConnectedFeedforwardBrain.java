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

import com.syncleus.dann.neural.NeuronGroup;
import com.syncleus.dann.neural.backprop.BackpropNeuron;
import com.syncleus.dann.neural.backprop.BackpropStaticNeuron;
import java.util.concurrent.ExecutorService;

public abstract class AbstractFullyConnectedFeedforwardBrain extends AbstractFeedforwardBrain
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
	 * Default constructor initializes a default threadExecutor based on the
	 * number of processors.
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
			final NeuronGroup<BackpropNeuron> sourceLayer = this.getEditableLayers().get(layerIndex);
			final NeuronGroup<BackpropNeuron> destinationLayer = this.getEditableLayers().get(layerIndex + 1);
			for(final BackpropNeuron sourceNeuron : sourceLayer.getChildrenNeuronsRecursivly())
				for(final BackpropNeuron destinationNeruon : destinationLayer.getChildrenNeuronsRecursivly())
					this.connect(sourceNeuron, destinationNeruon);
		}

		//create and connect biases
		for(int layerIndex = 1; layerIndex < this.getLayerCount(); layerIndex++)
		{
			for(final BackpropNeuron destinationNeuron : this.getEditableLayers().get(layerIndex).getChildrenNeuronsRecursivly())
			{
				if( hasBias )
				{
					//create the bias neuron and add it
					final BackpropNeuron biasNeuron = new BackpropStaticNeuron(this, 1.0);
					this.getEditableLayers().get(layerIndex - 1).add(biasNeuron);
					this.add(biasNeuron);

					//connect the new bias neuron to its destination neuron
					this.connect(biasNeuron, destinationNeuron);
				}
			}
		}
	}
}
