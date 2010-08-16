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
package com.syncleus.dann.graph.topological;

import java.util.*;
import com.syncleus.dann.graph.*;

public class SimpleTopologicalSorter<N> implements TopologicalSorter<N>
{
	//TODO rank these via numbers to determine which nodes are at the same level of the hiearchy
	public List<N> sort(final BidirectedGraph<? extends N, ? extends DirectedEdge<? extends N>> graph)
	{
		//initialize data structures
		final Set<N> nodes = new HashSet<N>(graph.getNodes());
		final Set<DirectedEdge<? extends N>> edges = new HashSet<DirectedEdge<? extends N>>(graph.getEdges());
		final Map<N, Set<DirectedEdge<? extends N>>> neighborEdges = new HashMap<N, Set<DirectedEdge<? extends N>>>();
		for(final DirectedEdge<? extends N> edge : edges)
		{
			final List<? extends N> edgeNodes = edge.getNodes();
			for(final N edgeNode : edgeNodes)
			{
				if( !nodes.contains(edgeNode) )
					throw new IllegalArgumentException("A node that is an end point in one of the edges was not in the nodes list");
				java.util.Set<com.syncleus.dann.graph.DirectedEdge<? extends N>> startNeighborEdges = neighborEdges.get(edgeNode);
				if( startNeighborEdges == null )
				{
					startNeighborEdges = new java.util.HashSet<com.syncleus.dann.graph.DirectedEdge<? extends N>>();
					neighborEdges.put(edgeNode, startNeighborEdges);
				}
				startNeighborEdges.add(edge);
			}
		}

		//pull a node of 0 degree then delete
		final List<N> topologicalNodes = new ArrayList<N>();
		while( !nodes.isEmpty() )
		{
			final int preNodeCount = nodes.size();
			for(final N node : nodes)
			{
				if( getIndegree(edges, node) == 0 )
				{
					topologicalNodes.add(node);

					//delete node
					final Set<DirectedEdge<? extends N>> neighbors = neighborEdges.get(node);
					for(final DirectedEdge<? extends N> neighbor : neighbors)
					{
						final List<N> adjacentNodes = new ArrayList<N>(neighbor.getNodes());
						adjacentNodes.remove(node);
						final N adjacentNode = adjacentNodes.get(0);

						//delete the edge from the neighbor map
						final Set<DirectedEdge<? extends N>> deleteFromEdges = neighborEdges.get(adjacentNode);
						deleteFromEdges.remove(neighbor);

						//delete the edge from edges
						edges.remove(neighbor);
					}
					nodes.remove(node);

					//since we found a nod with 0 in degree and removed it we should back out
					break;
				}
			}
			if( preNodeCount <= nodes.size() )
				return null;
		}

		return topologicalNodes;
	}

	public int getIndegree(final Set<DirectedEdge<? extends N>> edges, final N node)
	{
		int inDegree = 0;
		for(final DirectedEdge<? extends N> edge : edges)
			if( edge.getDestinationNode() == node )
				inDegree++;
		return inDegree;
	}
}
