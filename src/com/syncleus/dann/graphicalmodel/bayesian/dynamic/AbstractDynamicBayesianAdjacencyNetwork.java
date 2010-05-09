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
package com.syncleus.dann.graphicalmodel.bayesian.dynamic;

import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graphicalmodel.bayesian.AbstractBayesianAdjacencyNetwork;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianEdge;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianNode;
import java.util.Set;

public abstract class AbstractDynamicBayesianAdjacencyNetwork<N extends DynamicBayesianNode, E extends BayesianEdge<N>> extends AbstractBayesianAdjacencyNetwork<N,E> implements DynamicBayesianNetwork<N,E>
{
	protected AbstractDynamicBayesianAdjacencyNetwork()
	{
		super();
	}

	protected AbstractDynamicBayesianAdjacencyNetwork(final Graph<N,E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractDynamicBayesianAdjacencyNetwork(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	public void learnStates(final boolean updateHistory)
	{
		for(final BayesianNode node : this.getNodes())
		{
			if( node instanceof DynamicBayesianNode)
				((DynamicBayesianNode)node).learnState(updateHistory);
			else
				node.learnState();
		}
	}

	@Override
	public AbstractDynamicBayesianAdjacencyNetwork<N, E> cloneAdd(final E newEdge)
	{
		return (AbstractDynamicBayesianAdjacencyNetwork<N, E>)super.cloneAdd(newEdge);
	}

	@Override
	public AbstractDynamicBayesianAdjacencyNetwork<N, E> cloneAdd(final N newNode)
	{
		return (AbstractDynamicBayesianAdjacencyNetwork<N, E>)super.cloneAdd(newNode);
	}

	@Override
	public AbstractDynamicBayesianAdjacencyNetwork<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (AbstractDynamicBayesianAdjacencyNetwork<N, E>)super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractDynamicBayesianAdjacencyNetwork<N, E> cloneRemove(final E edgeToRemove)
	{
		return (AbstractDynamicBayesianAdjacencyNetwork<N, E>)super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractDynamicBayesianAdjacencyNetwork<N, E> cloneRemove(final N nodeToRemove)
	{
		return (AbstractDynamicBayesianAdjacencyNetwork<N, E>)super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractDynamicBayesianAdjacencyNetwork<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (AbstractDynamicBayesianAdjacencyNetwork<N, E>)super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractDynamicBayesianAdjacencyNetwork<N, E> clone()
	{
		return (AbstractDynamicBayesianAdjacencyNetwork<N, E>)super.clone();
	}
}
