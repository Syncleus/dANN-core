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

import com.syncleus.dann.*;
import com.syncleus.dann.neural.backprop.*;
import java.util.*;
import java.util.concurrent.*;
import org.apache.log4j.Logger;

public abstract class AbstractFeedforwardBrain extends AbstractBackpropBrain
{
	private boolean initialized = false;
	private ArrayList<BackpropNeuronGroup> neuronLayers = new ArrayList<BackpropNeuronGroup>();
	private int layerCount;
	private final static Logger LOGGER = Logger.getLogger(AbstractFeedforwardBrain.class);



	private static class Propagate implements Runnable
	{
		private BackpropNeuron neuron;
		private final static Logger LOGGER = Logger.getLogger(Propagate.class);

		public Propagate(BackpropNeuron neuron)
		{
			this.neuron = neuron;
		}

		public void run()
		{
			try
			{
				this.neuron.propagate();
			}
			catch(Throwable caught)
			{
				LOGGER.error("Throwable caught!", caught);
				throw new DannRuntimeException("Throwable exception caught in Propagate", caught);
			}
		}
	}

	private static class BackPropagate implements Runnable
	{
		private BackpropNeuron neuron;
		private final static Logger LOGGER = Logger.getLogger(BackPropagate.class);

		public BackPropagate(BackpropNeuron neuron)
		{
			this.neuron = neuron;
		}

		public void run()
		{
			try
			{
				this.neuron.backPropagate();
			}
			catch(Throwable caught)
			{
				LOGGER.error("Throwable caught!", caught);
				throw new DannRuntimeException("Throwable exception caught in Propagate", caught);
			}
		}
	}

	/**
	 * Uses the given threadExecutor for executing tasks.
	 *
	 * @param neuronsPerLayer an array of the count of neurons in each layer.
	 * Each element of the array represents the count of neurons for that
	 * particular layer. the first layer (index 0) is the inputlayer, the last
	 * layer (length - 1) is the output layer.
	 * @param threadExecutor executor to use for executing tasks.
	 * @since 2.0
	 */
	public AbstractFeedforwardBrain(ThreadPoolExecutor threadExecutor)
	{
		super(threadExecutor);
	}

	/**
	 * Default constructor initializes a default threadExecutor based on the
	 * number of processors.
	 *
	 * @param neuronsPerLayer an array of the count of neurons in each layer.
	 * Each element of the array represents the count of neurons for that
	 * particular layer. the first layer (index 0) is the inputlayer, the last
	 * layer (length - 1) is the output layer.
	 * @since 2.0
	 */
	public AbstractFeedforwardBrain()
	{
		super();
	}

	protected void initalizeNetwork(int neuronsPerLayer[])
	{
		if(neuronsPerLayer.length < 2)
			throw new IllegalArgumentException("neuronsPerLayer must have atleast 2 elements");

		this.layerCount = neuronsPerLayer.length;

		//create each layer
		int currentLayerCount = 0;
		for(int neuronCount : neuronsPerLayer)
		{
			BackpropNeuronGroup currentGroup = new BackpropNeuronGroup();
			for(int neuronIndex = 0; neuronIndex < neuronCount; neuronIndex++)
			{
				BackpropNeuron currentNeuron = this.createNeuron(currentLayerCount, neuronIndex);

				currentGroup.add(currentNeuron);
				this.addNeuron(currentNeuron);
			}

			this.getNeuronLayers().add(currentGroup);

			currentLayerCount++;
		}

		this.initialized = true;
	}

	/**
	 * Gets the neuronLayers for children to use for connection.
	 *
	 * @return the neuronLayers for children to use for connection.
	 * @since 2.0
	 */
	protected final ArrayList<BackpropNeuronGroup> getNeuronLayers()
	{
		return neuronLayers;
	}

	/**
	 * @return the layerCount
	 */
	public final int getLayerCount()
	{
		return layerCount;
	}

	public final void propagate()
	{
		if(this.initialized == false)
			throw new IllegalStateException("An implementation of AbstractFeedforwardBrain did not initialize network");

		//step forward through all the layers, except the last (output)
		for(int layerIndex = 0; layerIndex < (this.neuronLayers.size()); layerIndex++)
		{
			BackpropNeuronGroup layer = this.neuronLayers.get(layerIndex);
			Set<BackpropNeuron> layerNeurons = layer.getChildrenNeuronsRecursivly();

			//begin processing all neurons in one layer simultaniously
			ArrayList<Future> futures = new ArrayList<Future>();
			for(BackpropNeuron neuron : layerNeurons)
				futures.add(this.getThreadExecutor().submit(new Propagate(neuron)));

			//wait until all neurons have propogated
			try
			{
				for(Future future : futures)
					future.get();
			}
			catch(InterruptedException caught)
			{
				LOGGER.error("Propagate was unexpectidy interupted", caught);
				throw new InterruptedDannRuntimeException("Unexpected interuption. Get should block indefinately", caught);
			}
			catch(ExecutionException caught)
			{
				LOGGER.error("Propagate had an unexcepted problem executing.", caught);
				throw new AssertionError("Unexpected execution exception. Get should block indefinately");
			}

		}
	}

	public final void backPropagate()
	{
		if(this.initialized == false)
			throw new IllegalStateException("An implementation of AbstractFeedforwardBrain did not initialize network");
		
		//step backwards through all the layers, except the first.
		for(int layerIndex = (this.neuronLayers.size()-1); layerIndex >= 0 ; layerIndex--)
		{
			BackpropNeuronGroup layer = this.neuronLayers.get(layerIndex);
			Set<BackpropNeuron> layerNeurons = layer.getChildrenNeuronsRecursivly();

			//begin processing all neurons in one layer simultaniously
			ArrayList<Future> futures = new ArrayList<Future>();
			for(BackpropNeuron neuron : layerNeurons)
				futures.add(this.getThreadExecutor().submit(new BackPropagate(neuron)));

			//wait until all neurons have backPropogated
			try
			{
				for(Future future : futures)
					future.get();
			}
			catch(InterruptedException caught)
			{
				LOGGER.error("BackPropagate was unexpectidy interupted", caught);
				throw new InterruptedDannRuntimeException("Unexpected interuption. Get should block indefinately", caught);
			}
			catch(ExecutionException caught)
			{
				LOGGER.error("BackPropagate had an unexcepted problem executing.", caught);
				throw new AssertionError("Unexpected execution exception. Get should block indefinately");
			}

		}
	}

	/**
	 * Since a specific ActivationFunction or learning rate is needed then this
	 * should be overridden in a child class.
	 *
	 * @param layer the currrent layer index for which we are creating the
	 * neuron.
	 * @param index The index of the new neuron within the layer.
	 * @return The new BackpropNeuron to be added to the current layer.
	 * @since 2.0
	 */
	protected abstract BackpropNeuron createNeuron(int layer, int index);
}
