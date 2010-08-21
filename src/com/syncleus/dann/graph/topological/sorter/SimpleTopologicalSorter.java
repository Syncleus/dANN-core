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
package com.syncleus.dann.graph.topological.sorter;

import java.util.*;
import com.syncleus.dann.graph.*;

public class SimpleTopologicalSorter<N> implements TopologicalSorter<N>
{
	public List<Set<N>> rank(BidirectedGraph<? extends N, ? extends DirectedEdge<? extends N>> graph)
	{
		//initialize data structures
		final Set<N> remainingNodes = new HashSet<N>(graph.getNodes());
		final Set<DirectedEdge<? extends N>> remainingEdges = new HashSet<DirectedEdge<? extends N>>(graph.getEdges());
		final Map<N, Set<DirectedEdge<? extends N>>> remainingNeighborEdges = new HashMap<N, Set<DirectedEdge<? extends N>>>();

		//construct the remainingNeighborEdges with the entire graphs adjacency
		for(final DirectedEdge<? extends N> edge : remainingEdges )
		{
			for(final N edgeNode : edge.getNodes())
			{
				if( !remainingNodes.contains(edgeNode) )
					throw new IllegalArgumentException("A node that is an end point in one of the edges was not in the nodes list");
				java.util.Set<com.syncleus.dann.graph.DirectedEdge<? extends N>> startNeighborEdges = remainingNeighborEdges.get(edgeNode);
				if( startNeighborEdges == null )
				{
					startNeighborEdges = new java.util.HashSet<com.syncleus.dann.graph.DirectedEdge<? extends N>>();
					remainingNeighborEdges.put(edgeNode, startNeighborEdges);
				}
				startNeighborEdges.add(edge);
			}
		}

		//pull all nodes of 0 degree then delete as long as nodes are left
		final List<Set<N>> topologicalNodes = new ArrayList<Set<N>>();
		while( !remainingNodes.isEmpty() )
		{
			//find all nodes current with a in degree of 0
			final Set<N> currentRootNodes = new HashSet<N>();
			for(final N node : remainingNodes )
				if( getIndegree(remainingEdges, node) == 0 )
					currentRootNodes.add(node);

			//if no nodes were found yet some are still remaining then this cant be sorted
			if( currentRootNodes.isEmpty() )
				return null;

			//now lets delete all the nodes we found
			for(N node : currentRootNodes)
			{
				final Set<DirectedEdge<? extends N>> neighbors = remainingNeighborEdges.get(node);
				for(final DirectedEdge<? extends N> neighbor : neighbors)
				{
					final List<N> adjacentNodes = new ArrayList<N>(neighbor.getNodes());
					adjacentNodes.remove(node);
					final N adjacentNode = adjacentNodes.get(0);

					//delete the edge from the neighbor map
					final Set<DirectedEdge<? extends N>> deleteFromEdges = remainingNeighborEdges.get(adjacentNode);
					deleteFromEdges.remove(neighbor);

					//delete the edge from edges
					remainingEdges.remove(neighbor);
				}
				remainingNodes.remove(node);
			}

			//lets add the current root nodes and continue
			topologicalNodes.add(currentRootNodes);
		}

		return topologicalNodes;
	}

	public List<N> sort(final BidirectedGraph<? extends N, ? extends DirectedEdge<? extends N>> graph)
	{
		List<Set<N>> rankedNodes = this.rank(graph);

		//convert ranked nodes into sorted nodes
		List<N> sortedNodes = new ArrayList<N>(graph.getNodes().size());
		for(Set<N> levelNodes : rankedNodes)
			sortedNodes.addAll(levelNodes);

		return sortedNodes;
	}

	private int getIndegree(final Set<DirectedEdge<? extends N>> edges, final N node)
	{
		int inDegree = 0;
		for(final DirectedEdge<? extends N> edge : edges)
			if( edge.getDestinationNode() == node )
				inDegree++;
		return inDegree;
	}
}
