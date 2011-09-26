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
package com.syncleus.dann.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractBidirectedAdjacencyGraph<N, E extends BidirectedEdge<N>> extends AbstractAdjacencyGraph<N, E> implements BidirectedGraph<N, E>
{
	protected AbstractBidirectedAdjacencyGraph()
	{
		super();
	}

	protected AbstractBidirectedAdjacencyGraph(final CloudGraph<N, E> copyGraph)
	{
		super(copyGraph.getTargets(), copyGraph.getEdges());
	}

	protected AbstractBidirectedAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	@Override
	public Set<E> getInEdges(final N node)
	{
		final Set<E> inEdges = new HashSet<E>();
		for(final E edge : this.getEdges())
		{
			final List<N> adjacentNodes = new ArrayList<N>(edge.getTargets());
			adjacentNodes.remove(node);
			final N adjacentNode = adjacentNodes.get(0);

			if( edge.isTraversable(adjacentNode) && edge.getTraversableNodes(adjacentNode).contains(node) )
				inEdges.add(edge);
		}
		return Collections.unmodifiableSet(inEdges);
	}

	@Override
	protected AbstractBidirectedAdjacencyGraph<N, E> clone()
	{
		return (AbstractBidirectedAdjacencyGraph<N, E>) super.clone();
	}
}
