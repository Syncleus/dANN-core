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

public abstract class AbstractHyperAdjacencyGraph<N, E extends HyperEdge<N>> extends AbstractAdjacencyGraph<N, E> implements HyperGraph<N, E>
{
	protected AbstractHyperAdjacencyGraph()
	{
		super();
	}

	protected AbstractHyperAdjacencyGraph(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractHyperAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	/**
	 * This will always return false.
	 *
	 * @return always returns false
	 */
	@Override
	public boolean hasMaximumAllowableRank()
	{
		return false;
	}

	/**
	 * Always returns -1 since rank is not limited.
	 *
	 * @return Always returns -1
	 */
	@Override
	public int getMaximumAllowableRank()
	{
		return -1;
	}

	/**
	 * Always returns false since rank is not limited.
	 *
	 * @return Always returns false
	 */
	@Override
	public boolean hasMinimumAllowableRank()
	{
		return false;
	}

	/**
	 * Always returns -1 since rank is not limited
	 * @return
	 */
	@Override
	public int getMinimumAllowableRank()
	{
		return -1;
	}

	@Override
	public AbstractHyperAdjacencyGraph<N, E> cloneAdd(final E newEdge)
	{
		return (AbstractHyperAdjacencyGraph<N, E>) super.cloneAdd(newEdge);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N, E> cloneAdd(final N newNode)
	{
		return (AbstractHyperAdjacencyGraph<N, E>) super.cloneAdd(newNode);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (AbstractHyperAdjacencyGraph<N, E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N, E> cloneRemove(final E edgeToRemove)
	{
		return (AbstractHyperAdjacencyGraph<N, E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N, E> cloneRemove(final N nodeToRemove)
	{
		return (AbstractHyperAdjacencyGraph<N, E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (AbstractHyperAdjacencyGraph<N, E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractHyperAdjacencyGraph<N, E> clone()
	{
		return (AbstractHyperAdjacencyGraph<N, E>) super.clone();
	}
}
