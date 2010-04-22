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
 *  findCycles a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph;

import com.syncleus.dann.graph.cycle.CycleFinder;
import com.syncleus.dann.graph.cycle.ExhaustiveDepthFirstSearchCycleFinder;
import com.syncleus.dann.math.set.Combinations;
import java.util.*;

public abstract class AbstractGraph<N, E extends Edge<? extends N>> implements Graph<N,E>
{
	public int getDegree(N node)
	{
		Set<E> adjacentEdges = this.getEdges(node);
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

	public boolean isConnected(N leftNode, N rightNode)
	{
		Set<N> visited = new HashSet<N>();
		visited.add(leftNode);

		Set<N> toVisit = new HashSet<N>();
		toVisit.addAll(this.getTraversableNeighbors(leftNode));

		while(!toVisit.isEmpty())
		{
			N node = (N) toVisit.toArray()[0];
			if(node.equals(rightNode))
				return true;

			visited.add(node);
			toVisit.remove(node);

			toVisit.addAll(this.getTraversableNeighbors(node));
			toVisit.removeAll(visited);
		}

		return false;
	}

	public Set<Graph<N,E>> getConnectedComponents()
	{
		return null;
	}

	public boolean isMaximalSubgraph(Graph<? extends N, ? extends E> subgraph)
	{
		if( !this.isSubGraph(subgraph))
			return false;

		//findCycles all edges in the parent graph, but not in the subgraph
		final Set<E> exclusiveParentEdges = new HashSet<E>(this.getEdges());
		final Set<? extends E> subedges = subgraph.getEdges();
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

	private SimpleGraph deleteFromGraph(Set<? extends N> nodes, Set<? extends E> edges)
	{
		//remove the nodes
		final Set cutNodes = this.getNodes();
		cutNodes.removeAll(nodes);

		//remove the edges
		final Set<Edge> cutEdges = new HashSet<Edge>(this.getEdges());
		for(E edge : edges)
			cutEdges.remove(edge);

		//remove any remaining edges which connect to removed nodes
		//also replace edges that have one removed node but still have
		//2 or more remaining nodes with a new edge.
		final Set<Edge> removeEdges = new HashSet();
		final Set<Edge> addEdges = new HashSet();
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

	public boolean isCut(Set<? extends N> nodes, Set<? extends E> edges)
	{
		return this.deleteFromGraph(nodes, edges).isConnected();
	}

	public boolean isCut(Set<? extends N> nodes, Set<? extends E> edges, N begin, N end)
	{
		return this.deleteFromGraph(nodes, edges).isConnected(begin, end);
	}

	public boolean isCut(Set<? extends E> edges)
	{
		return this.isCut(Collections.EMPTY_SET, edges);
	}

	public boolean isCut(N node)
	{
		return this.isCut(Collections.singleton(node), Collections.EMPTY_SET);
	}

	public boolean isCut(E edge)
	{
		return this.isCut(Collections.EMPTY_SET, Collections.singleton(edge));
	}

	public boolean isCut(Set<? extends E> edges, N begin, N end)
	{
		return this.isCut(Collections.EMPTY_SET, edges, begin, end);
	}

	public boolean isCut(N node, N begin, N end)
	{
		return this.isCut(Collections.singleton(node), Collections.EMPTY_SET, begin, end);
	}

	public boolean isCut(E edge, N begin, N end)
	{
		return this.isCut(Collections.EMPTY_SET, Collections.singleton(edge), begin, end);
	}

	public int getNodeConnectivity()
	{
		final Set<Set<N>> combinations = Combinations.everyCombination(this.getNodes());
		final SortedSet<Set<N>> sortedCombinations = new TreeSet<Set<N>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(Set<N> cutNodes : combinations)
			if(this.isCut(cutNodes, Collections.EMPTY_SET))
				return cutNodes.size();
		return this.getNodes().size();
	}

	public int getEdgeConnectivity()
	{
		final Set<Set<E>> combinations = Combinations.everyCombination(this.getEdges());
		final SortedSet<Set<E>> sortedCombinations = new TreeSet<Set<E>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(Set<E> cutEdges : combinations)
			if(this.isCut(cutEdges))
				return cutEdges.size();
		return this.getEdges().size();
	}

	public int getNodeConnectivity(N begin, N end)
	{
		final Set<Set<N>> combinations = Combinations.everyCombination(this.getNodes());
		final SortedSet<Set<N>> sortedCombinations = new TreeSet<Set<N>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(Set<N> cutNodes : combinations)
			if(this.isCut(cutNodes, Collections.EMPTY_SET, begin, end))
				return cutNodes.size();
		return this.getNodes().size();
	}

	public int getEdgeConnectivity(N begin, N end)
	{
		final Set<Set<E>> combinations = Combinations.everyCombination(this.getEdges());
		final SortedSet<Set<E>> sortedCombinations = new TreeSet<Set<E>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(Set<E> cutEdges : combinations)
			if(this.isCut(cutEdges, begin, end))
				return cutEdges.size();
		return this.getEdges().size();
	}

	public boolean isComplete()
	{
		for(N startNode : this.getNodes())
			for(N endNode : this.getNodes())
				if(!startNode.equals(endNode))
					if(!this.getTraversableNeighbors(startNode).contains(endNode))
						return false;
		return true;
	}

	public int getOrder()
	{
		return this.getNodes().size();
	}

	public int getCycleCount()
	{
		CycleFinder finder = new ExhaustiveDepthFirstSearchCycleFinder();
		return finder.cycleCount(this);
	}

	public boolean isPancyclic()
	{
		CycleFinder finder = new ExhaustiveDepthFirstSearchCycleFinder();
		return finder.isPancyclic(this);
	}

	public boolean isUnicyclic()
	{
		CycleFinder finder = new ExhaustiveDepthFirstSearchCycleFinder();
		return finder.isUnicyclic(this);
	}

	public int getGirth()
	{
		CycleFinder finder = new ExhaustiveDepthFirstSearchCycleFinder();
		return finder.girth(this);
	}

	public int getCircumference()
	{
		CycleFinder finder = new ExhaustiveDepthFirstSearchCycleFinder();
		return finder.circumference(this);
	}

	public boolean isTraceable()
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

	public boolean isTree()
	{
		return (this.getCycleCount() == 0);
	}

	public boolean isSubGraph(Graph<? extends N, ? extends E> subgraph)
	{
		Set<N> nodes = this.getNodes();
		Set<? extends N> subnodes = subgraph.getNodes();
		for(N subnode : subnodes)
			if( !nodes.contains(subnode) )
				return false;

		Set<E> edges = this.getEdges();
		Set<? extends E> subedges = subgraph.getEdges();
		for(E subedge : subedges)
			if( !edges.contains(subedge))
				return false;

		return true;
	}

	public boolean isKnot(Graph<? extends N, ? extends E> subGraph)
	{
		return false;
	}

	public int getMinimumDegree()
	{
		if(this.getNodes().size() == 0)
			throw new IllegalStateException("This graph has no nodes!");
		
		int minimumDegree = Integer.MAX_VALUE;
		for(N node : this.getNodes())
			if(this.getDegree(node) < minimumDegree)
				minimumDegree = this.getDegree(node);
		return minimumDegree;
	}

	public boolean isMultigraph()
	{
		return false;
	}

	public boolean isIsomorphic(Graph<? extends N, ? extends E> isomorphicGraph)
	{
		return false;
	}

	public boolean isHomomorphic(Graph<? extends N, ? extends E> homomorphicGraph)
	{
		return false;
	}

	public boolean isRegular()
	{
		int degree = -1;
		for(N node : this.getNodes())
		{
			if(degree == -1)
				degree = this.getDegree(node);
			else
				if(degree != this.getDegree(node))
					return false;
		}
		return true;
	}

	public int getRegularDegree()
	{
		int degree = -1;
		for(N node : this.getNodes())
		{
			if(degree == -1)
				degree = this.getDegree(node);
			else
				if(degree != this.getDegree(node))
					return -1;
		}

		if(degree == -1)
			throw new IllegalStateException("This graph has no nodes!");

		return degree;
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
