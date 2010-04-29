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

import com.syncleus.dann.graph.*;
import java.util.List;

public class JohnsonPathFinder<N, E extends WeightedDirectedEdge<N>> implements PathFinder<N,WeightedDirectedEdge<N>>
{
	private BidirectedGraph<N,E> graph;
	private final static Object BLANK_NODE = new Object();
	// TODO stop using the non-generic form
	private final JohnsonGraphTransformer TRANSFORMER = new JohnsonGraphTransformer(BLANK_NODE);

	public JohnsonPathFinder(BidirectedGraph<N,E> graph)
	{
		if(graph == null)
			throw new IllegalArgumentException("graph can not be null");
		this.graph = graph;
	}

	public List<WeightedDirectedEdge<N>> getBestPath(N begin, N end)
	{
		BidirectedGraph johnsonGraph = TRANSFORMER.transform(this.graph);
		DijkstraPathFinder<N,WeightedDirectedEdge<N>> pathFinder = new DijkstraPathFinder<N,WeightedDirectedEdge<N>>(johnsonGraph);
		List<WeightedDirectedEdge<N>> pathWalk = pathFinder.getBestPath(begin, end);

		if(pathWalk == null)
			return null;

		return pathWalk;
	}

	public boolean isReachable(N begin, N end)
	{
		return (this.getBestPath(begin, end) != null);
	}

	public boolean isConnected(N begin, N end)
	{
		return (this.getBestPath(begin, end) != null);
	}
}
