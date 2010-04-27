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
package com.syncleus.dann.graph.mst;

import com.syncleus.dann.graph.*;
import java.util.*;

public class KruskalMinimumSpanningTreeFinder<N, E extends Edge<N>> implements MinimumSpanningTreeFinder<N,E>
{
	public Set<E> findMinimumSpanningTree(Graph<N,E> graph)
	{
		final Set<Set<N>> componentNodeSets = new HashSet<Set<N>>();
		for(N node : graph.getNodes())
			componentNodeSets.add(Collections.singleton(node));
		final Queue<E> edgeQueue = new PriorityQueue<E>(graph.getEdges().size(), new WeightComparator<E>());
		edgeQueue.addAll(graph.getEdges());
		final Set<E> mstEdges = new HashSet<E>();
		final Set mstNodes = new HashSet();

		while(mstNodes.size() < graph.getNodes().size())
		{
			//find all the componentNodeSets which contains one of the end points
			//of the next edge
			E queuedEdge = edgeQueue.poll();
			if( queuedEdge == null )
				return null;
			
			Set<Set<N>> setContainingEndNodes = new HashSet<Set<N>>();
			for(Set<N> component : componentNodeSets)
			{
				for(N endNode : queuedEdge.getNodes())
				{
					if( component.contains(endNode) )
					{
						setContainingEndNodes.add(component);
						continue;
					}
				}
			}

			//if more than one set was found then merge them
			if(setContainingEndNodes.size() > 1)
			{
				Set mergedSet = new HashSet();
				for(Set toMerge : setContainingEndNodes)
				{
					mergedSet.addAll(toMerge);
					componentNodeSets.remove(toMerge);
				}
				componentNodeSets.add(mergedSet);

				mstEdges.add(queuedEdge);
				mstNodes.addAll(queuedEdge.getNodes());
			}
		}
		
		return mstEdges;
	}

	private static class WeightComparator<E> implements Comparator<E>
	{
		public int compare(E first, E second)
		{
			double firstWeight = 0;
			if(first instanceof Weighted)
				firstWeight = ((Weighted) first).getWeight();
			double secondWeight = 0;
			if(second instanceof Weighted)
				secondWeight = ((Weighted) second).getWeight();

			if(firstWeight < secondWeight)
				return -1;
			if(firstWeight > secondWeight)
				return 1;
			return 0;
		}
	}
}
