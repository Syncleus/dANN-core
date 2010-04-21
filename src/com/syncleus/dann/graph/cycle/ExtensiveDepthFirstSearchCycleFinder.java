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
package com.syncleus.dann.graph.cycle;

import com.syncleus.dann.graph.*;
import java.util.*;

public class ExtensiveDepthFirstSearchCycleFinder extends ColoredDepthFirstSearchDetector implements CycleFinder
{
	public int cycleCount(Graph graph)
	{
		return this.find(graph).size();
	}

	public Set<Set<Edge>> find(Graph graph)
	{
		final Set<Object> untouchedNodes = new HashSet<Object>(graph.getNodes());
		final Set<Set<Edge>> cycles = new HashSet<Set<Edge>>();
		while(!untouchedNodes.isEmpty())
		{
			Object startingNode = untouchedNodes.toArray()[0];
			untouchedNodes.remove(startingNode);
			ExtensiveDepthFirstSearchCycleFinder.cyclesFromStart(graph, untouchedNodes, cycles, startingNode);
		}
		return cycles;
	}

	private static void cyclesFromStart(Graph graph, Set<Object> untouchedNodes, Set<Set<Edge>> cycles, Object startNode)
	{
		Stack<Object> parentNodes = new Stack<Object>();
		Stack<Edge> parentEdges = new Stack<Edge>();

		traverse(graph, untouchedNodes, cycles, parentNodes, parentEdges, null, startNode);
	}
	
	private static List<Object> neighborsFromEdge(Edge edge, Object sourceNode)
	{
		List<Object> destinations = new ArrayList<Object>(edge.getNodes());
		destinations.remove(sourceNode);
		return destinations;
	}

	private static void traverse(Graph graph, Set<Object> untouchedNodes, Set<Set<Edge>> cycles, Stack<Object> parentNodes, Stack<Edge> parentEdges, Edge lastEdge, Object currentNode)
	{
		untouchedNodes.remove(currentNode);
		parentNodes.push(currentNode);
		parentEdges.push(lastEdge);

		List<Edge> unexploredPaths = new ArrayList<Edge>(graph.getTraversableEdges(currentNode));
		if( lastEdge != null)
			unexploredPaths.remove(lastEdge);
		int cyclesFound = 0;

		for(Edge unexploredPath : unexploredPaths)
		{
			for(Object neighborNode : neighborsFromEdge(unexploredPath, currentNode))
			{
				if(!parentNodes.contains(neighborNode))
				{
					traverse(graph, untouchedNodes, cycles, parentNodes, parentEdges, unexploredPath, neighborNode);
				}
				else
				{
					List<Object> parentNodesCopy = new ArrayList<Object>(parentNodes);
					List<Edge> parentEdgesCopy = new ArrayList<Edge>(parentEdges);
					Set<Edge> cycle = new HashSet<Edge>();
					cycle.add(unexploredPath);

					Object currentCycleNode = parentNodesCopy.get(parentNodesCopy.size()-1);
					parentNodesCopy.remove(currentCycleNode);
					while(currentCycleNode != neighborNode)
					{
						currentCycleNode = parentNodesCopy.get(parentNodesCopy.size()-1);
						parentNodesCopy.remove(currentCycleNode);

						Edge currentCycleEdge = parentEdgesCopy.get(parentEdgesCopy.size()-1);
						parentEdgesCopy.remove(currentCycleEdge);

						if((currentCycleEdge != null))
							cycle.add(currentCycleEdge);
					}

					cycles.add(cycle);
					cyclesFound++;
				}
			}
		}

		parentNodes.pop();
		parentEdges.pop();
	}
}
