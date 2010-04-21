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
package com.syncleus.dann.graph;

import java.util.*;

public class SimpleTreeGraph<N, E extends BidirectedEdge<? extends N>, W extends BidirectedWalk<? extends N, ? extends E>> extends AbstractTreeGraph<N,E,W>
{
	final private Set<N> nodes;
	final private Set<E> edges;
	final private Map<N, Set<E>> neighborEdges = new HashMap<N, Set<E>>();
	final private Map<N, List<N>> neighborNodes = new HashMap<N, List<N>>();

	public SimpleTreeGraph(Set<? extends N> nodes, Set<? extends E> edges)
	{
		this.nodes = new HashSet<N>(nodes);
		this.edges = new HashSet<E>(edges);
		for(E edge : edges)
		{
			final List<? extends N> edgeNodes = edge.getNodes();
			for(int startNodeIndex = 0; startNodeIndex < edgeNodes.size(); startNodeIndex++)
			{
				if(!this.nodes.contains(edgeNodes.get(startNodeIndex)))
					throw new IllegalArgumentException("A node that is an end point in one of the edges was not in the nodes list");

				Set<E> startNeighborEdges = this.neighborEdges.get(edgeNodes.get(startNodeIndex));
				if( startNeighborEdges == null )
				{
					startNeighborEdges = new HashSet<E>();
					this.neighborEdges.put(edgeNodes.get(startNodeIndex), startNeighborEdges);
				}
				startNeighborEdges.add(edge);

				List<N> startNeighborNodes = this.neighborNodes.get(edgeNodes.get(startNodeIndex));
				if( startNeighborNodes == null )
				{
					startNeighborNodes = new ArrayList<N>();
					this.neighborNodes.put(edgeNodes.get(startNodeIndex), startNeighborNodes);
				}

				for(int endNodeIndex = 0; endNodeIndex < edgeNodes.size(); endNodeIndex++)
				{
					if(startNodeIndex == endNodeIndex)
						continue;

					startNeighborNodes.add(edgeNodes.get(endNodeIndex));
				}
			}
		}
	}

	public Set<N> getNodes()
	{
		return Collections.unmodifiableSet(this.nodes);
	}

	@Override
	public Set<E> getEdges()
	{
		return Collections.unmodifiableSet(this.edges);
	}

	public Set<E> getEdges(N node)
	{
		return Collections.unmodifiableSet(this.neighborEdges.get(node));
	}

	public Set<E> getTraversableEdges(N node)
	{
		return this.getEdges(node);
	}

	public Set<E> getOutEdges(N node)
	{
		return this.getTraversableEdges(node);
	}

	public Set<E> getInEdges(N node)
	{
		return this.getEdges(node);
	}

	public int getIndegree(N node)
	{
		return this.getInEdges(node).size();
	}

	public int getOutdegree(N node)
	{
		return this.getOutEdges(node).size();
	}

	public boolean isConnected(N leftNode, N rightNode)
	{
		return this.neighborNodes.get(leftNode).contains(rightNode);
	}

	public List<N> getNeighbors(N node)
	{
		return Collections.unmodifiableList(this.neighborNodes.get(node));
	}

	public List<N> getTraversableNeighbors(N node)
	{
		return this.getNeighbors(node);
	}
}
