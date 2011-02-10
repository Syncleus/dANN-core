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
package com.syncleus.dann.graph.search.pathfinding;

import com.syncleus.dann.graph.Edge;
import com.syncleus.dann.graph.Graph;

public class DijkstraPathFinder<N, E extends Edge<N>> extends AstarPathFinder<N, E>
{
	public static final class ZeroHeuristicPathCost<N> implements HeuristicPathCost<N>
	{
		@Override
		public double getHeuristicPathCost(final N begin, final N end)
		{
			return 0.0;
		}

		@Override
		public boolean isOptimistic()
		{
			return true;
		}

		@Override
		public boolean isConsistent()
		{
			return true;
		}
	}

	public DijkstraPathFinder(final Graph<N, E> graph)
	{
		super(graph, new ZeroHeuristicPathCost<N>());
	}
}
