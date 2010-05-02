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
import java.util.concurrent.ExecutorService;

public class BrainHyperassociativeMap extends HyperassociativeMap<Brain, Neuron>
{
	public BrainHyperassociativeMap(Brain graph, int dimensions, ExecutorService threadExecutor)
	{
		super(graph, dimensions, threadExecutor);
	}

	public BrainHyperassociativeMap(Brain graph, int dimensions)
	{
		super(graph, dimensions);
	}

	@Override
	Set<Neuron> getNeighbors(Neuron nodeToQuery)
	{
		final Set<Neuron> associations = new HashSet<Neuron>(this.getGraph().getAdjacentNodes(nodeToQuery));
		if(nodeToQuery instanceof InputNeuron)
			associations.addAll(this.getGraph().getInputNeurons());
		else if(nodeToQuery instanceof OutputNeuron)
			associations.addAll(this.getGraph().getOutputNeurons());
		associations.remove(nodeToQuery);

		return associations;
	}
}
