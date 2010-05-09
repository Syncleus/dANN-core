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

import java.util.*;

public abstract class AbstractBidirectedAdjacencyGraph<N, E extends BidirectedEdge<N>> extends AbstractAdjacencyGraph<N, E> implements BidirectedGraph<N, E>
{
	protected AbstractBidirectedAdjacencyGraph()
	{
		super();
	}

	protected AbstractBidirectedAdjacencyGraph(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractBidirectedAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	public boolean isPolytree()
	{
		return false;
	}

	@Override
	public Set<Graph<N, E>> getMaximallyConnectedComponents()
	{
		return null;
	}

	@Override
	public int getDegree(final N node)
	{
		final Set<E> adjacentEdges = this.getAdjacentEdges(node);
		int degree = 0;
		for(final E adjacentEdge : adjacentEdges)
		{
			if (adjacentEdge.isLoop())
				degree += 2;
			else
				degree++;
		}
		return degree;
	}

	public Set<E> getInEdges(final N node)
	{
		final Set<E> inEdges = new HashSet<E>();
		for(final E edge : this.getEdges())
		{
			final List<N> adjacentNodes = new ArrayList<N>(edge.getNodes());
			adjacentNodes.remove(node);
			final N adjacentNode = adjacentNodes.get(0);
			if (edge.isTraversable(adjacentNode) && edge.getTraversableNodes(adjacentNode).contains(node))
				inEdges.add(edge);
		}
		return Collections.unmodifiableSet(inEdges);
	}

	public int getIndegree(final N node)
	{
		return this.getInEdges(node).size();
	}

	public int getOutdegree(final N node)
	{
		return this.getTraversableEdges(node).size();
	}

	@Override
	public AbstractBidirectedAdjacencyGraph<N, E> cloneAdd(final E newEdge)
	{
		return (AbstractBidirectedAdjacencyGraph<N, E>) super.cloneAdd(newEdge);
	}

	@Override
	public AbstractBidirectedAdjacencyGraph<N, E> cloneAdd(final N newNode)
	{
		return (AbstractBidirectedAdjacencyGraph<N, E>) super.cloneAdd(newNode);
	}

	@Override
	public AbstractBidirectedAdjacencyGraph<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (AbstractBidirectedAdjacencyGraph<N, E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractBidirectedAdjacencyGraph<N, E> cloneRemove(final E edgeToRemove)
	{
		return (AbstractBidirectedAdjacencyGraph<N, E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractBidirectedAdjacencyGraph<N, E> cloneRemove(final N nodeToRemove)
	{
		return (AbstractBidirectedAdjacencyGraph<N, E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractBidirectedAdjacencyGraph<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (AbstractBidirectedAdjacencyGraph<N, E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractBidirectedAdjacencyGraph<N, E> clone()
	{
		return (AbstractBidirectedAdjacencyGraph<N, E>) super.clone();
	}
}
