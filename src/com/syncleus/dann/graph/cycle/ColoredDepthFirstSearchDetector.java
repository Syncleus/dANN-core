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

public class ColoredDepthFirstSearchDetector implements CycleDetector
{
	public <N, E extends Edge<N>> boolean hasCycle(Graph<N,E> graph)
	{
		//A map of the current Node colors. Key is the node, value is null for
		//white, false for grey, true for black.
		final Map<N, Boolean> colorMap = new HashMap<N, Boolean>();

		Set<E> traversedEdges = new HashSet<E>();

		for(N node : graph.getNodes())
			if(!colorMap.containsKey(node))
				if( visit(graph, colorMap, traversedEdges, node) )
					return true;

		return false;
	}

	private static <N, E extends Edge<N>> boolean visit(Graph<N,E> graph, Map<N, Boolean> colorMap, Set<E> traversedEdges, N node)
	{
		colorMap.put(node, Boolean.FALSE);

		Set<E> traversableEdges = graph.getTraversableEdges(node);
		for(E neighborEdge : traversableEdges)
		{
			if(!ColoredDepthFirstSearchDetector.<E>traversed(traversedEdges, neighborEdge))
			{
				traversedEdges.add(neighborEdge);
				List<N> neighborNodes = new ArrayList<N>(neighborEdge.getNodes());
				neighborNodes.remove(node);
				for(N neighborNode : neighborNodes)
				{
					if(colorMap.get(neighborNode) == Boolean.FALSE)
						return true;
					else if(!colorMap.containsKey(neighborNode))
						if(visit(graph, colorMap, traversedEdges, neighborNode))
							return true;
				}
			}
		}
		colorMap.put(node, Boolean.TRUE);
		return false;
	}

	private static <E extends Edge> boolean traversed(Set<E> traversedEdges, E edge)
	{
		for(E traversedEdge : traversedEdges)
			if(traversedEdge == edge)
				return true;
		return false;
	}
}
