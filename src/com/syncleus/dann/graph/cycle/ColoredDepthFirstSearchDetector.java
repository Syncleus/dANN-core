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
	public boolean hasCycle(Graph graph)
	{
		//A map of the current Node colors. Key is the node, value is null for
		//white, false for grey, true for black.
		final Map<Object, Boolean> colorMap = new HashMap<Object, Boolean>();

		Set<Edge> traversedEdges = new HashSet<Edge>();

		for(Object node : graph.getNodes())
			if(!colorMap.containsKey(node))
				if( visit(graph, colorMap, traversedEdges, node) )
					return true;

		return false;
	}

	private static boolean visit(Graph graph, Map<Object, Boolean> colorMap, Set<Edge> traversedEdges, Object node)
	{
		colorMap.put(node, Boolean.FALSE);

		Set<Edge> traversableEdges = graph.getTraversableEdges(node);
		for(Edge neighborEdge : traversableEdges)
		{
			if(!traversed(traversedEdges, neighborEdge))
			{
				traversedEdges.add(neighborEdge);
				List<Object> neighborNodes = new ArrayList<Object>(neighborEdge.getNodes());
				neighborNodes.remove(node);
				for(Object neighborNode : neighborNodes)
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

	private static boolean traversed(Set<Edge> traversedEdges, Edge edge)
	{
		for(Edge traversedEdge : traversedEdges)
			if(traversedEdge == edge)
				return true;
		return false;
	}
}
