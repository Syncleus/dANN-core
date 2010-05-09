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

public class ImmutableUndirectedEdge<N> extends AbstractBidirectedEdge<N>
{
	private static final long serialVersionUID = -7729189071866403594L;
	
	public ImmutableUndirectedEdge(final N leftNode, final N rightNode)
	{
		super(leftNode, EndState.NONE, rightNode, EndState.NONE);
	}

	public List<N> getTraversableNodes(final N node)
	{
		if( this.getLeftNode().equals(node) )
			return Collections.singletonList(this.getRightNode());
		else if( this.getRightNode().equals(node) )
			return Collections.singletonList(this.getLeftNode());
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
		return false;
	}

	@Override
	public boolean isHalfEdge()
	{
		return false;
	}

	@Override
	public boolean isLooseEdge()
	{
		return true;
	}

	@Override
	public boolean isOrdinaryEdge()
	{
		return false;
	}

	public ImmutableUndirectedEdge<N> disconnect(final N node)
	{
		if(node == null)
			throw new IllegalArgumentException("node can not be null", new NullPointerException());
		if(!this.getNodes().contains(node))
			throw new IllegalArgumentException("node is not currently connected to");

		return (ImmutableUndirectedEdge<N>) this.remove(node);
	}

	public ImmutableUndirectedEdge<N> disconnect(final List<N> nodes)
	{
		if(nodes == null)
			throw new IllegalArgumentException("node can not be null", new NullPointerException());
		if(!this.getNodes().containsAll(nodes))
			throw new IllegalArgumentException("node is not currently connected to");

		return (ImmutableUndirectedEdge<N>) this.remove(nodes);
	}

	@Override
	public ImmutableUndirectedEdge<N> clone()
	{
		return (ImmutableUndirectedEdge<N>) super.clone();
	}
}
