/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.neural.backprop.brain;

import com.syncleus.dann.neural.NeuronGroup;
import com.syncleus.dann.neural.backprop.BackpropNeuron;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractFullyConnectedFeedforwardBrain extends AbstractFeedforwardBrain
{
	/**
	 * Uses the given threadExecutor for executing tasks.
	 *
	 * @param threadExecutor executor to use for executing tasks.
	 * @since 2.0
	 */
	protected AbstractFullyConnectedFeedforwardBrain(ThreadPoolExecutor threadExecutor)
	{
		super(threadExecutor);
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
	}

	@Override
	protected final void initalizeNetwork(int neuronsPerLayer[])
	{
		//makse sure the parent has a chance to create the unconnected network.
		super.initalizeNetwork(neuronsPerLayer);

		//iterate through all layers (except the last) and connect it to the
		//next layer
		for(int layerIndex = 0; layerIndex < (this.getLayerCount() - 1); layerIndex++)
		{
			NeuronGroup<BackpropNeuron> sourceLayer = this.getNeuronLayers().get(layerIndex);
			NeuronGroup<BackpropNeuron> destinationLayer = this.getNeuronLayers().get(layerIndex + 1);
			for(BackpropNeuron sourceNeuron : sourceLayer.getChildrenNeuronsRecursivly())
				for(BackpropNeuron destinationNeruon : destinationLayer.getChildrenNeuronsRecursivly())
					this.connect(sourceNeuron, destinationNeruon);
		}
	}
}
