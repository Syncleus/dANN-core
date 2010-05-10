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

public abstract class AbstractTreeAdjacencyGraph<N, E extends BidirectedEdge<N>> extends AbstractBidirectedAdjacencyGraph<N, E> implements TreeGraph<N, E>
{
	// TODO restrict tree's to only maximally connected trees

	protected AbstractTreeAdjacencyGraph()
	{
		super();
	}

	protected AbstractTreeAdjacencyGraph(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractTreeAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	public boolean isLeaf(final N node)
	{
		return (this.getDegree(node) == 1);
	}

	public boolean isLeaf(final E edge)
	{
		for(final N node : edge.getNodes())
			if( this.isLeaf(node) )
				return true;
		return false;
	}

	@Override
	public AbstractTreeAdjacencyGraph<N, E> cloneAdd(final E newEdge)
	{
		return (AbstractTreeAdjacencyGraph<N, E>) super.cloneAdd(newEdge);
	}

	@Override
	public AbstractTreeAdjacencyGraph<N, E> cloneAdd(final N newNode)
	{
		return (AbstractTreeAdjacencyGraph<N, E>) super.cloneAdd(newNode);
	}

	@Override
	public AbstractTreeAdjacencyGraph<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (AbstractTreeAdjacencyGraph<N, E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractTreeAdjacencyGraph<N, E> cloneRemove(final E edgeToRemove)
	{
		return (AbstractTreeAdjacencyGraph<N, E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractTreeAdjacencyGraph<N, E> cloneRemove(final N nodeToRemove)
	{
		return (AbstractTreeAdjacencyGraph<N, E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractTreeAdjacencyGraph<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (AbstractTreeAdjacencyGraph<N, E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractTreeAdjacencyGraph<N, E> clone()
	{
		return (AbstractTreeAdjacencyGraph<N, E>) super.clone();
	}
}
