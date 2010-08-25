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
package com.syncleus.dann.graph.drawing.hyperassociativemap;

import java.util.*;
import java.util.concurrent.ExecutorService;
import com.syncleus.dann.neural.*;
import com.syncleus.dann.neural.backprop.*;
import com.syncleus.dann.neural.backprop.brain.FeedforwardBackpropBrain;

public class LayeredBrainHyperassociativeMap extends HyperassociativeMap<FeedforwardBackpropBrain<InputBackpropNeuron, OutputBackpropNeuron, BackpropNeuron, Synapse<BackpropNeuron>>, BackpropNeuron>
{
	private final boolean cached;
	private final Map<BackpropNeuron, Map<BackpropNeuron, Double>> neighbors;

	public LayeredBrainHyperassociativeMap(final FeedforwardBackpropBrain graph, final int dimensions, final double equilibriumDistance, final boolean useWeights, final ExecutorService threadExecutor, final boolean cache)
	{
		super(graph, dimensions, equilibriumDistance, useWeights, threadExecutor);
		this.cached = cache;
		this.neighbors = new HashMap<BackpropNeuron, Map<BackpropNeuron, Double>>();
	}

	public LayeredBrainHyperassociativeMap(final FeedforwardBackpropBrain graph, final int dimensions, final ExecutorService threadExecutor, final boolean cache)
	{
		this(graph, dimensions, 1.0, true, threadExecutor, cache);
	}

	public LayeredBrainHyperassociativeMap(final FeedforwardBackpropBrain graph, final int dimensions, final double equilibriumDistance, final boolean useWeights, final boolean cache)
	{
		this(graph, dimensions, equilibriumDistance, useWeights, null, cache);
	}

	public LayeredBrainHyperassociativeMap(final FeedforwardBackpropBrain graph, final int dimensions, final boolean cache)
	{
		this(graph, dimensions, 1.0, true, null, cache);
	}

	public LayeredBrainHyperassociativeMap(final FeedforwardBackpropBrain graph, final int dimensions, final double equilibriumDistance, final boolean useWeights, final ExecutorService threadExecutor)
	{
		this(graph, dimensions, equilibriumDistance, useWeights, threadExecutor, true);
	}

	public LayeredBrainHyperassociativeMap(final FeedforwardBackpropBrain graph, final int dimensions, final ExecutorService threadExecutor)
	{
		this(graph, dimensions, 1.0, true, threadExecutor, true);
	}

	public LayeredBrainHyperassociativeMap(final FeedforwardBackpropBrain graph, final int dimensions, final double equilibriumDistance, final boolean useWeights)
	{
		this(graph, dimensions, equilibriumDistance, useWeights, null, true);
	}

	public LayeredBrainHyperassociativeMap(final FeedforwardBackpropBrain graph, final int dimensions)
	{
		this(graph, dimensions, 1.0, true, null, true);
	}

	@Override
	Map<BackpropNeuron, Double> getNeighbors(final BackpropNeuron nodeToQuery)
	{
		if( !(nodeToQuery instanceof BackpropNeuron) )
			throw new IllegalArgumentException("nodeToQuery must be BackpropNeuron");
		final BackpropNeuron neuronToQuery = (BackpropNeuron) nodeToQuery;

		if( (this.cached) && (this.neighbors.containsKey(neuronToQuery)) )
			return this.neighbors.get(neuronToQuery);

		//populate initial associations based off edges
		final Map<BackpropNeuron, Double> associations = new HashMap<BackpropNeuron, Double>();
		for(final Synapse neighborEdge : this.getGraph().getAdjacentEdges(nodeToQuery))
		{
			final Double currentWeight = (this.isUsingWeights() ? neighborEdge.getWeight() : this.getEquilibriumDistance());
			//TODO fix this typing
			for(final Object neighbor : neighborEdge.getNodes())
				if( !neighbor.equals(nodeToQuery) )
					associations.put((BackpropNeuron)neighbor, currentWeight);
		}

		//add aditional associations per layer.
		for(final Set<BackpropNeuron> layer : this.getGraph().getLayers())
		{
			if( layer.contains(neuronToQuery) )
			{
				for(final BackpropNeuron layerNeuron : layer)
				{
					if( (neuronToQuery instanceof BackpropStaticNeuron) && (layerNeuron instanceof BackpropStaticNeuron) )
						associations.put(layerNeuron, this.getEquilibriumDistance());
					else if( (!(neuronToQuery instanceof BackpropStaticNeuron)) && (!(layerNeuron instanceof BackpropStaticNeuron)) )
						associations.put(layerNeuron, this.getEquilibriumDistance());
				}
			}
		}
		associations.remove(nodeToQuery);

		if( this.cached )
			this.neighbors.put(neuronToQuery, associations);

		return associations;
	}
}
