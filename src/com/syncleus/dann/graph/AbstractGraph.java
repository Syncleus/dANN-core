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

import com.syncleus.dann.math.set.Combinations;
import java.util.*;

public abstract class AbstractGraph<N, E extends Edge<? extends N>, W extends Walk<? extends N, ? extends E>> implements Graph<N,E,W>
{
	public int getDegree(N node)
	{
		List<E> adjacentEdges = this.getEdges(node);
		int degree = 0;
		for(E adjacentEdge : adjacentEdges)
			for(N adjacentNode : adjacentEdge.getNodes())
				if(adjacentNode == node)
					degree++;
		return degree;
	}
	
	public boolean isConnected()
	{
		Set<N> nodes = this.getNodes();
		for(N fromNode : nodes)
			for(N toNode : nodes)
				if((toNode != fromNode)&&(!this.isConnected(toNode, fromNode)))
					return false;
		return true;
	}

	public Set<Graph<N,E,W>> getConnectedComponents()
	{
		return null;
	}

	public boolean isMaximalSubgraph(Graph<? extends N, ? extends E, ? extends W> subgraph)
	{
		if( !this.isSubGraph(subgraph))
			return false;

		//find all edges in the parent graph, but not in the subgraph
		final List<E> exclusiveParentEdges = this.getEdges();
		final List<? extends E> subedges = subgraph.getEdges();
		exclusiveParentEdges.removeAll(subedges);

		//check to make sure none of the edges exclusive to the parent graph
		//connect to any of the nodes in the subgraph.
		final Set<? extends N> subnodes = subgraph.getNodes();
		for(E exclusiveParentEdge : exclusiveParentEdges)
			for(N exclusiveParentNode : exclusiveParentEdge.getNodes())
				if(subnodes.contains(exclusiveParentNode))
					return false;

		//passed all the tests, must be maximal
		return true;
	}

	private SimpleGraph deleteFromGraph(Set<? extends N> nodes, List<? extends E> edges)
	{
		//remove the nodes
		final Set cutNodes = this.getNodes();
		cutNodes.removeAll(nodes);

		//remove the edges
		final List<Edge> cutEdges = new ArrayList<Edge>(this.getEdges());
		for(E edge : edges)
			cutEdges.remove(edge);

		//remove any remaining edges which connect to removed nodes
		//also replace edges that have one removed node but still have
		//2 or more remaining nodes with a new edge.
		final List<Edge> removeEdges = new ArrayList();
		final List<Edge> addEdges = new ArrayList();
		for(Edge cutEdge : cutEdges)
		{
			final List<? extends N> cutEdgeNeighbors = cutEdge.getNodes();
			cutEdgeNeighbors.removeAll(cutNodes);
			if( cutEdgeNeighbors.size() != cutEdge.getNodes().size())
				removeEdges.add(cutEdge);
			if( cutEdgeNeighbors.size() > 1)
				addEdges.add(new SimpleEdge(cutEdgeNeighbors));
		}
		for(Edge removeEdge : removeEdges)
			cutEdges.remove(removeEdge);
		cutEdges.addAll(addEdges);

		//check if a graph fromt he new set of edges and nodes is still
		//connected
		return new SimpleGraph(cutNodes, cutEdges);
	}

	public boolean isCut(Set<? extends N> nodes, List<? extends E> edges)
	{
		return this.deleteFromGraph(nodes, edges).isConnected();
	}

	public boolean isCut(Set<? extends N> nodes, List<? extends E> edges, N begin, N end)
	{
		return this.deleteFromGraph(nodes, edges).isConnected(begin, end);
	}

	public boolean isCut(Set<? extends N> nodes)
	{
		return this.isCut(nodes, new ArrayList<E>());
	}

	public boolean isCut(List<? extends E> edges)
	{
		return this.isCut(new HashSet<N>(), edges);
	}

	public boolean isCut(N node)
	{
		return this.isCut(Collections.singleton(node), new ArrayList<E>());
	}

	public boolean isCut(E edge)
	{
		return this.isCut(new HashSet<N>(), Collections.singletonList(edge));
	}

	public boolean isCut(Set<? extends N> nodes, N begin, N end)
	{
		return this.isCut(nodes, new ArrayList<E>(), begin, end);
	}

	public boolean isCut(List<? extends E> edges, N begin, N end)
	{
		return this.isCut(new HashSet<N>(), edges, begin, end);
	}

	public boolean isCut(N node, N begin, N end)
	{
		return this.isCut(Collections.singleton(node), new ArrayList<E>(), begin, end);
	}

	public boolean isCut(E edge, N begin, N end)
	{
		return this.isCut(new HashSet<N>(), Collections.singletonList(edge), begin, end);
	}

	public int getNodeConnectivity()
	{
		final Set<Set<N>> combinations = Combinations.everyCombination(this.getNodes());
		final SortedSet<Set<N>> sortedCombinations = new TreeSet<Set<N>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(Set<N> cutNodes : combinations)
			if(this.isCut(cutNodes))
				return cutNodes.size();
		return this.getNodes().size();
	}

	public int getEdgeConnectivity()
	{
		final Set<List<E>> combinations = Combinations.everyCombination(this.getEdges());
		final SortedSet<List<E>> sortedCombinations = new TreeSet<List<E>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(List<E> cutEdges : combinations)
			if(this.isCut(cutEdges))
				return cutEdges.size();
		return this.getEdges().size();
	}

	public int getNodeConnectivity(N begin, N end)
	{
		return 0;
	}

	public int getEdgeConnectivity(N begin, N end)
	{
		return 0;
	}

	public boolean isComplete()
	{
		return false;
	}

	public int getOrder()
	{
		return 0;
	}

	public int getCycleCount()
	{
		return 0;
	}

	public boolean isPancyclic()
	{
		return false;
	}

	public int getGirth()
	{
		return 0;
	}

	public int getCircumference()
	{
		return 0;
	}

	public boolean isTraceable()
	{
		return false;
	}

	public boolean isSpanning(W walk)
	{
		return false;
	}

	public boolean isSpanning(TreeGraph graph)
	{
		return false;
	}

	public boolean isTraversable()
	{
		return false;
	}

	public boolean isEularian(W walk)
	{
		return false;
	}

	public boolean isTree()
	{
		return false;
	}

	public boolean isSubGraph(Graph<? extends N, ? extends E, ? extends W> subgraph)
	{
		Set<N> nodes = this.getNodes();
		Set<? extends N> subnodes = subgraph.getNodes();
		for(N subnode : subnodes)
			if( !nodes.contains(subnode) )
				return false;

		List<E> edges = this.getEdges();
		List<? extends E> subedges = subgraph.getEdges();
		for(E subedge : subedges)
			if( !edges.contains(subedge))
				return false;

		return true;
	}

	public boolean isKnot(Graph<? extends N, ? extends E, ? extends W> subGraph)
	{
		return false;
	}

	public int getTotalDegree()
	{
		return 0;
	}

	public boolean isMultigraph()
	{
		return false;
	}

	public boolean isIsomorphic(Graph<? extends N, ? extends E, ? extends W> isomorphicGraph)
	{
		return false;
	}

	public boolean isHomomorphic(Graph<? extends N, ? extends E, ? extends W> homomorphicGraph)
	{
		return false;
	}

	public boolean isRegular()
	{
		return false;
	}

	private static class SizeComparator<O> implements Comparator<Collection>
	{
		public int compare(Collection first, Collection second)
		{
			if(first.size() < second.size())
				return -1;
			else if(first.size() > second.size())
				return 1;
			return 0;
		}

		@Override
		public boolean equals(Object compareWith)
		{
			if(compareWith instanceof SizeComparator)
				return true;
			return false;
		}

		@Override
		public int hashCode()
		{
			return super.hashCode();
		}
	}
}
