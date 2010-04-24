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
package com.syncleus.dann.graph.cycle;

import com.syncleus.dann.graph.*;
import java.util.*;

public class ExhaustiveDepthFirstSearchCycleFinder<N, E extends Edge<N>> extends ColoredDepthFirstSearchDetector implements CycleFinder<N,E>
{
	public boolean isPancyclic(Graph<N,E> graph)
	{
		if(!graph.isSimple())
			return false;
		
		final int graphOrder = graph.getOrder();
		Set<Cycle<N,E>> cycles = this.findCycles(graph);
		SortedSet<Cycle<N,E>> sortedCycles = new TreeSet<Cycle<N,E>>(new CycleLengthComparator<N,E>());
		sortedCycles.addAll(cycles);

		int neededCycleSize = 3;
		for(Cycle<N,E> sortedCycle : sortedCycles)
		{
			int currentCycleSize = sortedCycle.getLength();
			if(currentCycleSize == neededCycleSize)
			{
				if(currentCycleSize == graphOrder)
					return true;
				else
					neededCycleSize++;
			}
			else if(currentCycleSize > neededCycleSize)
				return false;
		}
		return false;
	}

	public boolean isUnicyclic(Graph<N,E> graph)
	{
		return (this.findCycles(graph).size() == 1);
	}

	public int cycleCount(Graph<N,E> graph)
	{
		return this.findCycles(graph).size();
	}

	public int girth(Graph<N,E> graph)
	{
		Set<Cycle<N,E>> cycles = this.findCycles(graph);
		SortedSet<Cycle<N,E>> sortedCycles = new TreeSet<Cycle<N,E>>(new CycleLengthComparator<N,E>());
		sortedCycles.addAll(cycles);
		return sortedCycles.first().getLength();
	}

	public int circumference(Graph<N,E> graph)
	{
		Set<Cycle<N,E>> cycles = this.findCycles(graph);
		SortedSet<Cycle<N,E>> sortedCycles = new TreeSet<Cycle<N,E>>(new CycleLengthComparator<N,E>());
		sortedCycles.addAll(cycles);
		return sortedCycles.last().getLength();
	}

	public Set<Cycle<N,E>> findCycles(Graph<N,E> graph)
	{
		final Set<N> untouchedNodes = new HashSet<N>(graph.getNodes());
		final Set<Cycle<N,E>> cycles = new HashSet<Cycle<N,E>>();
		while(!untouchedNodes.isEmpty())
		{
			N startingNode = (N) untouchedNodes.toArray()[0];
			untouchedNodes.remove(startingNode);
			ExhaustiveDepthFirstSearchCycleFinder.<N,E>cyclesFromStart(graph, untouchedNodes, cycles, startingNode);
		}
		return cycles;
	}

	private static <N,E extends Edge<N>> void cyclesFromStart(Graph<N,E> graph, Set<N> untouchedNodes, Set<Cycle<N,E>> cycles, N startNode)
	{
		Stack<N> parentNodes = new Stack<N>();
		Stack<E> parentEdges = new Stack<E>();

		ExhaustiveDepthFirstSearchCycleFinder.<N,E>traverse(graph, untouchedNodes, cycles, parentNodes, parentEdges, null, startNode);
	}
	
	private static <N,E extends Edge<N>> List<N> neighborsFromEdge(E edge, N sourceNode)
	{
		List<N> destinations = new ArrayList<N>(edge.getNodes());
		destinations.remove(sourceNode);
		return destinations;
	}

	private static <N,E extends Edge<N>> void traverse(Graph<N,E> graph, Set<N> untouchedNodes, Set<Cycle<N,E>> cycles, Stack<N> parentNodes, Stack<E> parentEdges, E lastEdge, N currentNode)
	{
		untouchedNodes.remove(currentNode);
		parentNodes.push(currentNode);
		parentEdges.push(lastEdge);

		List<E> unexploredPaths = new ArrayList<E>(graph.getTraversableEdges(currentNode));
		if( lastEdge != null)
			unexploredPaths.remove(lastEdge);
		int cyclesFound = 0;

		for(E unexploredPath : unexploredPaths)
		{
			for(N neighborNode : neighborsFromEdge(unexploredPath, currentNode))
			{
				if(!parentNodes.contains(neighborNode))
				{
					traverse(graph, untouchedNodes, cycles, parentNodes, parentEdges, unexploredPath, neighborNode);
				}
				else
				{
					List<N> parentNodesCopy = new ArrayList<N>(parentNodes);
					List<E> parentEdgesCopy = new ArrayList<E>(parentEdges);
					List<E> cycleEdges = new ArrayList<E>();
					List<N> cycleNodes = new ArrayList<N>();
					cycleEdges.add(unexploredPath);
					cycleNodes.add(neighborNode);

					N currentCycleNode = parentNodesCopy.get(parentNodesCopy.size()-1);
					cycleNodes.add(currentCycleNode);
					parentNodesCopy.remove(currentCycleNode);
					while(currentCycleNode != neighborNode)
					{
						currentCycleNode = parentNodesCopy.get(parentNodesCopy.size()-1);
						parentNodesCopy.remove(currentCycleNode);

						E currentCycleEdge = parentEdgesCopy.get(parentEdgesCopy.size()-1);
						parentEdgesCopy.remove(currentCycleEdge);

						if((currentCycleEdge != null))
						{
							cycleEdges.add(currentCycleEdge);
							cycleNodes.add(currentCycleNode);
						}
					}

					cycles.add(new SimpleCycle<N,E>(cycleEdges, cycleNodes));
					cyclesFound++;
				}
			}
		}

		parentNodes.pop();
		parentEdges.pop();
	}

	private static class CycleLengthComparator<N, E extends Edge<N>> implements Comparator<Cycle<N,E>>
	{
		public int compare(Cycle<N,E> first, Cycle<N,E> second)
		{
			if(first.getLength() < second.getLength())
				return -1;
			else if(first.getLength() > second.getLength())
				return 1;
			return 0;
		}

		@Override
		public boolean equals(Object compareWith)
		{
			if(compareWith instanceof CycleLengthComparator)
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
