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

public class ImmutableDirectedEdge<N> extends AbstractBidirectedEdge<N> implements DirectedEdge<N>
{
	private static final long serialVersionUID = -7589242369886611386L;

	public ImmutableDirectedEdge(final N source, final N destination)
	{
		super(source, EndState.INWARD, destination, EndState.OUTWARD);
	}

	public N getSourceNode()
	{
		return this.getLeftNode();
	}

	public N getDestinationNode()
	{
		return this.getRightNode();
	}

	public List<N> getTraversableNodes(final N node)
	{
		if (this.getSourceNode().equals(node))
			return Collections.singletonList(this.getDestinationNode());
		else if (this.getDestinationNode().equals(node))
			return Collections.emptyList();
		else
			throw new IllegalArgumentException("node is not one of the end points!");
	}

	@Override
	public boolean isIntroverted()
	{
		return false;
	}

	@Override
	public boolean isExtraverted()
	{
		return false;
	}

	@Override
	public boolean isDirected()
	{
		return true;
	}

	@Override
	public boolean isHalfEdge()
	{
		return false;
	}

	@Override
	public boolean isLooseEdge()
	{
		return false;
	}

	@Override
	public boolean isOrdinaryEdge()
	{
		return true;
	}

	@Override
	public String toString()
	{
		return this.getSourceNode() + "->" + this.getDestinationNode();
	}

	public ImmutableDirectedEdge<N> disconnect(final N node)
	{
		if (node == null)
			throw new IllegalArgumentException("node can not be null");
		if (!this.getNodes().contains(node))
			throw new IllegalArgumentException("node is not currently connected to");
		return (ImmutableDirectedEdge<N>) this.remove(node);
	}

	public ImmutableDirectedEdge<N> disconnect(final List<N> nodes)
	{
		if (nodes == null)
			throw new IllegalArgumentException("node can not be null");
		if (!this.getNodes().containsAll(nodes))
			throw new IllegalArgumentException("node is not currently connected to");
		return (ImmutableDirectedEdge<N>) this.remove(nodes);
	}

	@Override
	public ImmutableDirectedEdge<N> clone()
	{
		return (ImmutableDirectedEdge<N>) super.clone();
	}
}
