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
package com.syncleus.dann.graph.tree;

import java.util.List;
import java.util.Set;
import com.syncleus.dann.graph.*;
import com.syncleus.dann.graph.Cloud;
import com.syncleus.dann.graph.cycle.Cycles;
import com.syncleus.dann.graph.topological.Topography;
import com.syncleus.dann.graph.topological.sorter.*;

public final class Trees
{
	/**
	 * This is an utility class, so it may not be instantiated.
	 */
	private Trees()
	{
		throw new IllegalStateException("This is an utility class, it can not be instantiated");
	}

	public static <N, E extends Cloud<N>> boolean isSpanningTree(final Graph<N, E> graph, final Graph<N, E> subGraph)
	{
		if( graph instanceof TreeOptimizedGraph )
		{
			try
			{
				return ((TreeOptimizedGraph)graph).isSpanningTree(subGraph);
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		// TODO check if subgraph is actually a subgraph, another words that it is a subset of nodes and edges
		// TODO in fact we probably want to rethink this entirely
		return ((graph.getTargets().containsAll(subGraph.getTargets()))
				&& (Topography.isWeaklyConnected(subGraph))
				&& (Cycles.isAcyclic(subGraph)));
	}

	public static <N, E extends Cloud<N>> boolean isTree(final Graph<N, E> graph)
	{
		if( graph instanceof TreeOptimizedGraph )
		{
			try
			{
				return ((TreeOptimizedGraph)graph).isTree();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return ((Topography.isWeaklyConnected(graph)) && (Cycles.isAcyclic(graph)) && (Topography.isSimple(graph)));
	}

	public static <N, E extends Cloud<N>> boolean isForest(final Graph<N, E> graph)
	{
		if( graph instanceof TreeOptimizedGraph )
		{
			try
			{
				return ((TreeOptimizedGraph)graph).isForest();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		return ( (Cycles.isAcyclic(graph)) && (Topography.isSimple(graph)));
	}

	public static <N, E extends BidirectedEdge<N>> boolean isPolytree(final BidirectedGraph<N, E> graph)
	{
		if( graph instanceof TreeOptimizedBidirectedGraph )
		{
			try
			{
				return ((TreeOptimizedBidirectedGraph)graph).isPolytree();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		//TODO implement this
		throw new UnsupportedOperationException();
	}

	public static <N, E extends DirectedEdge<N>> boolean isRootedTree(final DirectedGraph<N, E> graph)
	{
		if( graph instanceof TreeOptimizedDirectedGraph )
		{
			try
			{
				return ((TreeOptimizedDirectedGraph)graph).isRootedTree();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		if( !Trees.isTree(graph) )
			return false;

		final TopologicalRanker<N> ranker = new SimpleTopologicalRanker<N>();
		final List<Set<N>> rankedNodes = ranker.rank(graph);

		if( rankedNodes.isEmpty() || ((rankedNodes.size() == 1)
				&& (rankedNodes.get(0).size() < 2) ) )
			return true;

		return (rankedNodes.get(0).size() == 1);
	}

	public static <N, E extends DirectedEdge<N>> boolean isRootedForest(final DirectedGraph<N, E> graph)
	{
		if( graph instanceof TreeOptimizedDirectedGraph )
		{
			try
			{
				return ((TreeOptimizedDirectedGraph)graph).isRootedForest();
			}
			catch(UnsupportedOperationException caught)
			{
				//if its not supported lets handle it as if its not optimized
			}
		}

		//TODO make this more efficient
		final Set<Graph<N, E>> components = Topography.getMaximallyConnectedComponents(graph);
		for(Graph<N, E> component : components)
		{
			final DirectedGraph<N, E> directedComponent = new ImmutableDirectedAdjacencyGraph<N, E>(component);
			if( !Trees.isRootedTree(directedComponent) )
				return false;
		}
		return true;
	}
}
