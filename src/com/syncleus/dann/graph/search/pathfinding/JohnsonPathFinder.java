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

import java.util.List;
import com.syncleus.dann.graph.*;

public class JohnsonPathFinder<N, E extends WeightedDirectedEdge<N>> implements PathFinder<N, WeightedDirectedEdge<N>>
{
	private final BidirectedGraph<N, E> graph;
	private final JohnsonGraphTransformer<N> TRANSFORMER = new JohnsonGraphTransformer<N>();

	public JohnsonPathFinder(final BidirectedGraph<N, E> graph)
	{
		if (graph == null)
			throw new IllegalArgumentException("graph can not be null");
		this.graph = graph;
	}

	public List<WeightedDirectedEdge<N>> getBestPath(final N begin, final N end)
	{
		final BidirectedGraph<N, WeightedDirectedEdge<N>> johnsonGraph = TRANSFORMER.transform(this.graph);
		final DijkstraPathFinder<N, WeightedDirectedEdge<N>> pathFinder = new DijkstraPathFinder<N, WeightedDirectedEdge<N>>(johnsonGraph);
		final List<WeightedDirectedEdge<N>> pathWalk = pathFinder.getBestPath(begin, end);
		if (pathWalk == null)
			return null;
		return pathWalk;
	}

	public boolean isReachable(final N begin, final N end)
	{
		return (this.getBestPath(begin, end) != null);
	}

	public boolean isConnected(final N begin, final N end)
	{
		return (this.getBestPath(begin, end) != null);
	}
}
