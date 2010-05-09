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
package com.syncleus.dann.graph.search.pathfinding;

import java.util.*;
import com.syncleus.dann.graph.*;

public class FloydWarshallPathFinder<N, E extends Edge<N>> implements PathFinder<N, E>
{
	private final Graph<N, E> graph;
	private final Map<N, Map<N, Double>> walkWeight;
	private final Map<N, Map<N, N>> nextNode;

	public FloydWarshallPathFinder(final Graph<N, E> graph)
	{
		this.graph = graph;
		//initialize
		this.walkWeight = new HashMap<N, Map<N, Double>>(this.graph.getNodes().size());
		this.nextNode = new HashMap<N, Map<N, N>>(this.graph.getNodes().size());
		for(final N nodeX : this.graph.getNodes())
		{
			final Map<N, Double> weightMapX = new HashMap<N, Double>(this.graph.getNodes().size());
			this.walkWeight.put(nodeX, weightMapX);
			final Map<N, N> nodeMapX = new HashMap<N, N>(this.graph.getNodes().size());
			this.nextNode.put(nodeX, nodeMapX);
			for(final N nodeY : this.graph.getNodes())
			{
				double initialWeight = Double.POSITIVE_INFINITY;
				if (nodeX.equals(nodeY))
					initialWeight = 0.0;
				else if (this.graph.getTraversableNodes(nodeX).contains(nodeY))
				{
					E connectedEdge = null;
					for(final E edge : this.graph.getTraversableEdges(nodeX))
						if (edge.getNodes().contains(nodeY))
							connectedEdge = edge;
					assert connectedEdge != null;
					initialWeight = (connectedEdge instanceof WeightedEdge ? ((WeightedEdge) connectedEdge).getWeight() : 1.0);
					if (nodeY instanceof Weighted)
						initialWeight += ((Weighted) nodeY).getWeight();
				}
				weightMapX.put(nodeY, initialWeight);
				nodeMapX.put(nodeY, null);
			}
		}
		this.calculatePaths();
	}

	private void calculatePaths()
	{
		for(final N nodeK : this.graph.getNodes())
			for(final N nodeX : this.graph.getNodes())
				for(final N nodeY : this.graph.getNodes())
				{
					if (!Double.isInfinite(this.walkWeight.get(nodeX).get(nodeK)) &&
							!Double.isInfinite(this.walkWeight.get(nodeK).get(nodeY)) &&
							this.walkWeight.get(nodeX).get(nodeK) + this.walkWeight.get(nodeK).get(nodeY) < this.walkWeight.get(nodeX).get(nodeY)
							)
					{
						final double newWeight = this.walkWeight.get(nodeX).get(nodeK) + this.walkWeight.get(nodeK).get(nodeY);
						this.walkWeight.get(nodeX).put(nodeY, newWeight);
						this.nextNode.get(nodeX).put(nodeY, nodeK);
					}
				}
	}

	public List<E> getBestPath(final N begin, final N end)
	{
		final List<N> nodePath = getIntermediatePath(begin, end);
		if (nodePath.size() < 2)
			return null;
		final List<E> edgePath = new ArrayList<E>(nodePath.size() - 1);
		double overallWeight = 0.0;
		for(int nodeIndex = 0; nodeIndex < nodePath.size() - 1; nodeIndex++)
		{
			final N fromNode = nodePath.get(nodeIndex);
			final N toNode = nodePath.get(nodeIndex + 1);
			E stepEdge = null;
			double stepEdgeWeight = Double.MAX_VALUE;
			for(final E edge : this.graph.getTraversableEdges(fromNode))
			{
				if (edge.getNodes().contains(toNode))
				{
					if (stepEdge == null)
						stepEdge = edge;
					else if (edge instanceof WeightedEdge)
					{
						if (((WeightedEdge) edge).getWeight() < stepEdgeWeight)
						{
							stepEdge = edge;
							stepEdgeWeight = ((WeightedEdge) edge).getWeight();
						}
					}
					else
						stepEdge = edge;
				}
			}
			assert stepEdge != null;
			edgePath.add(stepEdge);
			if (stepEdge instanceof WeightedEdge)
				overallWeight += ((WeightedEdge) stepEdge).getWeight();
			if (toNode instanceof Weighted)
				overallWeight += ((Weighted) toNode).getWeight();
		}
		return edgePath;
	}

	private List<N> getIntermediatePath(final N begin, final N end)
	{
		if (this.nextNode.get(begin).get(end) == null)
			return new ArrayList<N>();
		final List<N> nodePath = new ArrayList<N>();
		nodePath.addAll(getIntermediatePath(begin, this.nextNode.get(begin).get(end)));
		nodePath.add(this.nextNode.get(begin).get(end));
		nodePath.addAll(getIntermediatePath(this.nextNode.get(begin).get(end), end));
		return nodePath;
	}

	public boolean isReachable(final N begin, final N end)
	{
		return (this.getBestPath(begin, end) != null);
	}

	public boolean isConnected(final N begin, final N end)
	{
		return (this.getBestPath(begin, end) != null);
	}
}
