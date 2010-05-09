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
import com.syncleus.dann.graph.topological.*;
import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

public class PrimMinimumSpanningTreeFinder<N,E extends Edge<N>> implements RootedMinimumSpanningTreeFinder<N,E>
{
	@SuppressWarnings("unchecked")
	public Set<E> findMinimumSpanningTree(final Graph<N,E> graph)
	{
		boolean isDirected = false;
		if( graph instanceof BidirectedGraph )
		{
			isDirected = true;
			for(final E edge : graph.getEdges())
				if( !(edge instanceof DirectedEdge) )
					isDirected = false;
		}

		final N startNode;
		if( isDirected )
		{
			final TopologicalSorter<N> sorter = new SimpleTopologicalSorter<N>();
			final List<N> sortedNodes = sorter.sort((BidirectedGraph)graph);
			startNode = sortedNodes.get(0);
		}
		else
		{
			startNode = graph.getNodes().iterator().next();
		}

		return this.primCalculate(graph, startNode);
	}

	public Set<E> findMinimumSpanningTree(final Graph<N,E> graph, final N startNode)
	{
		return primCalculate(graph, startNode);
	}

	private Set<E> primCalculate(final Graph<N,E> graph, final N startNode)
	{
		final Set<E> mst = new HashSet<E>();
		final PrimMap primMap = new PrimMap();
		for(final N node : graph.getNodes())
			primMap.put(node, null);

		N currentNode = null;
		while(!primMap.isEmpty())
		{
			if(currentNode != null)
			{
				final Entry<N,E> currentEntry = primMap.pop();
				currentNode = currentEntry.getKey();
				mst.add(currentEntry.getValue());
			}
			else
			{
				primMap.remove(startNode);
				currentNode = startNode;
			}

			final Set<E> neighborEdges = graph.getTraversableEdges(currentNode);
			for(final E neighborEdge : neighborEdges)
			{
				final List<N> neighborNodes = new ArrayList<N>(neighborEdge.getNodes());
				//remove all occurance of currentNode, not just the first
				while( neighborNodes.remove(currentNode) ){}
				for(final N neighborNode : neighborNodes)
				{
					if(primMap.containsKey(neighborNode))
					{
						if(primMap.isLess(neighborNode, neighborEdge))
						{
							primMap.put(neighborNode, neighborEdge);
							primMap.resort();
						}
					}
				}
			}
		}
		return mst;
	}

	private class PrimMap extends HashMap<N,E>
	{
		private static final long serialVersionUID = 6345120112273301259L;

		private final Queue<Entry<N,E>> weightedNodes = new PriorityQueue<Entry<N,E>>(10, new EntryCompare());

		public void resort()
		{
			weightedNodes.clear();
			for(final Entry<N,E> entry : this.entrySet())
				weightedNodes.add(entry);
		}

		public double getWeight(final N node)
		{
			final E edge = this.get(node);
			return edgeToWeight(edge);
		}

		public boolean isLess(final N node, final E edge)
		{
			if(edge == null)
				throw new IllegalArgumentException("edge can not be null");
			if(edgeToWeight(edge) < getWeight(node))
				return true;
			return false;
		}

		private double edgeToWeight(final E edge)
		{
			if(edge == null)
				return Double.MAX_VALUE;
			if(edge instanceof Weighted)
				return ((Weighted)edge).getWeight();
			else
				return 0;
		}

		public Entry<N,E> pop()
		{
			final Entry<N,E> poped = weightedNodes.poll();
			if(poped != null)
				this.remove(poped.getKey());
			return poped;
		}

		private class EntryCompare implements Comparator<Entry<N,E>>, Serializable
		{
			private static final long serialVersionUID = -4356537864223227850L;
			
			public int compare(final Entry<N,E> first, final Entry<N,E> second)
			{
				double firstWeight = 0;
				if(first.getValue() == null)
					firstWeight = Double.MAX_VALUE;
				else if(first.getValue() instanceof Weighted)
					firstWeight = ((Weighted)first.getValue()).getWeight();

				double secondWeight = 0;
				if(second.getValue() == null)
					secondWeight = Double.MAX_VALUE;
				if(second.getValue() instanceof Weighted)
					secondWeight = ((Weighted)second.getValue()).getWeight();

				if(firstWeight < secondWeight)
					return -1;
				else if(firstWeight > secondWeight)
					return 1;
				else
					return 0;
			}
		}
	}
}
