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
	private final Map<BackpropNeuron, Set<Neuron>> neighbors;

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions, ExecutorService threadExecutor, boolean cache)
	{
		super(graph, dimensions, threadExecutor);
		cached = cache;
		neighbors = new HashMap<BackpropNeuron, Set<Neuron>>();
	}

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions, ExecutorService threadExecutor)
	{
		this(graph, dimensions, threadExecutor, true);
	}

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions, boolean cache)
	{
		super(graph, dimensions);
		cached = cache;
		neighbors = new HashMap<BackpropNeuron, Set<Neuron>>();
	}

	public LayeredBrainHyperassociativeMap(FeedforwardBackpropBrain graph, int dimensions)
	{
		this(graph, dimensions, true);
	}

	@Override
	Set<Neuron> getNeighbors(Neuron nodeToQuery)
	{
		if(!(nodeToQuery instanceof BackpropNeuron))
			throw new IllegalArgumentException("nodeToQuery must be BackpropNeuron");
		BackpropNeuron neuronToQuery = (BackpropNeuron) nodeToQuery;

		if( (this.cached) && (this.neighbors.containsKey(neuronToQuery)) )
			return this.neighbors.get(neuronToQuery);

		final Set<Neuron> associations = new HashSet<Neuron>(this.getGraph().getAdjacentNodes(neuronToQuery));

		for(Set<BackpropNeuron> layer : this.getGraph().getLayers())
		{
			if(layer.contains(neuronToQuery))
			{
				for(BackpropNeuron layerNeuron : layer)
				{
					if( (neuronToQuery instanceof StaticNeuron) && (layerNeuron instanceof StaticNeuron) )
						associations.add(layerNeuron);
					else if( (!(neuronToQuery instanceof StaticNeuron)) && (!(layerNeuron instanceof StaticNeuron)) )
						associations.add(layerNeuron);
				}
			}
		}
		associations.remove(nodeToQuery);

		if(this.cached)
			this.neighbors.put(neuronToQuery, associations);

		return associations;
	}
}
