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
package com.syncleus.dann.graph.tree.mst;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import com.syncleus.dann.graph.TraversableEdge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.Weighted;

/**
 * An implementation of
 * <a href="http://en.wikipedia.org/wiki/Kruskal's_algorithm">
 *     Kruskal's minimum spanning tree algorithm</a>.
 * If the given graph is connected it computes the minimum spanning tree,
 * otherwise it computes the minimum spanning forest. The algorithm runs in time
 * O(E log E). This implementation uses the hashCode and equals method of the
 * vertices.
 * @author Jeffrey Phillips Freeman
 * @param <N> The node type
 * @param <E> The type of edge for the given node type
 */
public class KruskalMinimumSpanningTreeFinder<N, E extends TraversableEdge<N>> implements MinimumSpanningTreeFinder<N, E>
{
	@Override
	public Set<E> findMinimumSpanningTree(final Graph<N, E> graph)
	{
		final Set<Set<N>> componentNodeSets = new HashSet<Set<N>>();
		for(final N node : graph.getNodes())
			componentNodeSets.add(Collections.singleton(node));
		final Queue<E> edgeQueue = new PriorityQueue<E>(graph.getEdges().size(), new WeightComparator<E>());
		edgeQueue.addAll(graph.getEdges());
		final Set<E> mstEdges = new HashSet<E>();
		while( componentNodeSets.size() > 1 )
		{
			//find all the componentNodeSets which contains one of the end points
			//of the next edge
			final E queuedEdge = edgeQueue.poll();
			if( queuedEdge == null )
				return null;
			final Set<Set<N>> setContainingEndNodes = new HashSet<Set<N>>();
			for(final Set<N> component : componentNodeSets)
			{
				for(final N endNode : queuedEdge.getNodes())
				{
					if( component.contains(endNode) )
					{
						setContainingEndNodes.add(component);
					}
				}
			}
			//if more than one set was found then merge them
			if( setContainingEndNodes.size() > 1 )
			{
				final Set<N> mergedSet = new HashSet<N>();
				for(final Set<N> toMerge : setContainingEndNodes)
				{
					mergedSet.addAll(toMerge);
					componentNodeSets.remove(toMerge);
				}
				componentNodeSets.add(mergedSet);
				mstEdges.add(queuedEdge);
			}
		}
		return mstEdges;
	}

	private static class WeightComparator<E> implements Comparator<E>, Serializable
	{
		private static final long serialVersionUID = 4497530556915589495L;

		@Override
		public int compare(final E first, final E second)
		{
			double firstWeight = 0;
			if( first instanceof Weighted )
				firstWeight = ((Weighted) first).getWeight();
			double secondWeight = 0;
			if( second instanceof Weighted )
				secondWeight = ((Weighted) second).getWeight();
			if( firstWeight < secondWeight )
				return -1;
			if( firstWeight > secondWeight )
				return 1;
			return 0;
		}
	}
}
