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

import java.util.*;
import java.util.concurrent.*;
import com.syncleus.dann.*;
import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.backprop.*;
import org.apache.log4j.Logger;

public abstract class AbstractFeedforwardBrain<IN extends InputBackpropNeuron, ON extends OutputBackpropNeuron, N extends BackpropNeuron, S extends Synapse<N>> extends AbstractLocalBrain<IN,ON,N,S> implements FeedforwardBackpropBrain<IN,ON,N,S>
{
	private boolean initialized = false;
	private final List<NeuronGroup<N>> neuronLayers = new ArrayList<NeuronGroup<N>>();
	private int layerCount;
	private static final Logger LOGGER = Logger.getLogger(AbstractFeedforwardBrain.class);

	private static class Propagate implements Runnable
	{
		private final BackpropNeuron neuron;
		private static final Logger LOGGER = Logger.getLogger(Propagate.class);

		public Propagate(final BackpropNeuron neuron)
		{
			this.neuron = neuron;
		}

		public void run()
		{
			this.neuron.tick();
		}
	}

	private static class BackPropagate implements Runnable
	{
		private final BackpropNeuron neuron;
		private static final Logger LOGGER = Logger.getLogger(BackPropagate.class);

		public BackPropagate(final BackpropNeuron neuron)
		{
			this.neuron = neuron;
		}

		public void run()
		{
			this.neuron.backPropagate();
		}
	}

	/**
	 * Uses the given threadExecutor for executing tasks.
	 *
	 * @param threadExecutor executor to use for executing tasks.
	 * @since 2.0
	 */
	protected AbstractFeedforwardBrain(final ExecutorService threadExecutor)
	{
		super(threadExecutor);
	}

	/**
	 * Default constructor initializes a default threadExecutor based on the number
	 * of processors.
	 *
	 * @since 2.0
	 */
	protected AbstractFeedforwardBrain()
	{
		super();
	}

	protected void initalizeNetwork(final int neuronsPerLayer[])
	{
		if( neuronsPerLayer.length < 2 )
			throw new IllegalArgumentException("neuronsPerLayer must have atleast 2 elements");

		this.layerCount = neuronsPerLayer.length;

		//create each layer
		int currentLayerCount = 0;
		for(final int neuronCount : neuronsPerLayer)
		{
			final NeuronGroup<N> currentGroup = new NeuronGroup<N>();
			for(int neuronIndex = 0; neuronIndex < neuronCount; neuronIndex++)
			{
				final N currentNeuron = this.createNeuron(currentLayerCount, neuronIndex);

				currentGroup.add(currentNeuron);
				this.add(currentNeuron);
			}

			this.neuronLayers.add(currentGroup);

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
	protected final List<NeuronGroup<N>> getEditableLayers()
	{
		return this.neuronLayers;
	}

	public final List<Set<N>> getLayers()
	{
		final List<Set<N>> layerList = new ArrayList<Set<N>>();
		for(final NeuronGroup<N> layerGroup : this.neuronLayers)
		{
			final Set<N> layer = new HashSet<N>();
			for(final N layerNeuron : layerGroup.getChildrenNeuronsRecursivly())
				layer.add(layerNeuron);
			layerList.add(Collections.unmodifiableSet(layer));
		}
		return Collections.unmodifiableList(layerList);
	}

	/**
	 * @return the layerCount
	 */
	public final int getLayerCount()
	{
		return this.layerCount;
	}

	public final void propagate()
	{
		if( !this.initialized )
			throw new IllegalStateException("An implementation of AbstractFeedforwardBrain did not initialize network");
		//step forward through all the layers, except the last (output)
		for(final NeuronGroup<N> layer : this.neuronLayers)
		{
			final Set<N> layerNeurons = layer.getChildrenNeuronsRecursivly();
			if( this.getThreadExecutor() != null )
			{
				//begin processing all neurons in one layer simultaniously
				final java.util.ArrayList<java.util.concurrent.Future> futures = new java.util.ArrayList<java.util.concurrent.Future>();
				for(final com.syncleus.dann.neural.backprop.BackpropNeuron neuron : layerNeurons)
					futures.add(this.getThreadExecutor().submit(new com.syncleus.dann.neural.backprop.brain.AbstractFeedforwardBrain.Propagate(neuron)));
				//wait until all neurons have propogated
				try
				{
					for(final java.util.concurrent.Future future : futures)
						future.get();
				}
				catch(InterruptedException caught)
				{
					LOGGER.warn("Propagate was unexpectidy interupted", caught);
					throw new com.syncleus.dann.UnexpectedInterruptedException("Unexpected interuption. Get should block indefinately", caught);
				}
				catch(java.util.concurrent.ExecutionException caught)
				{
					LOGGER.error("Propagate had an unexcepted problem executing.", caught);
					throw new com.syncleus.dann.UnexpectedDannError("Unexpected execution exception. Get should block indefinately", caught);
				}
			}
			else
				for(final com.syncleus.dann.neural.backprop.BackpropNeuron neuron : layerNeurons)
					neuron.tick();
		}
	}

	public final void backPropagate()
	{
		if( !this.initialized )
			throw new IllegalStateException("An implementation of AbstractFeedforwardBrain did not initialize network");

		//step backwards through all the layers, except the first.
		for(int layerIndex = (this.neuronLayers.size() - 1); layerIndex >= 0; layerIndex--)
		{
			final NeuronGroup<N> layer = this.neuronLayers.get(layerIndex);
			final Set<N> layerNeurons = layer.getChildrenNeuronsRecursivly();

			if( this.getThreadExecutor() != null )
			{
				//begin processing all neurons in one layer simultaniously
				final ArrayList<Future> futures = new ArrayList<Future>();
				for(final BackpropNeuron neuron : layerNeurons)
					futures.add(this.getThreadExecutor().submit(new BackPropagate(neuron)));

				//wait until all neurons have backPropogated
				try
				{
					for(final Future future : futures)
						future.get();
				}
				catch(InterruptedException caught)
				{
					LOGGER.warn("BackPropagate was unexpectidy interupted", caught);
					throw new UnexpectedInterruptedException("Unexpected interuption. Get should block indefinately", caught);
				}
				catch(ExecutionException caught)
				{
					LOGGER.error("BackPropagate had an unexcepted problem executing.", caught);
					throw new UnexpectedDannError("Unexpected execution exception. Get should block indefinately", caught);
				}
			}
			else
				for(final BackpropNeuron neuron : layerNeurons)
					neuron.backPropagate();
		}
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
	protected abstract N createNeuron(int layer, int index);
}
