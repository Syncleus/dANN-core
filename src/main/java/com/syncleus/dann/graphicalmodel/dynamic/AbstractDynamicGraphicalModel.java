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
package com.syncleus.dann.graphicalmodel.dynamic;

import java.util.Set;
import com.syncleus.dann.graph.BidirectedEdge;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graphicalmodel.AbstractGraphicalModelAdjacencyGraph;

public abstract class AbstractDynamicGraphicalModel<N extends DynamicGraphicalModelNode, E extends BidirectedEdge<N>> extends AbstractGraphicalModelAdjacencyGraph<N, E> implements DynamicGraphicalModel<N, E>
{
	protected AbstractDynamicGraphicalModel()
	{
		super();
	}

	protected AbstractDynamicGraphicalModel(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractDynamicGraphicalModel(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	@Override
	public void learnStates(final boolean updateHistory)
	{
		for(final N node : this.getNodes())
		{
			node.learnState(updateHistory);
		}
	}

	@Override
	public AbstractDynamicGraphicalModel<N, E> cloneAdd(final E newEdge)
	{
		return (AbstractDynamicGraphicalModel<N, E>) super.cloneAdd(newEdge);
	}

	@Override
	public AbstractDynamicGraphicalModel<N, E> cloneAdd(final N newNode)
	{
		return (AbstractDynamicGraphicalModel<N, E>) super.cloneAdd(newNode);
	}

	@Override
	public AbstractDynamicGraphicalModel<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (AbstractDynamicGraphicalModel<N, E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractDynamicGraphicalModel<N, E> cloneRemove(final E edgeToRemove)
	{
		return (AbstractDynamicGraphicalModel<N, E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractDynamicGraphicalModel<N, E> cloneRemove(final N nodeToRemove)
	{
		return (AbstractDynamicGraphicalModel<N, E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractDynamicGraphicalModel<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (AbstractDynamicGraphicalModel<N, E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractDynamicGraphicalModel<N, E> clone()
	{
		return (AbstractDynamicGraphicalModel<N, E>) super.clone();
	}
}
