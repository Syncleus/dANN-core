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

import java.util.Set;

public abstract class AbstractHyperAdjacencyGraph<N, E extends HyperEdge<N>> extends AbstractAdjacencyGraph<N,E> implements HyperGraph<N,E>
{
	protected AbstractHyperAdjacencyGraph()
	{
		super();
	}

	protected AbstractHyperAdjacencyGraph(final Graph<N,E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractHyperAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	public int getRank()
	{
		return -1;
	}
	
	public BidirectedGraph<N,BidirectedEdge<N>> getPrimal()
	{
		return null;
	}

	public boolean isPartial(final HyperGraph<N,E> partialGraph)
	{
		return false;
	}

	public boolean isHost(final HyperGraph<N,E> hostGraph)
	{
		return false;
	}

	public boolean isUniform()
	{
		return false;
	}

	@Override
	public AbstractHyperAdjacencyGraph<N,E> cloneAdd(E newEdge)
	{
		return (AbstractHyperAdjacencyGraph<N,E>) super.cloneAdd(newEdge);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N,E> cloneAdd(N newNode)
	{
		return (AbstractHyperAdjacencyGraph<N,E>) super.cloneAdd(newNode);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N,E> cloneAdd(Set<N> newNodes, Set<E> newEdges)
	{
		return (AbstractHyperAdjacencyGraph<N,E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N,E> cloneRemove(E edgeToRemove)
	{
		return (AbstractHyperAdjacencyGraph<N,E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N,E> cloneRemove(N nodeToRemove)
	{
		return (AbstractHyperAdjacencyGraph<N,E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N,E> cloneRemove(Set<N> deleteNodes, Set<E> deleteEdges)
	{
		return (AbstractHyperAdjacencyGraph<N,E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N,E> clone()
	{
		return (AbstractHyperAdjacencyGraph<N,E>) super.clone();
	}
}
