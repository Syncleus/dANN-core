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

import java.io.Serializable;
import java.util.*;
import com.syncleus.dann.graph.*;
import com.syncleus.dann.math.counting.Counters;

public final class Topography
{
	//this is a utility class so we cant instantiate it, make default constructor private
	private Topography()
	{
		throw new IllegalStateException("This is a utility class, it cant be instantiated");
	}

	/**
	 * Gets the degree of a given node. The degree of a node is how many adjacent edges link to
	 * this node, as opposed to how many nodes this node links to. A typical adjacent edge will
	 * connect once to this edge and to one or more other nodes, counting once towards the
	 * degree. However, loop edges can connect twice or more to this node counting multiple
	 * times.
	 * @param node The node whose degree is to be returned
	 * @return The degree of this node
	 * @see com.syncleus.dann.graph.topological.Topography#getDegree(Graph,Object)
	 */
	public static <N, E extends Edge<N>> int getDegree(final Graph<N, E> graph, final N node)
	{
		if( graph instanceof ConnectionismOptimizedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedGraph)graph).getDegree(node);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		if(graph instanceof BidirectedGraph)
		{
			final Set<BidirectedEdge<N>> adjacentEdges = ((BidirectedGraph<N, BidirectedEdge<N>>)graph).getAdjacentEdges(node);
			int degree = 0;
			for(final BidirectedEdge<?> adjacentEdge : adjacentEdges)
			{
				if( adjacentEdge.isLoop() )
					degree += 2;
				else
					degree++;
			}
			return degree;
		}
		else
		{
			final Set<E> adjacentNodesEdges = graph.getAdjacentEdges(node);
			int degree = 0;
			for(final E adjacentEdge : adjacentNodesEdges)
				for(final N adjacentNode : adjacentEdge.getNodes())
					if( adjacentNode.equals(node) )
						degree++;
			return degree;
		}
	}

	/**
	 * Determines whether this graph is strongly connected. A graph is strongly connected if and only if
	 * every node is strongly connected to every other node.
	 * @return If this graph is strongly connected
	 * @see com.syncleus.dann.graph.topological.Topography#isStronglyConnected(Graph)
	 */
	public static <N, E extends Edge<N>> boolean isStronglyConnected(final Graph<N, E> graph)
	{
		if( graph instanceof StrongConnectivityOptimizedGraph )
		{
			try
			{
				return ((StrongConnectivityOptimizedGraph)graph).isStronglyConnected();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		final Set<N> nodes = graph.getNodes();
		for(final N fromNode : nodes)
			for(final N toNode : nodes)
				if( (toNode != fromNode) && (!Topography.isStronglyConnected(graph, toNode, fromNode)) )
					return false;
		return true;
	}

	/**
	 * Determines whether this graph is weakly connected. A graph is weakly connected if
	 * there is only one node or every node is weakly connected to every other node.
	 * @return If this graph is weakly connected
	 * @see com.syncleus.dann.graph.topological.Topography#isWeaklyConnected(Graph)
	 */
	public static <N, E extends Edge<N>> boolean isWeaklyConnected(final Graph<N, E> graph)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isWeaklyConnected();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		final List<N> remainingNodes = new ArrayList<N>(graph.getNodes());
		while( remainingNodes.size() >= 2 )
		{
			final N fromNode = remainingNodes.get(0);
			remainingNodes.remove(0);
			for(final N toNode : remainingNodes)
				if( (toNode != fromNode) && (!Topography.isWeaklyConnected(graph, toNode, fromNode)) )
					return false;
		}
		return true;
	}

	/**
	 * Determines whether two nodes are weakly connected. Nodes are weakly connected if
	 * there is some path from the left node to the right node, across any adjacent nodes.
	 * Assume that the hypothetical <pre>A->B</pre> graph is directional. This graph
	 * would have a strong connection from A to B, but only a weak connection from B to A.
	 * This graph would, therefore, be weakly connected overall, but not strongly connected overall.
	 * @param leftNode The left node
	 * @param rightNode The right node
	 * @return Whether there is any connection between the two nodes
	 * @see com.syncleus.dann.graph.topological.Topography#isWeaklyConnected(Graph, Object, Object)
	 */
	public static <N, E extends Edge<N>> boolean isWeaklyConnected(final Graph<N, E> graph, final N leftNode, final N rightNode)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isWeaklyConnected(leftNode, rightNode);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		final Set<N> visited = new HashSet<N>();
		visited.add(leftNode);

		final Set<N> toVisit = new HashSet<N>();
		toVisit.addAll(graph.getAdjacentNodes(leftNode));

		while( !toVisit.isEmpty() )
		{
			final N node = toVisit.iterator().next();
			if( node.equals(rightNode) )
				return true;

			visited.add(node);
			toVisit.remove(node);

			toVisit.addAll(graph.getAdjacentNodes(node));
			toVisit.removeAll(visited);
		}

		return false;
	}

	/**
	 * Determines if two nodes are strongly connected. Nodes are strongly connected
	 * if there is some path from the left node to the right node, across any traversable
	 * nodes. See <code>isWeaklyConnected(N,N)</code> for an example of weak versus strong
	 * connections.
	 * @param leftNode The first node to check
	 * @param rightNode The second node to check
	 * @return Whether these nodes are strongly connected
	 * @see com.syncleus.dann.graph.topological.Topography#isWeaklyConnected(Graph, Object, Object)
	 * @see com.syncleus.dann.graph.topological.Topography#isStronglyConnected(Graph, Object, Object)
	 */
	public static <N, E extends Edge<N>> boolean isStronglyConnected(final Graph<N, E> graph, final N leftNode, final N rightNode)
	{
		if( graph instanceof StrongConnectivityOptimizedGraph )
		{
			try
			{
				return ((StrongConnectivityOptimizedGraph)graph).isStronglyConnected(leftNode, rightNode);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		final Set<N> visited = new HashSet<N>();
		visited.add(leftNode);
		final Set<N> toVisit = new HashSet<N>();
		toVisit.addAll(graph.getTraversableNodes(leftNode));
		while( !toVisit.isEmpty() )
		{
			final N node = toVisit.iterator().next();
			if( node.equals(rightNode) )
				return true;
			visited.add(node);
			toVisit.remove(node);
			toVisit.addAll(graph.getTraversableNodes(node));
			toVisit.removeAll(visited);
		}
		return false;
	}

	/**
	 * Gets the set of maximally-connected components from a graph. The maximally-connected
	 * components are those with the most connections to other nodes.
	 * NOTE: This method currently returns null.
	 * @return <code>null</code>
	 * @see com.syncleus.dann.graph.topological.Topography#getMaximallyConnectedComponents(Graph)
	 */
	public static <N, E extends Edge<N>> Set<Graph<N, E>> getMaximallyConnectedComponents(final Graph<N, E> graph)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).getMaximallyConnectedComponents();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		// TODO fill this in
		throw new UnsupportedOperationException();
	}

	/**
	 * Determines whether the given graph is a maximal subgraph of the current graph.
	 * A subgraph is maximal if none of the edges of the parent graph not in the subgraph
	 * connect to any nodes in the subgraph.
	 * @param subGraph A subgraph of this graph to be checked if maximally
	 * connected.
	 * @return Whether this is a maximal subgraph
	 * @see com.syncleus.dann.graph.topological.Topography#isMaximalSubgraph(Graph, Graph)
	 */
	public static <N, E extends Edge<N>> boolean isMaximalSubgraph(final Graph<N, E> graph, final Graph<N, E> subGraph)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isMaximalSubgraph(subGraph);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		if( !Topography.isSubGraph(graph, subGraph) )
			return false;
		//findCycles all edges in the parent graph, but not in the subgraph
		final Set<E> exclusiveParentEdges = new HashSet<E>(graph.getEdges());
		final Set<? extends E> subedges = subGraph.getEdges();
		exclusiveParentEdges.removeAll(subedges);
		//check to make sure none of the edges exclusive to the parent graph
		//connect to any of the nodes in the subgraph.
		final Set<? extends N> subnodes = subGraph.getNodes();
		for(final E exclusiveParentEdge : exclusiveParentEdges)
			for(final N exclusiveParentNode : exclusiveParentEdge.getNodes())
				if( subnodes.contains(exclusiveParentNode) )
					return false;
		//passed all the tests, must be maximal
		return true;
	}


	public static <N, E extends Edge<N>> boolean isCut(final Graph<N, E> graph, final Set<N> cutNodes, final Set<E> cutEdges)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isCut(cutNodes, cutEdges);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return Topography.isStronglyConnected(Topography.deleteFromGraph(graph, cutNodes, cutEdges));
	}

	public static <N, E extends Edge<N>> boolean isCut(final Graph<N, E> graph, final Set<N> cutNodes, final Set<E> cutEdges, final N begin, final N end)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isCut(cutNodes, cutEdges, begin, end);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return Topography.isStronglyConnected(Topography.deleteFromGraph(graph, cutNodes, cutEdges), begin, end);
	}

	public static <N, E extends Edge<N>> boolean isCut(final Graph<N, E> graph, final Set<E> cutEdges)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isCut(cutEdges);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return Topography.isCut(graph, Collections.<N>emptySet(), cutEdges);
	}

	public static <N, E extends Edge<N>> boolean isCut(final Graph<N, E> graph, final N node)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isCut(node);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return Topography.isCut(graph, Collections.singleton(node), Collections.<E>emptySet());
	}

	public static <N, E extends Edge<N>> boolean isCut(final Graph<N, E> graph, final E edge)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isCut(edge);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return Topography.isCut(graph, Collections.<N>emptySet(), Collections.singleton(edge));
	}

	public static <N, E extends Edge<N>> boolean isCut(final Graph<N, E> graph, final Set<E> cutEdges, final N begin, final N end)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isCut(cutEdges, begin, end);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return Topography.isCut(graph, Collections.<N>emptySet(), cutEdges, begin, end);
	}

	public static <N, E extends Edge<N>> boolean isCut(final Graph<N, E> graph, final N node, final N begin, final N end)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isCut(node, begin, end);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return Topography.isCut(graph, Collections.singleton(node), Collections.<E>emptySet(), begin, end);
	}

	public static <N, E extends Edge<N>> boolean isCut(final Graph<N, E> graph, final E edge, final N begin, final N end)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isCut(edge, begin, end);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return Topography.isCut(graph,Collections.<N>emptySet(), Collections.singleton(edge), begin, end);
	}

	public static <N, E extends Edge<N>> int getNodeConnectivity(final Graph<N, E> graph)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).getNodeConnectivity();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		final Set<Set<N>> combinations = Counters.everyCombination(graph.getNodes());
		final SortedSet<Set<N>> sortedCombinations = new TreeSet<Set<N>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(final Set<N> cutNodes : sortedCombinations)
			if( Topography.isCut(graph, cutNodes, Collections.<E>emptySet()) )
				return cutNodes.size();
		return graph.getNodes().size();
	}

	public static <N, E extends Edge<N>> int getEdgeConnectivity(final Graph<N, E> graph)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).getEdgeConnectivity();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		final Set<Set<E>> combinations = Counters.everyCombination(graph.getEdges());
		final SortedSet<Set<E>> sortedCombinations = new TreeSet<Set<E>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(final Set<E> cutEdges : sortedCombinations)
			if( Topography.isCut(graph, cutEdges) )
				return cutEdges.size();
		return graph.getEdges().size();
	}

	public static <N, E extends Edge<N>> int getNodeConnectivity(final Graph<N, E> graph, final N begin, final N end)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).getNodeConnectivity(begin, end);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		final Set<Set<N>> combinations = Counters.everyCombination(graph.getNodes());
		final SortedSet<Set<N>> sortedCombinations = new TreeSet<Set<N>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(final Set<N> cutNodes : sortedCombinations)
			if( Topography.isCut(graph, cutNodes, Collections.<E>emptySet(), begin, end) )
				return cutNodes.size();
		return graph.getNodes().size();
	}

	public static <N, E extends Edge<N>> int getEdgeConnectivity(final Graph<N, E> graph, final N begin, final N end)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).getEdgeConnectivity(begin, end);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		final Set<Set<E>> combinations = Counters.everyCombination(graph.getEdges());
		final SortedSet<Set<E>> sortedCombinations = new TreeSet<Set<E>>(new SizeComparator());
		sortedCombinations.addAll(combinations);
		for(final Set<E> cutEdges : sortedCombinations)
			if( Topography.isCut(graph, cutEdges, begin, end) )
				return cutEdges.size();
		return graph.getEdges().size();
	}

	public static <N, E extends Edge<N>> boolean isComplete(final Graph<N, E> graph)
	{
		if( graph instanceof WeakConnectivityOptimizedGraph )
		{
			try
			{
				return ((WeakConnectivityOptimizedGraph)graph).isComplete();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		if( !Topography.isSimple(graph) )
			return false;
		for(final N startNode : graph.getNodes())
			for(final N endNode : graph.getNodes())
				if( !startNode.equals(endNode) )
					if( !graph.getAdjacentNodes(startNode).contains(endNode) )
						return false;
		return true;
	}

	public static <N, E extends Edge<N>> int getOrder(final Graph<N, E> graph)
	{
		if( graph instanceof ConnectionismOptimizedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedGraph)graph).getOrder();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return graph.getNodes().size();
	}

	public static <N, E extends Edge<N>> boolean isSubGraph(final Graph<N, E> graph, final Graph<N, E> subgraph)
	{
		if( graph instanceof StructureOptimizedGraph )
		{
			try
			{
				return ((StructureOptimizedGraph)graph).isSubGraph(subgraph);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		final Set<N> nodes = graph.getNodes();
		final Set<N> subnodes = subgraph.getNodes();
		for(final N subnode : subnodes)
			if( !nodes.contains(subnode) )
				return false;
		final Set<E> subedges = subgraph.getEdges();
		for(final E subedge : subedges)
			if( !graph.getEdges().contains(subedge) )
				return false;
		return true;
	}

	public static <N, E extends Edge<N>> int getMinimumDegree(final Graph<N, E> graph)
	{
		if( graph instanceof ConnectionismOptimizedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedGraph)graph).getMinimumDegree();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		if( graph.getNodes().isEmpty() )
			throw new IllegalStateException("This graph has no nodes!");
		int minimumDegree = Integer.MAX_VALUE;
		for(final N node : graph.getNodes())
			if( Topography.getDegree(graph, node) < minimumDegree )
				minimumDegree = Topography.getDegree(graph, node);
		return minimumDegree;
	}

	public static <N, E extends Edge<N>> boolean isMultigraph(final Graph<N, E> graph)
	{
		if( graph instanceof ConnectionismOptimizedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedGraph)graph).isMultigraph();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		for(final N currentNode : graph.getNodes())
		{
			final List<N> neighbors = new ArrayList<N>(graph.getAdjacentNodes(currentNode));
			while( neighbors.remove(currentNode) )
			{
				//do nothing, just remove all instances of the currentNode
			}
			final Set<N> uniqueNeighbors = new HashSet<N>(neighbors);
			if( neighbors.size() > uniqueNeighbors.size() )
				return true;
		}
		return false;
	}

	public static <N, E extends Edge<N>> boolean isIsomorphic(final Graph<N, E> graph, final Graph<N, E> isomorphicGraph)
	{
		if( graph instanceof StructureOptimizedGraph )
		{
			try
			{
				return ((StructureOptimizedGraph)graph).isIsomorphic(isomorphicGraph);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}
		
		return (Topography.isHomomorphic(graph, isomorphicGraph) && Topography.isHomomorphic(isomorphicGraph, graph));
	}

	public static <N, E extends Edge<N>> boolean isHomomorphic(final Graph<N, E> graph, final Graph<N, E> homomorphicGraph)
	{
		if( graph instanceof StructureOptimizedGraph )
		{
			try
			{
				return ((StructureOptimizedGraph)graph).isHomomorphic(homomorphicGraph);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		final Set<N> uncheckedNodes = new HashSet<N>(graph.getNodes());
		final Set<N> homomorphicNodes = homomorphicGraph.getNodes();

		while( !uncheckedNodes.isEmpty() )
		{
			final N currentNode = uncheckedNodes.iterator().next();
			uncheckedNodes.remove(currentNode);

			final List<N> neighborNodes = graph.getAdjacentNodes(currentNode);
			if( !neighborNodes.isEmpty() )
				if( !homomorphicNodes.contains(currentNode) )
					return false;
			for(final N neighborNode : neighborNodes)
			{
				if( uncheckedNodes.contains(neighborNode) )
				{
					if( !homomorphicNodes.contains(neighborNode) )
						return false;
					if( !homomorphicGraph.getAdjacentNodes(currentNode).contains(neighborNode) )
						return false;
				}
			}
		}

		return true;
	}

	/**
	 * Determines if graph has no loops, and all edges have a multiplicity of 0.
	 * Simple graphs can not have two nodes connected by two edges differing only
	 * by its direction/navigability.
	 *
	 * @return true if graph has no loops, and all edges have a multiplicity of 0.
	 * @since 2.0
	 * @see com.syncleus.dann.graph.topological.Topography#isSimple(Graph)
	 */
	public static <N, E extends Edge<N>> boolean isSimple(final Graph<N, E> graph)
	{
		if( graph instanceof ConnectionismOptimizedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedGraph)graph).isSimple();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		for(final N currentNode : graph.getNodes())
		{
			final List<N> neighbors = graph.getAdjacentNodes(currentNode);
			final Set<N> uniqueNeighbors = new HashSet<N>(neighbors);
			if( neighbors.size() > uniqueNeighbors.size() )
				return false;
		}
		return true;
	}

	/**
	 * Determines if every node in this graph has the same degree. If there are no
	 * nodes in the graph this will return true.
	 *
	 * @return true if every node in this graph has the same degree or there are no
	 *         nodes, false otherwise.
	 * @since 2.0
	 * @see com.syncleus.dann.graph.topological.Topography#isRegular(Graph)
	 */
	public static <N, E extends Edge<N>> boolean isRegular(final Graph<N, E> graph)
	{
		if( graph instanceof ConnectionismOptimizedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedGraph)graph).isRegular();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}
		
		int degree = -1;
		for(final N node : graph.getNodes())
		{
			if( degree == -1 )
				degree = Topography.getDegree(graph, node);
			else if( degree != Topography.getDegree(graph, node) )
				return false;
		}
		return true;
	}

	/**
	 * Gets the degree of the regular graph.
	 * @throws IllegalStateException If this graph has no nodes, or the graph is not regular
	 * @return The degree of the regular graph
	 * @see com.syncleus.dann.graph.topological.Topography#getRegularDegree(Graph)
	 */
	public static <N, E extends Edge<N>> int getRegularDegree(final Graph<N, E> graph)
	{
		if( graph instanceof ConnectionismOptimizedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedGraph)graph).getRegularDegree();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		int degree = -1;
		for(final N node : graph.getNodes())
		{
			if( degree == -1 )
				degree = Topography.getDegree(graph, node);
			else if( degree != Topography.getDegree(graph, node) )
				return -1;
		}

		if( degree == -1 )
			throw new IllegalStateException("This graph has no nodes!");

		return degree;
	}

	/**
	 * Determined the largest multiplicty of any node in the graph and return it.
	 * Returns 0 if there are no edges.
	 *
	 * @return the largest multiplicty of any node in the graph and return it.
	 * @since 2.0
	 * @see com.syncleus.dann.graph.topological.Topography#getMultiplicity(Graph)
	 */
	public static <N, E extends Edge<N>> int getMultiplicity(final Graph<N, E> graph)
	{
		if( graph instanceof ConnectionismOptimizedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedGraph)graph).getMultiplicity();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		int multiplicity = 0;
		for(final E edge : graph.getEdges())
		{
			final int edgeMultiplicity = Topography.getMultiplicity(graph, edge);
			if( edgeMultiplicity > multiplicity )
				multiplicity = edgeMultiplicity;
		}
		return multiplicity;
	}

	/**
	 * Calculates the number of edges in the graph with the exact set of end nodes
	 * as the specified edge, not including the specified edge itself.
	 *
	 * @param edge the edge of which the multiplicity is to be calculated.
	 * @return the number of edges in the graph with the exact set of end nodes as
	 *         the specified edge, not including the specified edge itself.
	 * @throws IllegalArgumentException if the specified edge is not present in the
	 * graph.
	 * @since 2.0
	 * @see com.syncleus.dann.graph.topological.Topography#getMultiplicity(Graph, com.syncleus.dann.graph.Edge)
	 */
	public static <N, E extends Edge<N>> int getMultiplicity(final Graph<N, E> graph, final E edge)
	{
		if( graph instanceof ConnectionismOptimizedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedGraph)graph).getMultiplicity(edge);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		int multiplicity = 0;
		final List<N> edgeNodes = edge.getNodes();
		final Set<E> potentialMultiples = graph.getAdjacentEdges(edge.getNodes().get(0));
		for(final E potentialMultiple : potentialMultiples)
		{
			if( potentialMultiple.equals(edge) )
				continue;
			final List<N> potentialNodes = new ArrayList<N>(potentialMultiple.getNodes());
			if( potentialNodes.size() != edgeNodes.size() )
				continue;
			for(final N edgeNode : edgeNodes)
				potentialNodes.remove(edgeNode);
			if( potentialNodes.isEmpty() )
				multiplicity++;
		}
		return multiplicity;
	}

	/**
	 * Determines if the edge is the only edge with its particular set of end point
	 * nodes, false if unique, true if not. If there is another edge in the graph
	 * with the exact same set of nodes, no more and no less, then returns true,
	 * otherwise false.
	 *
	 * @param edge the edge to check if it is multiple.
	 * @return true if there is another edge in the graph with the exact same set
	 *         of nodes.
	 * @throws IllegalArgumentException if the specified edge is not present in the
	 * graph.
	 * @since 2.0
	 */
	public static <N, E extends Edge<N>> boolean isMultiple(final Graph<N, E> graph, final E edge)
	{
		if( graph instanceof ConnectionismOptimizedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedGraph)graph).isMultiple(edge);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}
		
		final List<N> edgeNodes = edge.getNodes();
		final Set<E> potentialMultiples = graph.getAdjacentEdges(edge.getNodes().get(0));
		for(final E potentialMultiple : potentialMultiples)
		{
			if( potentialMultiple.equals(edge) )
				continue;
			final List<N> potentialNodes = new ArrayList<N>(potentialMultiple.getNodes());
			if( potentialNodes.size() != edgeNodes.size() )
				continue;
			for(final N edgeNode : edgeNodes)
				potentialNodes.remove(edgeNode);
			if( potentialNodes.isEmpty() )
				return true;
		}
		return false;
	}

	/**
	 * Determines if the specified nodes can be traversed to from outside of the
	 * set, and once the set is entered there is no path to traverse outside of the
	 * set. if the specified nodes each have at least one traversable edge, all
	 * traversable edges go to other nodes in the knot, no traversable edges
	 * connect to a node outside of the knot, and there is at least one traversable
	 * edge from a node outside of the knot to a node in the knot. It is important
	 * to note that while the knot is not a maximally connected component of the
	 * graph it is weakly connected amongst itself.
	 *
	 * NOTE: This method always returns false.
	 *
	 * @param knotedNodes A set of nodes to check if they form a knot.
	 * @return false
	 * @since 2.0
	 */
	public static <N, E extends Edge<N>> boolean isKnot(final Graph<N, E> graph, final Set<N> knotedNodes)
	{
		if( graph instanceof KnotOptimizedGraph )
		{
			try
			{
				return ((KnotOptimizedGraph)graph).isKnot(knotedNodes);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		// TODO fill this in
		throw new UnsupportedOperationException();
	}

	public static <N, E extends Edge<N>> boolean isKnot(final Graph<N, E> graph, final Set<N> knotedNodes, final Set<E> knotedEdges)
	{
		if( graph instanceof KnotOptimizedGraph )
		{
			try
			{
				return ((KnotOptimizedGraph)graph).isKnot(knotedNodes, knotedEdges);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		// TODO fill this in
		throw new UnsupportedOperationException();
	}


	public static <N, E extends BidirectedEdge<N>> int getIndegree(final BidirectedGraph<N, E> graph, final N node)
	{
		if( graph instanceof ConnectionismOptimizedBidirectedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedBidirectedGraph)graph).getIndegree(node);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return graph.getInEdges(node).size();
	}

	public static <N, E extends BidirectedEdge<N>> int getOutdegree(final BidirectedGraph<N, E> graph, final N node)
	{
		if( graph instanceof ConnectionismOptimizedBidirectedGraph )
		{
			try
			{
				return ((ConnectionismOptimizedBidirectedGraph)graph).getOutdegree(node);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return graph.getTraversableEdges(node).size();
	}

	public static <N, E extends HyperEdge<N>> int getRank(final HyperGraph<N, E> graph)
	{
		if( graph instanceof ConnectionismOptimizedHyperGraph )
		{
			try
			{
				return ((ConnectionismOptimizedHyperGraph)graph).getRank();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		//TODO implement this
		throw new UnsupportedOperationException();
	}

	public static <N, E extends HyperEdge<N>> BidirectedGraph<N, BidirectedEdge<N>> getPrimal(final HyperGraph<N, E> graph)
	{
		if( graph instanceof StructureOptimizedHyperGraph )
		{
			try
			{
				return ((StructureOptimizedHyperGraph)graph).getPrimal();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		//TODO implement this
		throw new UnsupportedOperationException();
	}

	public static <N, E extends HyperEdge<N>> boolean isPartial(final HyperGraph<N, E> graph, final HyperGraph<N, E> partialGraph)
	{
		if( graph instanceof StructureOptimizedHyperGraph )
		{
			try
			{
				return ((StructureOptimizedHyperGraph)graph).isPartial(partialGraph);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		//TODO implement this
		throw new UnsupportedOperationException();
	}

	public static <N, E extends HyperEdge<N>> boolean isHost(final HyperGraph<N, E> graph, final HyperGraph<N, E> hostGraph)
	{
		if( graph instanceof StructureOptimizedHyperGraph )
		{
			try
			{
				return ((StructureOptimizedHyperGraph)graph).isHost(hostGraph);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		//TODO implement this
		throw new UnsupportedOperationException();
	}

	public static <N, E extends HyperEdge<N>> boolean isUniform(final HyperGraph<N, E> graph)
	{
		if( graph instanceof StructureOptimizedHyperGraph )
		{
			try
			{
				return ((StructureOptimizedHyperGraph)graph).isUniform();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		//TODO implement this
		throw new UnsupportedOperationException();
	}

	/**
	 * Removes a set of nodes and a set of edges from the given graph. First, it removes all nodes from the graph.
	 * Then, it removes all edges in the edges to remove that are not connected to the given node. It then
	 * returns an ImmutableAdjacencyGraph with the given nodes and edges removed.
	 * @param deleteNodes The nodes to remove
	 * @param deleteEdges The edges to remove in addition to the nodes
	 * @return A graph of the remaining nodes
	 */
	private static <N, E extends Edge<N>> ImmutableAdjacencyGraph<N, Edge<N>> deleteFromGraph(final Graph<N, E> graph, final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		//remove the deleteNodes
		final Set<N> cutNodes = graph.getNodes();
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
			if( cutEdgeNeighbors.size() != cutEdge.getNodes().size() )
				removeEdges.add(cutEdge);
			if( cutEdgeNeighbors.size() > 1 )
				// TODO instead of ImmutableHyperEdge implement clone or something
				addEdges.add(new ImmutableHyperEdge<N>(cutEdgeNeighbors));
		}
		for(final Edge<N> removeEdge : removeEdges)
			cutEdges.remove(removeEdge);
		cutEdges.addAll(addEdges);
		//check if a graph from the new set of deleteEdges and deleteNodes is still
		//connected
		return new ImmutableAdjacencyGraph<N, Edge<N>>(cutNodes, cutEdges);
	}

	/**
	 * A SizeComparator compares two collections solely based on size
	 */
	private static class SizeComparator implements Comparator<Collection<?>>, Serializable
	{
		private static final long serialVersionUID = -4454396728238585057L;

		/**
		 * Compares the two collections based on their size.
		 * @param first The first collection
		 * @param second The second collection
		 * @return Which collection is larger
		 * @see java.util.Comparator#compare(Object, Object)
		 */
		@Override
		public int compare(final Collection<?> first, final Collection<?> second)
		{
			if( first.size() < second.size() )
				return -1;
			else if( first.size() > second.size() )
				return 1;
			return 0;
		}

		/**
		 * Returns true if the given object is a non-null SizeComparator. As this class
		 * has no state, all SizeComparators are equal.
		 * @param compareWith The object to compare with
		 * @return If the object is a non-null SizeComparator
		 */
		@Override
		public boolean equals(final Object compareWith)
		{
			if( compareWith == null )
				return false;

			return (compareWith instanceof SizeComparator);
		}


		@Override
		public int hashCode()
		{
			//Method only implemented for checkstyle reasons.
			return super.hashCode(); //Default implementation; may need to bring in line to equals
		}
	}
}
