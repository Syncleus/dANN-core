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

import com.syncleus.dann.graph.BidirectedGraph;
import com.syncleus.dann.graph.SimpleWeightedBidirectedWalk;
import com.syncleus.dann.graph.WeightedBidirectedWalk;
import com.syncleus.dann.graph.WeightedDirectedEdge;
import com.syncleus.dann.graph.WeightedWalk;
import java.util.List;

public class JohnsonPathFinder<G extends BidirectedGraph<N, WeightedDirectedEdge<N>, WeightedBidirectedWalk<N, WeightedDirectedEdge<N>>>, N, E extends WeightedDirectedEdge<N>> implements PathFinder<N,WeightedDirectedEdge<N>,WeightedBidirectedWalk<N, WeightedDirectedEdge<N>>>
{
	private G graph;
	private final static Object BLANK_NODE = new Object();
	private final static JohnsonGraphTransformer TRANSFORMER = new JohnsonGraphTransformer(BLANK_NODE);

	public JohnsonPathFinder(G graph)
	{
		if(graph == null)
			throw new IllegalArgumentException("graph can not be null");
		this.graph = graph;
	}

	public WeightedBidirectedWalk<N, WeightedDirectedEdge<N>> getBestPath(N begin, N end)
	{
		BidirectedGraph johnsonGraph = TRANSFORMER.transform(this.graph);
		DijkstraPathFinder pathFinder = new DijkstraPathFinder(johnsonGraph);
		WeightedWalk pathWalk = pathFinder.getBestPath(begin, end);

		if(pathWalk == null)
			return null;

		return new SimpleWeightedBidirectedWalk<N, WeightedDirectedEdge<N>>((N) pathWalk.getFirstNode(), (N) pathWalk.getLastNode(), (List<WeightedDirectedEdge<N>>) pathWalk.getSteps());
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
