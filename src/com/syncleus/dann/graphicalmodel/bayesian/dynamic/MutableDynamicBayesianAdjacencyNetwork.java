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

import java.util.*;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianEdge;

public class MutableDynamicBayesianAdjacencyNetwork<N extends DynamicBayesianNode, E extends BayesianEdge<N>> extends AbstractDynamicBayesianAdjacencyNetwork<N, E> implements MutableDynamicBayesianNetwork<N, E>
{
	private static final long serialVersionUID = -7951102585507791756L;

	public MutableDynamicBayesianAdjacencyNetwork()
	{
		super();
	}

	public MutableDynamicBayesianAdjacencyNetwork(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	public MutableDynamicBayesianAdjacencyNetwork(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	public boolean add(final E newEdge)
	{
		if (newEdge == null)
			throw new IllegalArgumentException("newEdge can not be null");
		if (!this.getNodes().containsAll(newEdge.getNodes()))
			throw new IllegalArgumentException("newEdge has a node as an end point that is not part of the graph");
		if (this.getInternalEdges().add(newEdge))
		{
			for(final N currentNode : newEdge.getNodes())
			{
				this.getInternalAdjacencyEdges().get(currentNode).add(newEdge);
				final List<N> newAdjacentNodes = new ArrayList<N>(newEdge.getNodes());
				newAdjacentNodes.remove(currentNode);
				for(final N newAdjacentNode : newAdjacentNodes)
					this.getInternalAdjacencyNodes().get(currentNode).add(newAdjacentNode);
			}
			return true;
		}
		return false;
	}

	public boolean add(final N newNode)
	{
		if (newNode == null)
			throw new IllegalArgumentException("newNode can not be null");
		if (this.getNodes().contains(newNode))
			return false;
		this.getInternalAdjacencyEdges().put(newNode, new HashSet<E>());
		this.getInternalAdjacencyNodes().put(newNode, new ArrayList<N>());
		return true;
	}

	public boolean remove(final E edgeToRemove)
	{
		if (edgeToRemove == null)
			throw new IllegalArgumentException("removeSynapse can not be null");
		if (!this.getInternalEdges().remove(edgeToRemove))
			return false;
		for(final N removeNode : edgeToRemove.getNodes())
		{
			this.getInternalAdjacencyEdges().get(removeNode).remove(edgeToRemove);
			final List<N> removeAdjacentNodes = new ArrayList<N>(edgeToRemove.getNodes());
			removeAdjacentNodes.remove(removeNode);
			for(final N removeAdjacentNode : removeAdjacentNodes)
				this.getInternalAdjacencyNodes().get(removeNode).remove(removeAdjacentNode);
		}
		return true;
	}

	public boolean remove(final N nodeToRemove)
	{
		if (nodeToRemove == null)
			throw new IllegalArgumentException("node can not be null");
		if (!this.getNodes().contains(nodeToRemove))
			return false;
		final Set<E> removeEdges = this.getInternalAdjacencyEdges().get(nodeToRemove);
		//remove all the edges
		for(final E removeEdge : removeEdges)
			this.remove(removeEdge);
		//modify edges by removing the node to remove
		final Set<E> newEdges = new HashSet<E>();
		for(final E removeEdge : removeEdges)
		{
			E newEdge = (E) removeEdge.disconnect(nodeToRemove);
			while ((newEdge.getNodes().contains(nodeToRemove)) && (newEdge != null))
				newEdge = (E) removeEdge.disconnect(nodeToRemove);
			if (newEdge != null)
				newEdges.add(newEdge);
		}
		//add the modified edges
		for(final E newEdge : newEdges)
			this.add(newEdge);
		//remove the node itself
		this.getInternalAdjacencyEdges().remove(nodeToRemove);
		this.getInternalAdjacencyNodes().remove(nodeToRemove);
		return true;
	}

	@Override
	public MutableDynamicBayesianAdjacencyNetwork<N, E> cloneAdd(final E newEdge)
	{
		return (MutableDynamicBayesianAdjacencyNetwork<N, E>) super.cloneAdd(newEdge);
	}

	@Override
	public MutableDynamicBayesianAdjacencyNetwork<N, E> cloneAdd(final N newNode)
	{
		return (MutableDynamicBayesianAdjacencyNetwork<N, E>) super.cloneAdd(newNode);
	}

	@Override
	public MutableDynamicBayesianAdjacencyNetwork<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (MutableDynamicBayesianAdjacencyNetwork<N, E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public MutableDynamicBayesianAdjacencyNetwork<N, E> cloneRemove(final E edgeToRemove)
	{
		return (MutableDynamicBayesianAdjacencyNetwork<N, E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public MutableDynamicBayesianAdjacencyNetwork<N, E> cloneRemove(final N nodeToRemove)
	{
		return (MutableDynamicBayesianAdjacencyNetwork<N, E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public MutableDynamicBayesianAdjacencyNetwork<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (MutableDynamicBayesianAdjacencyNetwork<N, E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public MutableDynamicBayesianAdjacencyNetwork<N, E> clone()
	{
		return (MutableDynamicBayesianAdjacencyNetwork<N, E>) super.clone();
	}
}
