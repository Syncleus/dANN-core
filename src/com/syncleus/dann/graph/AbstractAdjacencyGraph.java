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

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import com.syncleus.dann.UnexpectedDannError;
import com.syncleus.dann.graph.cycle.*;
import com.syncleus.dann.math.counting.Counters;
import org.apache.log4j.Logger;

public abstract class AbstractAdjacencyGraph<N, E extends Edge<N>> implements Graph<N, E>
{
	private static final Logger LOGGER = Logger.getLogger(AbstractAdjacencyGraph.class);
	private Set<E> edges;
	private Map<N, Set<E>> adjacentEdges = new HashMap<N, Set<E>>();
	private Map<N, List<N>> adjacentNodes = new HashMap<N, List<N>>();

	protected AbstractAdjacencyGraph()
	{
		this.edges = new HashSet<E>();
	}

	protected AbstractAdjacencyGraph(final Graph<N, E> copyGraph)
	{
		this(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		this.edges = new HashSet<E>(edges);
		for(final N node : nodes)
		{
			this.adjacentNodes.put(node, new ArrayList<N>());
			this.adjacentEdges.put(node, new HashSet<E>());
		}
		for(final E edge : edges)
		{
			final List<N> edgeNodes = edge.getNodes();
			for(int startNodeIndex = 0; startNodeIndex < edgeNodes.size(); startNodeIndex++)
			{
				final N edgeNode = edgeNodes.get(startNodeIndex);
				if (!nodes.contains(edgeNode))
					throw new IllegalArgumentException("A node that is an end point in one of the edges was not in the nodes list");
				this.adjacentEdges.get(edgeNode).add(edge);
				for(int endNodeIndex = 0; endNodeIndex < edgeNodes.size(); endNodeIndex++)
				{
					if (startNodeIndex != endNodeIndex)
						this.adjacentNodes.get(edgeNode).add(edgeNodes.get(endNodeIndex));
				}
			}
		}
	}

	protected Set<E> getInternalEdges()
	{
		return this.edges;
	}

	protected Map<N, Set<E>> getInternalAdjacencyEdges()
	{
		return this.adjacentEdges;
	}

	protected Map<N, List<N>> getInternalAdjacencyNodes()
	{
		return this.adjacentNodes;
	}

	public Set<N> getNodes()
	{
		return Collections.unmodifiableSet(this.adjacentEdges.keySet());
	}

	@Override
	public Set<E> getEdges()
	{
		return Collections.unmodifiableSet(this.edges);
	}

	public Set<E> getAdjacentEdges(final N node)
	{
		if (this.adjacentEdges.containsKey(node))
			return Collections.unmodifiableSet(this.adjacentEdges.get(node));
		else
			return Collections.<E>emptySet();
	}

	public List<N> getAdjacentNodes(final N node)
	{
		return Collections.unmodifiableList(new ArrayList<N>(this.adjacentNodes.get(node)));
	}

	public List<N> getTraversableNodes(final N node)
	{
		final List<N> traversableNodes = new ArrayList<N>();
		for(final E adjacentEdge : this.getAdjacentEdges(node))
			traversableNodes.addAll(adjacentEdge.getTraversableNodes(node));
		return Collections.unmodifiableList(traversableNodes);
	}

	public Set<E> getTraversableEdges(final N node)
	{
		final Set<E> traversableEdges = new HashSet<E>();
		for(final E adjacentEdge : this.getAdjacentEdges(node))
			if (adjacentEdge.isTraversable(node))
				traversableEdges.add(adjacentEdge);
		return Collections.unmodifiableSet(traversableEdges);
	}

	public int getDegree(final N node)
	{
		final Set<E> adjacentNodesEdges = this.getAdjacentEdges(node);
		int degree = 0;
		for(final E adjacentEdge : adjacentNodesEdges)
			for(final N adjacentNode : adjacentEdge.getNodes())
				if (adjacentNode.equals(node))
					degree++;
		return degree;
	}

	public boolean isStronglyConnected()
	{
		final Set<N> nodes = this.getNodes();
		for(final N fromNode : nodes)
			for(final N toNode : nodes)
				if ((toNode != fromNode) && (!this.isStronglyConnected(toNode, fromNode)))
					return false;
		return true;
	}

	public boolean isWeaklyConnected()
	{
		final List<N> remainingNodes = new ArrayList<N>(this.getNodes());
		while (remainingNodes.size() >= 2)
		{
			final N fromNode = remainingNodes.get(0);
			for(final N toNode : remainingNodes)
				if ((toNode != fromNode) && (!this.isWeaklyConnected(toNode, fromNode)))
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
		while (!toVisit.isEmpty())
		{
			final N node = toVisit.iterator().next();
			if (node.equals(rightNode))
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
		while (!toVisit.isEmpty())
		{
			final N node = toVisit.iterator().next();
			if (node.equals(rightNode))
				return true;
			visited.add(node);
			toVisit.remove(node);
			toVisit.addAll(this.getTraversableNodes(node));
			toVisit.removeAll(visited);
		}
		return false;
	}

	public Set<Graph<N, E>> getMaximallyConnectedComponents()
	{
		// TODO fill this in
		return null;
	}

	public boolean isMaximalSubgraph(final Graph<N, E> subgraph)
	{
		if (!this.isSubGraph(subgraph))
			return false;
		//findCycles all edges in the parent graph, but not in the subgraph
		final Set<E> exclusiveParentEdges = new HashSet<E>(this.edges);
		final Set<? extends E> subedges = subgraph.getEdges();
		exclusiveParentEdges.removeAll(subedges);
		//check to make sure none of the edges exclusive to the parent graph
		//connect to any of the nodes in the subgraph.
		final Set<? extends N> subnodes = subgraph.getNodes();
		for(final E exclusiveParentEdge : exclusiveParentEdges)
			for(final N exclusiveParentNode : exclusiveParentEdge.getNodes())
				if (subnodes.contains(exclusiveParentNode))
					return false;
		//passed all the tests, must be maximal
		return true;
	}

	private ImmutableAdjacencyGraph<N, Edge<N>> deleteFromGraph(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		//remove the deleteNodes
		final Set<N> cutNodes = this.getNodes();
		cutNodes.removeAll(deleteNodes);
		//remove the deleteEdges
		final Set<Edge<N>> cutEdges = new HashSet<Edge<N>>(deleteEdges);
		for(final E edge : deleteEdges)
			cutEdges.remove(edge);
		//remove any remaining deleteEdges which connect to removed deleteNodes
		//also replace deleteEdges that have one removed node but still have
		//2 or more remaining deleteNodes with a new edge.
		final Set<Edge<N>> removeEdges = new HashSet<Edge<N>>();
		final Set<Edge<N>> addEdges = new HashSet<Edge<N>>();
		for(final Edge<N> cutEdge : cutEdges)
		{
			final List<N> cutEdgeNeighbors = cutEdge.getNodes();
			cutEdgeNeighbors.removeAll(cutNodes);
			if (cutEdgeNeighbors.size() != cutEdge.getNodes().size())
				removeEdges.add(cutEdge);
			if (cutEdgeNeighbors.size() > 1)
				// TODO instead of ImmutableHyperEdge implement clone or something
				addEdges.add(new ImmutableHyperEdge<N>(cutEdgeNeighbors));
		}
		for(final Edge<N> removeEdge : removeEdges)
			cutEdges.remove(removeEdge);
		cutEdges.addAll(addEdges);
		//check if a graph fromt he new set of deleteEdges and deleteNodes is still
		//connected
		return new ImmutableAdjacencyGraph<N, Edge<N>>(cutNodes, cutEdges);
	}

	public boolean isCut(final Set<N> cutNodes, final Set<E> cutEdges)
	{
		return this.deleteFromGraph(cutNodes, cutEdges).isStronglyConnected();
	}

	public boolean isCut(final Set<N> cutNodes, final Set<E> cutEdges, final N begin, final N end)
	{
		return this.deleteFromGraph(cutNodes, cutEdges).isStronglyConnected(begin, end);
	}

	public boolean isCut(final Set<E> cutEdges)
	{
		return this.isCut(Collections.<N>emptySet(), cutEdges);
	}

	public boolean isCut(final N node)
	{
		return this.isCut(Collections.singleton(node), Collections.<E>emptySet());
	}

	public boolean isCut(final E edge)
	{
		return this.isCut(Collections.<N>emptySet(), Collections.singleton(edge));
	}

	public boolean isCut(final Set<E> cutEdges, final N begin, final N end)
	{
		return this.isCut(Collections.<N>emptySet(), cutEdges, begin, end);
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
		final Set<Set<N>> combinations = Counters.everyCombination(this.getNodes());
		final SortedSet<Set<N>> sortedCombinations = new TreeSet<Set<N>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(final Set<N> cutNodes : sortedCombinations)
			if (this.isCut(cutNodes, Collections.<E>emptySet()))
				return cutNodes.size();
		return this.getNodes().size();
	}

	public int getEdgeConnectivity()
	{
		final Set<Set<E>> combinations = Counters.everyCombination(this.edges);
		final SortedSet<Set<E>> sortedCombinations = new TreeSet<Set<E>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(final Set<E> cutEdges : sortedCombinations)
			if (this.isCut(cutEdges))
				return cutEdges.size();
		return this.edges.size();
	}

	public int getNodeConnectivity(final N begin, final N end)
	{
		final Set<Set<N>> combinations = Counters.everyCombination(this.getNodes());
		final SortedSet<Set<N>> sortedCombinations = new TreeSet<Set<N>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(final Set<N> cutNodes : sortedCombinations)
			if (this.isCut(cutNodes, Collections.<E>emptySet(), begin, end))
				return cutNodes.size();
		return this.getNodes().size();
	}

	public int getEdgeConnectivity(final N begin, final N end)
	{
		final Set<Set<E>> combinations = Counters.everyCombination(this.edges);
		final SortedSet<Set<E>> sortedCombinations = new TreeSet<Set<E>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(final Set<E> cutEdges : sortedCombinations)
			if (this.isCut(cutEdges, begin, end))
				return cutEdges.size();
		return this.edges.size();
	}

	public boolean isComplete()
	{
		if (!this.isSimple())
			return false;
		for(final N startNode : this.getNodes())
			for(final N endNode : this.getNodes())
				if (!startNode.equals(endNode))
					if (!this.getAdjacentNodes(startNode).contains(endNode))
						return false;
		return true;
	}

	public int getOrder()
	{
		return this.getNodes().size();
	}

	public int getCycleCount()
	{
		final CycleFinder<N, E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N, E>();
		return finder.cycleCount(this);
	}

	public boolean isPancyclic()
	{
		final CycleFinder<N, E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N, E>();
		return finder.isPancyclic(this);
	}

	public boolean isUnicyclic()
	{
		final CycleFinder<N, E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N, E>();
		return finder.isUnicyclic(this);
	}

	public boolean isAcyclic()
	{
		final CycleFinder finder = new ExhaustiveDepthFirstSearchCycleFinder();
		return !finder.hasCycle(this);
	}

	public int getGirth()
	{
		final CycleFinder<N, E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N, E>();
		return finder.girth(this);
	}

	public int getCircumference()
	{
		final CycleFinder<N, E> finder = new ExhaustiveDepthFirstSearchCycleFinder<N, E>();
		return finder.circumference(this);
	}

	public boolean isSpanningTree(final Graph<N, E> graph)
	{
		return ((this.getNodes().containsAll(graph.getNodes())) &&
				(graph.isWeaklyConnected()) &&
				(graph.isAcyclic()));
	}

	public boolean isTree()
	{
		return ((this.isWeaklyConnected()) && (this.isAcyclic()) && (this.isSimple()));
	}

	public boolean isForest()
	{
		// TODO fill this in
		return false;
	}

	public boolean isSubGraph(final Graph<N, E> subgraph)
	{
		final Set<N> nodes = this.getNodes();
		final Set<N> subnodes = subgraph.getNodes();
		for(final N subnode : subnodes)
			if (!nodes.contains(subnode))
				return false;
		final Set<E> subedges = subgraph.getEdges();
		for(final E subedge : subedges)
			if (!this.edges.contains(subedge))
				return false;
		return true;
	}

	public int getMinimumDegree()
	{
		if (this.getNodes().isEmpty())
			throw new IllegalStateException("This graph has no nodes!");
		int minimumDegree = Integer.MAX_VALUE;
		for(final N node : this.getNodes())
			if (this.getDegree(node) < minimumDegree)
				minimumDegree = this.getDegree(node);
		return minimumDegree;
	}

	public boolean isMultigraph()
	{
		for(final N currentNode : this.getNodes())
		{
			final List<N> neighbors = new ArrayList<N>(this.getAdjacentNodes(currentNode));
			while (neighbors.remove(currentNode))
			{
				//do nothing, just remove all instances of the currentNode
			}
			final Set<N> uniqueNeighbors = new HashSet<N>(neighbors);
			if (neighbors.size() > uniqueNeighbors.size())
				return true;
		}
		return false;
	}

	public boolean isIsomorphic(final Graph<N, E> isomorphicGraph)
	{
		return (this.isHomomorphic(isomorphicGraph) && isomorphicGraph.isHomomorphic(this));
	}

	public boolean isHomomorphic(final Graph<N, E> homomorphicGraph)
	{
		final Set<N> uncheckedNodes = new HashSet<N>(this.getNodes());
		final Set<N> homomorphicNodes = homomorphicGraph.getNodes();
		while (!uncheckedNodes.isEmpty())
		{
			final N currentNode = uncheckedNodes.iterator().next();
			uncheckedNodes.remove(currentNode);
			final List<N> neighborNodes = this.getAdjacentNodes(currentNode);
			if (!neighborNodes.isEmpty())
				if (!homomorphicNodes.contains(currentNode))
					return false;
			for(final N neighborNode : neighborNodes)
			{
				if (uncheckedNodes.contains(neighborNode))
				{
					if (!homomorphicNodes.contains(neighborNode))
						return false;
					if (!homomorphicGraph.getAdjacentNodes(currentNode).contains(neighborNode))
						return false;
				}
			}
		}
		return true;
	}

	public boolean isSimple()
	{
		for(final N currentNode : this.getNodes())
		{
			final List<N> neighbors = this.getAdjacentNodes(currentNode);
			final Set<N> uniqueNeighbors = new HashSet<N>(neighbors);
			if (neighbors.size() > uniqueNeighbors.size())
				return false;
		}
		return true;
	}

	public boolean isRegular()
	{
		int degree = -1;
		for(final N node : this.getNodes())
		{
			if (degree == -1)
				degree = this.getDegree(node);
			else if (degree != this.getDegree(node))
				return false;
		}
		return true;
	}

	public int getRegularDegree()
	{
		int degree = -1;
		for(final N node : this.getNodes())
		{
			if (degree == -1)
				degree = this.getDegree(node);
			else if (degree != this.getDegree(node))
				return -1;
		}
		if (degree == -1)
			throw new IllegalStateException("This graph has no nodes!");
		return degree;
	}

	public int getMultiplicity()
	{
		int multiplicity = 0;
		for(final E edge : this.edges)
		{
			final int edgeMultiplicity = this.getMultiplicity(edge);
			if (edgeMultiplicity > multiplicity)
				multiplicity = edgeMultiplicity;
		}
		return multiplicity;
	}

	public int getMultiplicity(final E edge)
	{
		int multiplicity = 0;
		final List<N> edgeNodes = edge.getNodes();
		final Set<E> potentialMultiples = this.getAdjacentEdges(edge.getNodes().get(0));
		for(final E potentialMultiple : potentialMultiples)
		{
			if (potentialMultiple.equals(edge))
				continue;
			final List<N> potentialNodes = new ArrayList<N>(potentialMultiple.getNodes());
			if (potentialNodes.size() != edgeNodes.size())
				continue;
			for(final N edgeNode : edgeNodes)
				potentialNodes.remove(edgeNode);
			if (potentialNodes.isEmpty())
				multiplicity++;
		}
		return multiplicity;
	}

	public boolean isMultiple(final E edge)
	{
		final List<N> edgeNodes = edge.getNodes();
		final Set<E> potentialMultiples = this.getAdjacentEdges(edge.getNodes().get(0));
		for(final E potentialMultiple : potentialMultiples)
		{
			if (potentialMultiple.equals(edge))
				continue;
			final List<N> potentialNodes = new ArrayList<N>(potentialMultiple.getNodes());
			if (potentialNodes.size() != edgeNodes.size())
				continue;
			for(final N edgeNode : edgeNodes)
				potentialNodes.remove(edgeNode);
			if (potentialNodes.isEmpty())
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

	public AbstractAdjacencyGraph<N, E> cloneAdd(final E newEdge)
	{
		if (newEdge == null)
			throw new IllegalArgumentException("newEdge can not be null");
		if (!this.getNodes().containsAll(newEdge.getNodes()))
			throw new IllegalArgumentException("newEdge has a node as an end point that is not part of the graph");
		final Set<E> newEdges = new HashSet<E>(this.edges);
		if (newEdges.add(newEdge))
		{
			final Map<N, Set<E>> newAdjacentEdges = new HashMap<N, Set<E>>();
			for(final Entry<N, Set<E>> neighborEdgeEntry : this.adjacentEdges.entrySet())
				newAdjacentEdges.put(neighborEdgeEntry.getKey(), new HashSet<E>(neighborEdgeEntry.getValue()));
			final Map<N, List<N>> newAdjacentNodes = new HashMap<N, List<N>>();
			for(final Entry<N, List<N>> neighborNodeEntry : this.adjacentNodes.entrySet())
				newAdjacentNodes.put(neighborNodeEntry.getKey(), new ArrayList<N>(neighborNodeEntry.getValue()));
			for(final N currentNode : newEdge.getNodes())
			{
				newAdjacentEdges.get(currentNode).add(newEdge);
				final List<N> currentAdjacentNodes = new ArrayList<N>(newEdge.getNodes());
				currentAdjacentNodes.remove(currentNode);
				for(final N currentAdjacentNode : currentAdjacentNodes)
					newAdjacentNodes.get(currentNode).add(currentAdjacentNode);
			}
			final AbstractAdjacencyGraph<N, E> copy = (AbstractAdjacencyGraph<N, E>) this.clone();
			copy.edges = newEdges;
			copy.adjacentEdges = newAdjacentEdges;
			copy.adjacentNodes = newAdjacentNodes;
			return copy;
		}
		return null;
	}

	public AbstractAdjacencyGraph<N, E> cloneAdd(final N newNode)
	{
		// TODO fill this in
		return null;
	}

	public AbstractAdjacencyGraph<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		// TODO fill this in
		return null;
	}

	public AbstractAdjacencyGraph<N, E> cloneRemove(final E edgeToRemove)
	{
		// TODO fill this in
		return null;
	}

	public AbstractAdjacencyGraph<N, E> cloneRemove(final N nodeToRemove)
	{
		// TODO fill this in
		return null;
	}

	public AbstractAdjacencyGraph<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		// TODO fill this in
		return null;
	}

	@Override
	public AbstractAdjacencyGraph<N, E> clone()
	{
		try
		{
			return (AbstractAdjacencyGraph<N, E>) super.clone();
		}
		catch (CloneNotSupportedException caught)
		{
			LOGGER.error("Unexpectidly could not clone Graph.", caught);
			throw new UnexpectedDannError("Unexpectidly could not clone graph", caught);
		}
	}

	private static class SizeComparator implements Comparator<Collection>, Serializable
	{
		private static final long serialVersionUID = -4454396728238585057L;

		public int compare(final Collection first, final Collection second)
		{
			if (first.size() < second.size())
				return -1;
			else if (first.size() > second.size())
				return 1;
			return 0;
		}

		@Override
		public boolean equals(final Object compareWith)
		{
			if (compareWith == null)
				return false;
			
			return (compareWith instanceof SizeComparator);
		}

		@Override
		public int hashCode()
		{
			return super.hashCode();
		}
	}
}
