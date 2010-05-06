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

import com.syncleus.dann.graph.cycle.*;
import java.util.*;
import com.syncleus.dann.math.set.Combinations;
import java.io.Serializable;

public abstract class AbstractGraph<N, E extends Edge<N>> implements Graph<N,E>
{
	public List<N> getTraversableNodes(final N node)
	{
		final List<N> traversableNodes = new ArrayList<N>();
		for(E adjacentEdge : this.getAdjacentEdges(node))
			traversableNodes.addAll(adjacentEdge.getTraversableNodes(node));
		return Collections.unmodifiableList(traversableNodes);
	}

	public Set<E> getTraversableEdges(final N node)
	{
		final Set<E> traversableEdges = new HashSet<E>();
		for(E adjacentEdge : this.getAdjacentEdges(node))
			if(adjacentEdge.isTraversable(node))
				traversableEdges.add(adjacentEdge);
		return Collections.unmodifiableSet(traversableEdges);
	}
	
	public int getDegree(final N node)
	{
		final Set<E> adjacentEdges = this.getAdjacentEdges(node);
		int degree = 0;
		for(E adjacentEdge : adjacentEdges)
			for(N adjacentNode : adjacentEdge.getNodes())
				if(adjacentNode.equals(node))
					degree++;
		return degree;
	}
	
	public boolean isStronglyConnected()
	{
		final Set<N> nodes = this.getNodes();
		for(N fromNode : nodes)
			for(N toNode : nodes)
				if((toNode != fromNode)&&(!this.isStronglyConnected(toNode, fromNode)))
					return false;
		return true;
	}

	public boolean isWeaklyConnected()
	{
		final List<N> remainingNodes = new ArrayList<N>(this.getNodes());
		while(remainingNodes.size() >= 2)
		{
			final N fromNode = remainingNodes.get(0);
			for(N toNode : remainingNodes)
				if((toNode != fromNode)&&(!this.isWeaklyConnected(toNode, fromNode)))
					return false;
		}
		return true;
	}

	public boolean isWeaklyConnected(final N leftNode, final N rightNode)
	{
		final Set<N> visited = new HashSet<N>();
		visited.add(leftNode);

		final Set<N> toVisit = new HashSet<N>();
		toVisit.addAll(this.getAdjacentNodes(leftNode));

		while(!toVisit.isEmpty())
		{
			final N node = toVisit.iterator().next();
			if(node.equals(rightNode))
				return true;

			visited.add(node);
			toVisit.remove(node);

			toVisit.addAll(this.getAdjacentNodes(node));
			toVisit.removeAll(visited);
		}

		return false;
	}

	public boolean isStronglyConnected(final N leftNode, final N rightNode)
	{
		final Set<N> visited = new HashSet<N>();
		visited.add(leftNode);

		final Set<N> toVisit = new HashSet<N>();
		toVisit.addAll(this.getTraversableNodes(leftNode));

		while(!toVisit.isEmpty())
		{
			final N node = toVisit.iterator().next();
			if(node.equals(rightNode))
				return true;

			visited.add(node);
			toVisit.remove(node);

			toVisit.addAll(this.getTraversableNodes(node));
			toVisit.removeAll(visited);
		}

		return false;
	}

	public Set<Graph<N,E>> getMaximallyConnectedComponents()
	{
		// TODO fill this in
		return null;
	}

	public boolean isMaximalSubgraph(final Graph<N,E> subgraph)
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

	private ImmutableGraph<N,Edge<N>> deleteFromGraph(final Set<N> nodes, final Set<E> edges)
	{
		//remove the nodes
		final Set<N> cutNodes = this.getNodes();
		cutNodes.removeAll(nodes);

		//remove the edges
		final Set<Edge<N>> cutEdges = new HashSet<Edge<N>>(this.getEdges());
		for(E edge : edges)
			cutEdges.remove(edge);

		//remove any remaining edges which connect to removed nodes
		//also replace edges that have one removed node but still have
		//2 or more remaining nodes with a new edge.
		final Set<Edge<N>> removeEdges = new HashSet<Edge<N>>();
		final Set<Edge<N>> addEdges = new HashSet<Edge<N>>();
		for(Edge<N> cutEdge : cutEdges)
		{
			final List<N> cutEdgeNeighbors = cutEdge.getNodes();
			cutEdgeNeighbors.removeAll(cutNodes);
			if( cutEdgeNeighbors.size() != cutEdge.getNodes().size())
				removeEdges.add(cutEdge);
			if( cutEdgeNeighbors.size() > 1)
				// TODO instead of ImmutableHyperEdge implement clone or something
				addEdges.add(new ImmutableHyperEdge<N>(cutEdgeNeighbors));
		}
		for(Edge<N> removeEdge : removeEdges)
			cutEdges.remove(removeEdge);
		cutEdges.addAll(addEdges);

		//check if a graph fromt he new set of edges and nodes is still
		//connected
		return new ImmutableGraph<N,Edge<N>>(cutNodes, cutEdges);
	}

	public boolean isCut(final Set<N> nodes, final Set<E> edges)
	{
		return this.deleteFromGraph(nodes, edges).isStronglyConnected();
	}

	public boolean isCut(final Set<N> nodes, final Set<E> edges, final N begin, final N end)
	{
		return this.deleteFromGraph(nodes, edges).isStronglyConnected(begin, end);
	}

	public boolean isCut(final Set<E> edges)
	{
		return this.isCut(Collections.<N>emptySet(), edges);
	}

	public boolean isCut(final N node)
	{
		return this.isCut(Collections.singleton(node), Collections.<E>emptySet());
	}

	public boolean isCut(final E edge)
	{
		return this.isCut(Collections.<N>emptySet(), Collections.singleton(edge));
	}

	public boolean isCut(final Set<E> edges, final N begin, final N end)
	{
		return this.isCut(Collections.<N>emptySet(), edges, begin, end);
	}

	public boolean isCut(final N node, final N begin, final N end)
	{
		return this.isCut(Collections.singleton(node), Collections.<E>emptySet(), begin, end);
	}

	public boolean isCut(final E edge, final N begin, final N end)
	{
		return this.isCut(Collections.<N>emptySet(), Collections.singleton(edge), begin, end);
	}

	public int getNodeConnectivity()
	{
		final Set<Set<N>> combinations = Combinations.everyCombination(this.getNodes());
		final SortedSet<Set<N>> sortedCombinations = new TreeSet<Set<N>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(Set<N> cutNodes : combinations)
			if(this.isCut(cutNodes, Collections.<E>emptySet()))
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

	public int getNodeConnectivity(final N begin, final N end)
	{
		final Set<Set<N>> combinations = Combinations.everyCombination(this.getNodes());
		final SortedSet<Set<N>> sortedCombinations = new TreeSet<Set<N>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(Set<N> cutNodes : combinations)
			if(this.isCut(cutNodes, Collections.<E>emptySet(), begin, end))
				return cutNodes.size();
		return this.getNodes().size();
	}

	public int getEdgeConnectivity(final N begin, final N end)
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
		if(!this.isSimple())
			return false;
		
		for(N startNode : this.getNodes())
			for(N endNode : this.getNodes())
				if(!startNode.equals(endNode))
					if(!this.getAdjacentNodes(startNode).contains(endNode))
						return false;
		return true;
	}

	public int getOrder()
	{
		return this.getNodes().size();
	}

	public int getCycleCount()
	{
		final CycleFinder<N,E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N,E>();
		return finder.cycleCount(this);
	}

	public boolean isPancyclic()
	{
		final CycleFinder<N,E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N,E>();
		return finder.isPancyclic(this);
	}

	public boolean isUnicyclic()
	{
		final CycleFinder<N,E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N,E>();
		return finder.isUnicyclic(this);
	}

	public boolean isAcyclic()
	{
		final CycleFinder finder = new ExhaustiveDepthFirstSearchCycleFinder();
		return !finder.hasCycle(this);
	}

	public int getGirth()
	{
		final CycleFinder<N,E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N,E>();
		return finder.girth(this);
	}

	public int getCircumference()
	{
		final CycleFinder<N,E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N,E>();
		return finder.circumference(this);
	}

	public boolean isSpanningTree(final Graph<N,E> graph)
	{
		return ((this.getNodes().containsAll(graph.getNodes())) &&
			(graph.isWeaklyConnected()) &&
			(graph.isAcyclic()));
	}

	public boolean isTree()
	{
		return ((this.isWeaklyConnected())&&(this.isAcyclic())&&(this.isSimple()));
	}

	public boolean isForest()
	{
		// TODO fill this in
		return false;
	}

	public boolean isSubGraph(final Graph<N,E> subgraph)
	{
		final Set<N> nodes = this.getNodes();
		final Set<N> subnodes = subgraph.getNodes();
		for(N subnode : subnodes)
			if( !nodes.contains(subnode) )
				return false;

		final Set<E> edges = this.getEdges();
		final Set<E> subedges = subgraph.getEdges();
		for(E subedge : subedges)
			if( !edges.contains(subedge))
				return false;

		return true;
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
		for(N currentNode : this.getNodes())
		{
			final List<N> neighbors = new ArrayList<N>(this.getAdjacentNodes(currentNode));
			while( neighbors.remove(currentNode) )
			{
				//do nothing, just remove all instances of the currentNode
			}
			final Set<N> uniqueNeighbors = new HashSet<N>(neighbors);
			if(neighbors.size() > uniqueNeighbors.size())
				return true;
		}
		return false;
	}

	public boolean isIsomorphic(final Graph<N,E> isomorphicGraph)
	{
		return (this.isHomomorphic(isomorphicGraph) && isomorphicGraph.isHomomorphic(this));
	}

	public boolean isHomomorphic(final Graph<N,E> homomorphicGraph)
	{
		final Set<N> uncheckedNodes = new HashSet<N>(this.getNodes());
		final Set<N> homomorphicNodes = homomorphicGraph.getNodes();

		while(!uncheckedNodes.isEmpty())
		{
			final N currentNode = uncheckedNodes.iterator().next();
			uncheckedNodes.remove(currentNode);

			final List<N> neighborNodes = this.getAdjacentNodes(currentNode);
			if(!neighborNodes.isEmpty())
				if(!homomorphicNodes.contains(currentNode))
					return false;
			for(N neighborNode : neighborNodes )
			{
				if(uncheckedNodes.contains(neighborNode))
				{
					if( !homomorphicNodes.contains(neighborNode) )
						return false;
					if( !homomorphicGraph.getAdjacentNodes(currentNode).contains(neighborNode))
						return false;
				}
			}
		}

		return true;
	}

	public boolean isSimple()
	{
		for(N currentNode : this.getNodes())
		{
			final List<N> neighbors = this.getAdjacentNodes(currentNode);
			final Set<N> uniqueNeighbors = new HashSet<N>(neighbors);
			if(neighbors.size() > uniqueNeighbors.size())
				return false;
		}
		return true;
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

	public int getMultiplicity()
	{
		int multiplicity = 0;
		for(E edge : this.getEdges())
		{
			final int edgeMultiplicity = this.getMultiplicity(edge);
			if(edgeMultiplicity > multiplicity)
				multiplicity = edgeMultiplicity;
		}
		return multiplicity;
	}

	public int getMultiplicity(final E edge)
	{
		int multiplicity = 0;
		final List<N> edgeNodes = edge.getNodes();
		final Set<E> potentialMultiples = this.getAdjacentEdges(edge.getNodes().get(0));
		for( E potentialMultiple : potentialMultiples)
		{
			if(potentialMultiple.equals(edge))
				continue;
			final List<N> potentialNodes = new ArrayList<N>(potentialMultiple.getNodes());
			if(potentialNodes.size() != edgeNodes.size())
				continue;
			for(N edgeNode : edgeNodes)
				potentialNodes.remove(edgeNode);
			if(potentialNodes.isEmpty())
				multiplicity++;
		}
		return multiplicity;
	}

	public boolean isMultiple(final E edge)
	{
		final List<N> edgeNodes = edge.getNodes();
		final Set<E> potentialMultiples = this.getAdjacentEdges(edge.getNodes().get(0));
		for( E potentialMultiple : potentialMultiples)
		{
			if(potentialMultiple.equals(edge))
				continue;
			final List<N> potentialNodes = new ArrayList<N>(potentialMultiple.getNodes());
			if(potentialNodes.size() != edgeNodes.size())
				continue;
			for(N edgeNode : edgeNodes)
				potentialNodes.remove(edgeNode);
			if(potentialNodes.isEmpty())
				return true;
		}
		return false;
	}

	public boolean isKnot(final Set<N> knotedNodes)
	{
		// TODO fill this in
		return false;
	}

	public boolean isKnot(final Set<N> knotedNodes, final Set<E> knotedEdges)
	{
		// TODO fill this in
		return false;
	}

	private static class SizeComparator implements Comparator<Collection>, Serializable
	{
		private static final long serialVersionUID = -4454396728238585057L;

		public int compare(final Collection first, final Collection second)
		{
			if(first.size() < second.size())
				return -1;
			else if(first.size() > second.size())
				return 1;
			return 0;
		}

		@Override
		public boolean equals(final Object compareWith)
		{
			if(compareWith == null)
				return false;

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
