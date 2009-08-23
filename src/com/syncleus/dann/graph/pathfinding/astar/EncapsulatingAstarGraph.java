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
package com.syncleus.dann.graph.pathfinding.astar;

import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.Node;
import com.syncleus.dann.graph.Walk;
import com.syncleus.dann.graph.WeightedEdge;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class EncapsulatingAstarGraph<G extends Graph<? extends Node<? extends Edge>, ? extends Edge, ? extends Walk>> extends AbstractAstarGraph<Node>
{
	private G encapsulatedGraph;
	private boolean useGraphWeights = false;
	private Set<AstarNode<Node>> cachedNodes;
	private Map<Node, AstarNode<Node>> cachedNodeMapping;

	public EncapsulatingAstarGraph(G encapsulatedGraph)
	{
		if( encapsulatedGraph == null )
			throw new IllegalArgumentException("encapsulatedGraph can not be null");

		this.encapsulatedGraph = encapsulatedGraph;

		this.remapSnapshot();
	}

	public EncapsulatingAstarGraph(G encapsulatedGraph, boolean useGraphWeights)
	{
		this(encapsulatedGraph);
		this.useGraphWeights = useGraphWeights;
	}

	public void remapSnapshot()
	{
		Set<? extends Node<? extends Edge>> encapsulatedNodes = this.encapsulatedGraph.getNodes();
		Set<AstarNode<Node>> newNodes = new HashSet<AstarNode<Node>>();
		Map<Node, AstarNode<Node>> newNodeMapping = new HashMap<Node, AstarNode<Node>>();

		//generate new AstarNodes to encapsulate nodes
		for(Node encapsulatedNode : encapsulatedNodes)
		{
			AstarNode<Node> newNode;
			if(useGraphWeights)
				newNode = new AstarNode<Node>(encapsulatedNode, 0.0);
			else
				newNode = new AstarNode<Node>(encapsulatedNode, 1.0);
			newNodes.add(newNode);
			newNodeMapping.put(encapsulatedNode, newNode);
		}

		//generate connections between AstarNodes
		for(Node<? extends Edge> source : encapsulatedNodes)
		{
			AstarNode<Node> newSource = newNodeMapping.get(source);

			List<? extends Edge> paths = source.getTraversableEdges();
			for(Edge path : paths)
			{
				List<Node> destinations = path.getNodes();
				for(Node destination : destinations)
				{
					if(destination.equals(source))
						continue;

					AstarNode<Node> newDestrination = newNodeMapping.get(destination);
					if((useGraphWeights)&&(path instanceof WeightedEdge))
						newSource.connectTo(newDestrination, ((WeightedEdge)path).getWeight().doubleValue());
					else
						newSource.connectTo(newDestrination, 1.0);
				}
			}
		}

		//store the refreshed data to the cache
		this.cachedNodes = newNodes;
		this.cachedNodeMapping = newNodeMapping;
	}

	@Override
	public Set<AstarNode<Node>> getNodes()
	{
		return Collections.unmodifiableSet(cachedNodes);
	}
}
