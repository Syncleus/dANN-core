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
import com.syncleus.dann.graph.topological.SimpleTopologicalSorter;
import com.syncleus.dann.graph.topological.TopologicalSorter;

public abstract class AbstractDirectedAdjacencyGraph<N, E extends DirectedEdge<N>> extends AbstractBidirectedAdjacencyGraph<N, E> implements DirectedGraph<N, E>
{
	protected AbstractDirectedAdjacencyGraph()
	{
		super();
	}

	protected AbstractDirectedAdjacencyGraph(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	protected AbstractDirectedAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	public boolean isRootedTree()
	{
		if( !this.isTree() )
			return false;

		TopologicalSorter<N> sorter = new SimpleTopologicalSorter<N>();
		List<N> sortedNodes = sorter.sort(this);

		if( sortedNodes.size() < 2 )
			return true;

		return ( (this.getIndegree(sortedNodes.get(0)) == 0) && (this.getIndegree(sortedNodes.get(1)) > 0) );
	}

	public boolean isRootedForest()
	{
		//TODO make this more efficient
		Set<Graph<N,E>> components = this.getMaximallyConnectedComponents();
		for(Graph<N,E> component : components)
		{
			DirectedGraph<N,E> directedComponent = new ImmutableDirectedAdjacencyGraph<N,E>(component);
			if( !directedComponent.isRootedTree() )
				return false;
		}
		return true;
	}

	@Override
	public AbstractDirectedAdjacencyGraph<N, E> cloneAdd(final E newEdge)
	{
		return (AbstractDirectedAdjacencyGraph<N, E>) super.cloneAdd(newEdge);
	}

	@Override
	public AbstractDirectedAdjacencyGraph<N, E> cloneAdd(final N newNode)
	{
		return (AbstractDirectedAdjacencyGraph<N, E>) super.cloneAdd(newNode);
	}

	@Override
	public AbstractDirectedAdjacencyGraph<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (AbstractDirectedAdjacencyGraph<N, E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public AbstractDirectedAdjacencyGraph<N, E> cloneRemove(final E edgeToRemove)
	{
		return (AbstractDirectedAdjacencyGraph<N, E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public AbstractDirectedAdjacencyGraph<N, E> cloneRemove(final N nodeToRemove)
	{
		return (AbstractDirectedAdjacencyGraph<N, E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public AbstractDirectedAdjacencyGraph<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (AbstractDirectedAdjacencyGraph<N, E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public AbstractDirectedAdjacencyGraph<N, E> clone()
	{
		return (AbstractDirectedAdjacencyGraph<N, E>) super.clone();
	}
}
