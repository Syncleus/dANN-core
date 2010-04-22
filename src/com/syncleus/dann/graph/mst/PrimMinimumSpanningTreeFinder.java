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

import com.syncleus.dann.graph.BidirectedGraph;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.Weighted;
import com.syncleus.dann.graph.topological.SimpleTopologicalSorter;
import com.syncleus.dann.graph.topological.TopologicalSorter;
import java.util.*;
import java.util.Map.Entry;

public class PrimMinimumSpanningTreeFinder<E extends Edge<?>> implements RootedMinimumSpanningTreeFinder<E>
{
	public Set<E> findMinimumSpanningTree(Graph<?, ? extends E> graph)
	{
		boolean isDirected = false;
		if( graph instanceof BidirectedGraph )
		{
			isDirected = true;
			for(E edge : graph.getEdges())
				if( !(edge instanceof DirectedEdge) )
					isDirected = false;
		}

		Object startNode;
		if( isDirected )
		{
			TopologicalSorter<Object> sorter = new SimpleTopologicalSorter<Object>();
			List<Object> sortedNodes = sorter.sort((BidirectedGraph)graph);
			startNode = sortedNodes.get(0);
		}
		else
		{
			startNode = graph.getNodes().toArray()[0];
		}

		return this.primCalculate((Graph)graph, startNode);
	}

	public <N> Set<E> findMinimumSpanningTree(Graph<N, ? extends E> graph, N startNode)
	{
		return primCalculate((Graph)graph, startNode);
	}

	private Set<E> primCalculate(Graph<Object, E> graph, Object startNode)
	{
		Set<E> mst = new HashSet<E>();
		PrimMap primMap = new PrimMap();
		for(Object node : graph.getNodes())
			primMap.put(node, null);

		Object currentNode = null;
		while(!primMap.isEmpty())
		{
			if(currentNode != null)
			{
				Entry<Object,E> currentEntry = primMap.pop();
				currentNode = currentEntry.getKey();
				mst.add(currentEntry.getValue());
			}
			else
			{
				primMap.remove(startNode);
				currentNode = startNode;
			}

			Set<E> neighborEdges = graph.getTraversableEdges(currentNode);
			for(E neighborEdge : neighborEdges)
			{
				List<Object> neighborNodes = new ArrayList<Object>(neighborEdge.getNodes());
				//remove all occurance of currentNode, not just the first
				while( neighborNodes.remove(currentNode) ){}
				for(Object neighborNode : neighborNodes)
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

	private class PrimMap extends HashMap<Object,E>
	{
		final Queue<Entry<Object,E>> weightedNodes = new PriorityQueue<Entry<Object,E>>(10, new EntryCompare());

		public void resort()
		{
			weightedNodes.clear();
			for(Entry<Object,E> entry : this.entrySet())
				weightedNodes.add(entry);
		}

		public double getWeight(Object node)
		{
			E edge = this.get(node);
			return edgeToWeight(edge);
		}

		public boolean isLess(Object node, E edge)
		{
			if(edge == null)
				throw new IllegalArgumentException("edge can not be null");
			if(edgeToWeight(edge) < getWeight(node))
				return true;
			return false;
		}

		private double edgeToWeight(E edge)
		{
			if(edge == null)
				return Double.MAX_VALUE;
			if(edge instanceof Weighted)
				return ((Weighted)edge).getWeight();
			else
				return 0;
		}

		public Entry<Object,E> pop()
		{
			Entry<Object,E> poped = weightedNodes.poll();
			if(poped != null)
				this.remove(poped.getKey());
			return poped;
		}

		private class EntryCompare implements Comparator<Entry<Object,E>>
		{
			public int compare(Entry<Object,E> first, Entry<Object,E> second)
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
