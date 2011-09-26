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
import com.syncleus.dann.graph.topological.Topography;
import com.syncleus.dann.graph.tree.TreeOptimizedGraph;
import com.syncleus.dann.graph.tree.Trees;

public abstract class AbstractTreeAdjacencyGraph<N, E extends BidirectedEdge<N>> extends AbstractBidirectedAdjacencyGraph<N, E> implements TreeGraph<N, E>, TreeOptimizedGraph<N, E>
{
	// TODO restrict tree's to only maximally connected trees or perhaps just tree in general

	protected AbstractTreeAdjacencyGraph()
	{
		super();
	}

	protected AbstractTreeAdjacencyGraph(final BidirectedGraph<N, E> copyGraph)
	{
		super(copyGraph.getTargets(), copyGraph.getEdges());
		if( !Trees.isTree(copyGraph) )
			throw new IllegalArgumentException("copyGraph is not a Tree");
	}

	protected AbstractTreeAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
		if( !Trees.isTree(this) )
			throw new IllegalArgumentException("edges do not form a tree graph");
	}

	@Override
	public boolean isTree()
	{
		return true;
	}

	@Override
	public boolean isSpanningTree(final CloudGraph<N, E> subGraph)
	{
		throw new UnsupportedOperationException("We have not optimized for this, allow default algorithms to calcualte");
	}
	@Override
	public boolean isForest()
	{
		//all trees are forests
		return true;
	}

	@Override
	public boolean isLeaf(final N node)
	{
		return (Topography.getDegree(this, node) == 1);
	}

	@Override
	public boolean isLeaf(final E edge)
	{
		for(final N node : edge.getTargets())
			if( this.isLeaf(node) )
				return true;
		return false;
	}

	@Override
	protected AbstractTreeAdjacencyGraph<N, E> clone()
	{
		return (AbstractTreeAdjacencyGraph<N, E>) super.clone();
	}
}
