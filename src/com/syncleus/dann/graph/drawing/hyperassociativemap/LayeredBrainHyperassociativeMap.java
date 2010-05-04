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

import com.syncleus.dann.neural.*;
import java.util.*;
import com.syncleus.dann.neural.backprop.BackpropNeuron;
import com.syncleus.dann.neural.backprop.brain.FeedforwardBackpropBrain;
import java.util.concurrent.ExecutorService;

public class LayeredBrainHyperassociativeMap extends HyperassociativeMap<FeedforwardBackpropBrain, Neuron>
{
	private final boolean cached;
	private final Map<BackpropNeuron, Map<Neuron, Double>> neighbors;

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions, double equilibriumDistance, boolean useWeights, ExecutorService threadExecutor, boolean cache)
	{
		super(graph, dimensions, equilibriumDistance, useWeights, threadExecutor);
		cached = cache;
		neighbors = new HashMap<BackpropNeuron, Map<Neuron, Double>>();
	}

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions, ExecutorService threadExecutor, boolean cache)
	{
		this(graph, dimensions, 1.0, true, threadExecutor, cache);
	}

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions, double equilibriumDistance, boolean useWeights, boolean cache)
	{
		this(graph, dimensions, equilibriumDistance, useWeights, null, cache);
	}

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions, boolean cache)
	{
		this(graph, dimensions, 1.0, true, null, cache);
	}

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions, double equilibriumDistance, boolean useWeights, ExecutorService threadExecutor)
	{
		this(graph, dimensions, equilibriumDistance, useWeights, threadExecutor, true);
	}

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions, ExecutorService threadExecutor)
	{
		this(graph, dimensions, 1.0, true, threadExecutor, true);
	}

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions, double equilibriumDistance, boolean useWeights)
	{
		this(graph, dimensions, equilibriumDistance, useWeights, null, true);
	}

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions)
	{
		this(graph, dimensions, 1.0, true, null, true);
	}

	@Override
	Map<Neuron, Double> getNeighbors(Neuron nodeToQuery)
	{
		if(!(nodeToQuery instanceof BackpropNeuron))
			throw new IllegalArgumentException("nodeToQuery must be BackpropNeuron");
		BackpropNeuron neuronToQuery = (BackpropNeuron) nodeToQuery;

		if( (this.cached) && (this.neighbors.containsKey(neuronToQuery)) )
			return this.neighbors.get(neuronToQuery);

		//populate initial associations based off edges
		final Map<Neuron, Double> associations = new HashMap<Neuron, Double>();
		for(Synapse neighborEdge : this.getGraph().getAdjacentEdges(nodeToQuery))
		{
			Double currentWeight = (this.isUsingWeights() ? neighborEdge.getWeight() : this.getEquilibriumDistance() );
			for(Neuron neighbor : neighborEdge.getNodes())
				if( !neighbor.equals(nodeToQuery) )
					associations.put(neighbor, currentWeight);
		}

		//add aditional associations per layer.
		for(Set<BackpropNeuron> layer : this.getGraph().getLayers())
		{
			if(layer.contains(neuronToQuery))
			{
				for(BackpropNeuron layerNeuron : layer)
				{
					if( (neuronToQuery instanceof StaticNeuron) && (layerNeuron instanceof StaticNeuron) )
						associations.put(layerNeuron, this.getEquilibriumDistance());
					else if( (!(neuronToQuery instanceof StaticNeuron)) && (!(layerNeuron instanceof StaticNeuron)) )
						associations.put(layerNeuron, this.getEquilibriumDistance());
				}
			}
		}
		associations.remove(nodeToQuery);

		if(this.cached)
			this.neighbors.put(neuronToQuery, associations);

		return associations;
	}
}
