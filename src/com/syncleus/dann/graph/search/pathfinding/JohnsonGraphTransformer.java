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
import java.util.*;

public class JohnsonGraphTransformer<N> implements GraphTransformer<BidirectedGraph<N, ? extends WeightedDirectedEdge<N>>>
{
	private static final Object blankNode = new Object();

	private boolean containsInfinite(final Graph<N,?> original)
	{
		for(final Edge edge : original.getEdges())
			if(edge instanceof Weighted)
				if(Double.isInfinite(((Weighted)edge).getWeight()))
					return true;
		return false;
	}

	@SuppressWarnings("unchecked")
	public BidirectedGraph<N, WeightedDirectedEdge<N>> transform(final BidirectedGraph<N,? extends WeightedDirectedEdge<N>> original)
	{
		if(original == null)
			throw new IllegalArgumentException("original can not be null");
		if(containsInfinite(original))
			throw new IllegalArgumentException("original cannot contain infinite weights");

		final Set<WeightedDirectedEdge<Object>> originalEdges = new HashSet<WeightedDirectedEdge<Object>>();
		for(final WeightedDirectedEdge<N> originalEdge : original.getEdges())
			originalEdges.add((WeightedDirectedEdge<Object>)originalEdge);
		final MutableDirectedAdjacencyGraph<Object, WeightedDirectedEdge<Object>> copyGraph = new MutableDirectedAdjacencyGraph<Object, WeightedDirectedEdge<Object>>(new HashSet<Object>(original.getNodes()), originalEdges );

		final Set<Object> originalNodes = copyGraph.getNodes();
		copyGraph.add(blankNode);
		for(final Object originalNode : originalNodes)
			copyGraph.add(new ImmutableWeightedDirectedEdge<Object>(blankNode, originalNode, 0.0));

		final BellmanFordPathFinder<Object, WeightedDirectedEdge<Object>> pathFinder = new BellmanFordPathFinder<Object, WeightedDirectedEdge<Object>>(copyGraph);

		final MutableDirectedAdjacencyGraph johnsonGraph = new MutableDirectedAdjacencyGraph(original.getNodes(), new HashSet<WeightedDirectedEdge<N>>(original.getEdges()));
		final List<WeightedDirectedEdge<N>> edges = new ArrayList<WeightedDirectedEdge<N>>(johnsonGraph.getEdges());
		for(final WeightedDirectedEdge<N> edge : edges)
		{
			final double newWeight = edge.getWeight() + this.getPathWeight(pathFinder.getBestPath(blankNode, edge.getSourceNode(), false)) - this.getPathWeight(pathFinder.getBestPath(blankNode, edge.getDestinationNode(), false));
			johnsonGraph.remove(edge);
			johnsonGraph.add(new SimpleWeightedDirectedEdge<N>(edge.getSourceNode(), edge.getDestinationNode(), newWeight));
		}

		return johnsonGraph;
	}

	private double getPathWeight(final List<WeightedDirectedEdge<Object>> path)
	{
		double weight = 0.0;
		for(final WeightedDirectedEdge<Object> node : path)
			weight += node.getWeight();
		return weight;
	}
}
