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
import java.util.Map.Entry;

public class ImmutableTreeAdjacencyGraph<N, E extends BidirectedEdge<N>> extends AbstractTreeAdjacencyGraph<N,E>
{
	private static final long serialVersionUID = -7193318700735007603L;

	public ImmutableTreeAdjacencyGraph()
	{
		super();
	}
	
	public ImmutableTreeAdjacencyGraph(final Graph<N,E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	public ImmutableTreeAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes,edges);
	}

	@Override
	protected Set<E> getInternalEdges()
	{
		return Collections.unmodifiableSet(super.getInternalEdges());
	}

	@Override
	protected Map<N, Set<E>> getInternalAdjacencyEdges()
	{
		final Map<N,Set<E>> newAdjacentEdges = new HashMap<N, Set<E>>();
		for(final Entry<N, Set<E>> neighborEdgeEntry : super.getInternalAdjacencyEdges().entrySet())
			newAdjacentEdges.put(neighborEdgeEntry.getKey(), new HashSet<E>(neighborEdgeEntry.getValue()));
		return newAdjacentEdges;
	}

	@Override
	protected Map<N, List<N>> getInternalAdjacencyNodes()
	{
		final Map<N,List<N>> newAdjacentNodes = new HashMap<N, List<N>>();
		for(final Entry<N, List<N>> neighborNodeEntry : this.getInternalAdjacencyNodes().entrySet())
			newAdjacentNodes.put(neighborNodeEntry.getKey(), new ArrayList<N>(neighborNodeEntry.getValue()));
		return newAdjacentNodes;
	}

	@Override
	public ImmutableTreeAdjacencyGraph<N,E> cloneAdd(final E newEdge)
	{
		return (ImmutableTreeAdjacencyGraph<N,E>) super.cloneAdd(newEdge);
	}

	@Override
	public ImmutableTreeAdjacencyGraph<N,E> cloneAdd(final N newNode)
	{
		return (ImmutableTreeAdjacencyGraph<N,E>) super.cloneAdd(newNode);
	}

	@Override
	public ImmutableTreeAdjacencyGraph<N,E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (ImmutableTreeAdjacencyGraph<N,E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public ImmutableTreeAdjacencyGraph<N,E> cloneRemove(final E edgeToRemove)
	{
		return (ImmutableTreeAdjacencyGraph<N,E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public ImmutableTreeAdjacencyGraph<N,E> cloneRemove(final N nodeToRemove)
	{
		return (ImmutableTreeAdjacencyGraph<N,E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public ImmutableTreeAdjacencyGraph<N,E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (ImmutableTreeAdjacencyGraph<N,E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public ImmutableTreeAdjacencyGraph<N,E> clone()
	{
		return (ImmutableTreeAdjacencyGraph<N,E>) super.clone();
	}
}
