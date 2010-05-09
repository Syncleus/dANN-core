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

public abstract class AbstractRootedTreeAdjacencyGraph<N, E extends BidirectedEdge<N>> extends AbstractTreeAdjacencyGraph<N,E> implements RootedTreeGraph<N,E>
{
	protected AbstractRootedTreeAdjacencyGraph()
	{
		super();
	}

	protected AbstractRootedTreeAdjacencyGraph(final Graph<N,E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractRootedTreeAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	public N getRoot()
	{
		return null;
	}

	@Override
	public AbstractRootedTreeAdjacencyGraph<N,E> cloneAdd(final E newEdge)
	{
		return (AbstractRootedTreeAdjacencyGraph<N,E>) super.cloneAdd(newEdge);
	}

	@Override
	public AbstractRootedTreeAdjacencyGraph<N,E> cloneAdd(final N newNode)
	{
		return (AbstractRootedTreeAdjacencyGraph<N,E>) super.cloneAdd(newNode);
	}

	@Override
	public AbstractRootedTreeAdjacencyGraph<N,E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (AbstractRootedTreeAdjacencyGraph<N,E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractRootedTreeAdjacencyGraph<N,E> cloneRemove(final E edgeToRemove)
	{
		return (AbstractRootedTreeAdjacencyGraph<N,E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractRootedTreeAdjacencyGraph<N,E> cloneRemove(final N nodeToRemove)
	{
		return (AbstractRootedTreeAdjacencyGraph<N,E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractRootedTreeAdjacencyGraph<N,E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (AbstractRootedTreeAdjacencyGraph<N,E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractRootedTreeAdjacencyGraph<N,E> clone()
	{
		return (AbstractRootedTreeAdjacencyGraph<N,E>) super.clone();
	}
}
