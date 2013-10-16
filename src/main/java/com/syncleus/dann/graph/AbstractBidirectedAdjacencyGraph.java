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

/**
 * An AbstractBidirectedAdjacencyGraph is a BidirectedGraph implemented using adjacency lists.
 *
 * @since 2.0
 * @param <N> The node type
 * @param <E> The type of edge for the given node type
 */
public abstract class AbstractBidirectedAdjacencyGraph<N, E extends BidirectedEdge<N>> extends AbstractAdjacencyGraph<N, E> implements BidirectedGraph<N, E>
{
    /**
     * Creates a new graph with no edges and no adjacencies.
     * nodeContext and edgeContext is enabled.
     */
	protected AbstractBidirectedAdjacencyGraph()
	{
		super();
	}

    /**
     * Creates a new graph as a copy of the current Graph.
     * nodeContext is enabled.
     * @param copyGraph The Graph to copy
     */
	protected AbstractBidirectedAdjacencyGraph(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

    /**
     * Creates a new graph from the given list of nodes, and
     * the given list of Edges.
     * The adjacency lists are created from this structure. nodeContext is
     * enabled.
     *
     * @param nodes The set of all nodes
     * @param edges The set of all ourEdges
     */
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
			final List<N> adjacentNodes = new ArrayList<N>(edge.getNodes());
			adjacentNodes.remove(node);
			final N adjacentNode = adjacentNodes.get(0);

			if( edge.isTraversable(adjacentNode) && edge.getTraversableNodes(adjacentNode).contains(node) )
				inEdges.add(edge);
		}
		return Collections.unmodifiableSet(inEdges);
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
